package org.usfirst.frc.team4828;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4828.Vision.Pixy;


public class DriveTrain {
    private static final double TWIST_THRESHOLD = 0.15;
    private static final double MIN_X_SPEED = 0.3;
    private static final double MIN_Y_SPEED = 0.2;
    private static final double AUTON_SPEED = .3;
    private static final double TURN_DEADZONE = 1;
    private static final double TURN_SPEED = .25;
    private static final double MAX_HORIZONTAL_OFFSET = 36.0;
    private static final double VISION_DEADZONE = 0.5;
    private static final double PLACING_DIST = 8; //todo: determine distance from the wall to stop when placing gear
    private CANTalon frontLeft;
    private CANTalon frontRight;
    private CANTalon backLeft;
    private CANTalon backRight;
    private AHRS navx;

    /**
     * Create drive train object containing mecanum motor functionality.
     *
     * @param frontLeftPort  port of the front left motor
     * @param backLeftPort   port of the back left motor
     * @param frontRightPort port of the front right motor
     * @param backRightPort  port of the back right motor
     */
    DriveTrain(int frontLeftPort, int backLeftPort, int frontRightPort, int backRightPort) {
        frontLeft = new CANTalon(frontLeftPort);
        frontRight = new CANTalon(frontRightPort);
        backLeft = new CANTalon(backLeftPort);
        backRight = new CANTalon(backRightPort);
        frontLeft.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        frontRight.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        backLeft.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        backRight.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
        backLeft.reverseSensor(true);
        frontLeft.reverseSensor(true);
        navx = new AHRS(SPI.Port.kMXP);
    }

    /**
     * Test drive train object.
     */
    DriveTrain(boolean gyro) {
        if (gyro) {
            navx = new AHRS(SPI.Port.kMXP);
        }
        System.out.println("Created dummy drivetrain");
    }

    /**
     * Ensure that wheel speeds are valid numbers.
     *
     * @param wheelSpeeds wheel speeds
     */
    static void normalize(double[] wheelSpeeds) {
        double maxMagnitude = Math.abs(wheelSpeeds[0]);
        for (int i = 1; i < 4; i++) {
            double temp = Math.abs(wheelSpeeds[i]);
            if (maxMagnitude < temp) {
                maxMagnitude = temp;
            }
        }
        if (maxMagnitude > 1.0) {
            for (int i = 0; i < 4; i++) {
                wheelSpeeds[i] = wheelSpeeds[i] / maxMagnitude;
            }
        }
    }

    /**
     * Rotate a vector in Cartesian space.
     *
     * @param xcomponent X component of the vector
     * @param ycomponent Y component of the vector
     * @return the resultant vector as a double[2]
     */
    private double[] rotateVector(double xcomponent, double ycomponent) {
        double angle = navx.getAngle();
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double[] out = new double[2];
        out[0] = xcomponent * cosA - ycomponent * sinA;
        out[1] = xcomponent * sinA + ycomponent * cosA;
        return out;
    }

    /**
     * Adjust motor speeds according to joystick input.
     *
     * @param xcomponent x component of the joystick
     * @param ycomponent y component of the joystick
     * @param rotation   rotation of the joystick
     */
    void mecanumDriveAbsolute(double xcomponent, double ycomponent, double rotation) {
        if (Math.abs(rotation) <= TWIST_THRESHOLD) {
            rotation = 0.0;
        }

        // Negate y for the joystick.
        ycomponent = -ycomponent;
        double[] wheelSpeeds = new double[4];
        wheelSpeeds[0] = xcomponent + ycomponent + rotation;
        wheelSpeeds[1] = -xcomponent + ycomponent - rotation;
        wheelSpeeds[2] = -xcomponent + ycomponent + rotation;
        wheelSpeeds[3] = xcomponent + ycomponent - rotation;

        normalize(wheelSpeeds);
        frontLeft.set(wheelSpeeds[0]);
        frontRight.set(wheelSpeeds[1]);
        backLeft.set(wheelSpeeds[2]);
        backRight.set(wheelSpeeds[3]);
    }

    /**
     * Adjust motor speeds according to heading and joystick input.
     * Uses input from the gyroscope to determine field orientation.
     *
     * @param xcomponent x component of the joystick
     * @param ycomponent y component of the joystick
     * @param rotation   rotation of the joystick
     */
    void mecanumDrive(double xcomponent, double ycomponent, double rotation) {
        // Ignore tiny inadvertent joystick rotations
        if (Math.abs(rotation) <= TWIST_THRESHOLD) {
            rotation = 0.0;
        }

        // Negate y for the joystick.
        ycomponent = -ycomponent;
        // Compensate for gyro angle.
        double[] rotated = rotateVector(xcomponent, ycomponent);
        xcomponent = rotated[0];
        ycomponent = rotated[1];

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[0] = xcomponent + ycomponent + rotation;
        wheelSpeeds[1] = -xcomponent + ycomponent - rotation;
        wheelSpeeds[2] = -xcomponent + ycomponent + rotation;
        wheelSpeeds[3] = xcomponent + ycomponent - rotation;

        normalize(wheelSpeeds);
        frontLeft.set(wheelSpeeds[0]);
        frontRight.set(wheelSpeeds[1]);
        backLeft.set(wheelSpeeds[2]);
        backRight.set(wheelSpeeds[3]);
    }

