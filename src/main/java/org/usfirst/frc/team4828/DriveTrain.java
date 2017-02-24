package org.usfirst.frc.team4828;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import org.usfirst.frc.team4828.Vision.PixyThread;
import org.usfirst.frc.team4828.Vision.Vision;


public class DriveTrain {
    private CANTalon frontLeft;
    private CANTalon frontRight;
    private CANTalon backLeft;
    private CANTalon backRight;
    private AHRS navx;

    private static final double TWIST_THRESHOLD = 0.15;
    private static final double DIST_TO_ENC = 1;
    private static final double TURN_DEADZONE = 1;
    private static final double TURN_SPEED = 48;
    private static final double VISION_DEADZONE = 0.5;
    private static final double PLACING_DIST = 2;

    /**
     * Create drive train object containing mecanum motor functionality.
     *
     * @param frontLeftPort  port of the front left motor
     * @param backLeftPort   port of the back left motor
     * @param frontRightPort port of the front right motor
     * @param backRightPort  port of the back right motor
     */
    public DriveTrain(int frontLeftPort, int backLeftPort, int frontRightPort, int backRightPort) {
        frontLeft = new CANTalon(frontLeftPort);
        frontRight = new CANTalon(frontRightPort);
        backLeft = new CANTalon(backLeftPort);
        backRight = new CANTalon(backRightPort);

        frontLeft.setPID(0.6, 0, 0);
        frontRight.setPID(0.6, 0, 0);
        backLeft.setPID(0.6, 0, 0);
        backRight.setPID(0.6, 0, 0);

        navx = new AHRS(SPI.Port.kMXP);
    }

    /**
     * Test drive train object.
     */
    public DriveTrain(boolean gyro) {
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
    public static void normalize(double[] wheelSpeeds) {
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
    public double[] rotateVector(double xcomponent, double ycomponent) {
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
    public void mecanumDriveAbsolute(double xcomponent, double ycomponent, double rotation) {
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
    public void mecanumDrive(double xcomponent, double ycomponent, double rotation) {
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
     */
    public void moveDistance(double dist) {
        double encchange = dist * DIST_TO_ENC;

        frontLeft.changeControlMode(CANTalon.TalonControlMode.Position);
        frontRight.changeControlMode(CANTalon.TalonControlMode.Position);
        backLeft.changeControlMode(CANTalon.TalonControlMode.Position);
        backRight.changeControlMode(CANTalon.TalonControlMode.Position);

        frontLeft.set(frontLeft.getEncPosition() + encchange);
        frontRight.set(frontRight.getEncPosition() + encchange);
        backLeft.set(backLeft.getEncPosition() + encchange);
        backRight.set(backRight.getEncPosition() + encchange);
    }

    /**
     * @param pos    1 = Right, 2 = Middle, 3 = Right
     * @param vision
     */
    public void placeGear(int pos, Vision vision) {
        if (pos == 1) {
            turnDegrees(30);
        } else if (pos == 2) {
            turnDegrees(90);
        } else {
            turnDegrees(150);
        }

        while (vision.horizontalOffset() <= VISION_DEADZONE) {
            moveDistance(vision.horizontalOffset());
        }
        while (vision.transverseOffset() >= PLACING_DIST) {
            mecanumDrive(0.5, 0, 0);
        }
        brake();
    }

    /**
     * Turn all wheels slowly for testing purposes.
     */

    public void testMotors() {
        frontLeft.set(.2);
        frontRight.set(.2);
        backLeft.set(.2);
        backRight.set(.2);
    }

    /**
     * <<<<<<< HEAD
     * Turns at a certain speed
     *
     * @param speed double -1-1
     */
    public void turn(double speed) {
        frontLeft.set(-speed);
        backLeft.set(-speed);
        frontRight.set(speed);
        backRight.set(speed);
    }

    /**
     * Turn a certain amount of degrees
     *
     * @param degrees target degrees
     */
    public void turnDegrees(double degrees) {
        if (navx.getAngle() % 360 + TURN_DEADZONE > degrees) {
            turn(TURN_SPEED);
        } else {
            turn(-TURN_SPEED);
        }
    }

    /**
     * Turn all wheels at set speeds
     * =======
     * Turn all wheels at set speeds.
     * >>>>>>> release/bag
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
     * Use PID to lock the robot in its current position.
     */
    public void lock() {
        frontLeft.changeControlMode(CANTalon.TalonControlMode.Position);
        frontRight.changeControlMode(CANTalon.TalonControlMode.Position);
        backRight.changeControlMode(CANTalon.TalonControlMode.Position);
        backLeft.changeControlMode(CANTalon.TalonControlMode.Position);
        frontLeft.set(frontLeft.get());
        frontRight.set(frontRight.get());
        backRight.set(backRight.get());
        backLeft.set(backLeft.get());
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
        System.out.print((backLeft.getPosition() + backRight.getPosition() + frontLeft.getPosition() + frontRight.getPosition()) / 4);
    }

    /**
     * Set the motors back to normal speed control.
     */
    public void unlock() {
        frontLeft.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        frontRight.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        backRight.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
        backLeft.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    }

    /**
     * Zero the gyro.
     */
    public void reset() {
        navx.reset();
    }
}