    /**
     * Move motors a certain distance.
     *
     * @param dist distance
     * @param speed 0-1 no negatives
     */
    public void moveDistance(double dist, double speed) {
        int dir = 1;
        zeroEncoders();
        if (dist < 0) {
            dir = -1;
        }
        while (frontLeft.getEncPosition() < Math.abs(dist)) {
            mecanumDrive(0, speed * dir, 0);
        }
        brake();
    }

    /**
     * Gear placement routine.
     *
     * @param pos 1 = Right, 2 = Middle, 3 = Right
     */
    void placeGear(int pos, Pixy pixy, Ultrasonic ultrasonic, GearGobbler gobbler) {
        //todo: confirm angles for each side
        if (pixy.blocksDetected()) {
            if (pos == 1) {
                turnDegrees(-30);
            } else if (pos == 2) {
                turnDegrees(-90);
            } else if (pos == 3) {
                turnDegrees(-150);
            } else {
                turnDegrees(0);
            }
            int dir;
            while (Math.abs(pixy.horizontalOffset()) > VISION_DEADZONE) {
                dir = 1;
                if (pixy.horizontalOffset() < 0) {
                    dir = -1;
                }
                // center relative to the target
                mecanumDriveAbsolute(0, AUTON_SPEED * dir, 0);
            }
            while (ultrasonic.getDist() >= PLACING_DIST) {
                // approach the target
                dir = 1;
                if (pixy.horizontalOffset() < 0) {
                    dir = -1;
                }
                mecanumDriveAbsolute(AUTON_SPEED, AUTON_SPEED * dir, 0);
            }
            brake();
            gobbler.open();
            Timer.delay(.5);
            gobbler.close();
        }
    }

    /**
     * Teleop version finds nearest angle before starting.
     */
    void placeGear(Pixy pixy, Ultrasonic ultrasonic, GearGobbler gobbler) {
        //todo: round to nearest angle
        double angle = navx.getAngle();
        if (angle > 0 && angle < 60) {
            placeGear(1, pixy, ultrasonic, gobbler);
        }
    }

    /**
     * Turn a certain amount of degrees
     *
     * @param degrees target degrees
     */
    void turnDegrees(double degrees) {
        int dir = getOptimalDirection(getTrueAngle(), degrees);
        while (getTrueAngle() - TURN_DEADZONE > degrees || getTrueAngle() + TURN_DEADZONE < degrees) {
            mecanumDriveAbsolute(0, 0, TURN_SPEED * dir);
        }
        brake();
    }

    /**
     * Get the true navx angle
     *
     * @return 0 <= angle < 360
     */
    private double getTrueAngle() {
        return (navx.getAngle() + 360) % 360;
    }

    /**
     * Get the best direction to turn
     *
     * @param current current angle
     * @param target  target angle
     * @return -1 = left, 1 = right
     */
    private int getOptimalDirection(double current, double target) {
        if (Math.abs(current - target) <= 180) {
            if (current > target) {
                return -1;
            }
            return 1;
        }
        if (current > target) {
            return 1;
        }
        return -1;
    }

    /**
     * Turn all wheels at set speeds.
     *
     * @param fl speed for front left wheel
     * @param fr speed for front right wheel
     * @param bl speed for back left wheel
     * @param br speed for back right wheel
     */
    public void testMotors(int fl, int fr, int bl, int br) {
        frontLeft.set(fl);
        frontRight.set(fr);
        backLeft.set(bl);
        backRight.set(br);
    }

    /**
     * Turn all wheels slowly for testing purposes.
     */
    void testMotors() {
        frontLeft.set(.2);
        frontRight.set(.2);
        backLeft.set(.2);
        backRight.set(.2);
    }

    /**
     * Stop all motors.
     */
    public void brake() {
        frontLeft.set(0);
        frontRight.set(0);
        backRight.set(0);
        backLeft.set(0);
    }

    /**
     * @return the current gyro heading
     */
    public String toString() {
        return Double.toString(navx.getAngle());
    }

    /**
     * Prints current average encoder values.
     */
    public void debugEncoders() {
        System.out.println("bl " + backLeft.getPosition() + " br " + backRight.getPosition() + " fl " + frontLeft.getPosition() + " fr " + frontRight.getPosition());
    }

    /**
     * Zero the gyro.
     */
    public void reset() {
        navx.reset();
    }

    public void zeroEncoders() {
        frontLeft.setPosition(0);
        frontRight.setPosition(0);
        backLeft.setPosition(0);
        backRight.setPosition(0);
    }
}
