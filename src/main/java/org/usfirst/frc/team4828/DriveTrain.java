package org.usfirst.frc.team4828;


import com.ctre.CANTalon;

public class DriveTrain {
    private CANTalon frontLeft;
    private CANTalon frontRight;
    private CANTalon backLeft;
    private CANTalon backRight;

    private static final double TWIST_THRESHOLD = 0.15;

    DriveTrain(int frontLeftPort, int backLeftPort, int frontRightPort, int backRightPort) {
        frontLeft = new CANTalon(frontLeftPort);
        frontRight = new CANTalon(frontRightPort);
        backLeft = new CANTalon(backLeftPort);
        backRight = new CANTalon(backRightPort);
    }

    private static void normalize(double[] wheelSpeeds) {
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

    /** rotateVector - Rotate a vector in Cartesian space.
     *
     * @param x     X component of the vector
     * @param y     Y component of the vector
     * @param angle Angle by which to rotate the vector
     * @return The resultant vector as a double[2]
     */
    public static double[] rotateVector(double xComponent, double yComponent, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double[] out = new double[2];
        out[0] = xComponent * cosA - yComponent * sinA;
        out[1] = xComponent * sinA + yComponent * cosA;
        return out;
    }

    /** mecanumDrive - Adjust motor speeds according to joystick input.
     */
    public void mecanumDrive(double xComponent, double yComponent, double rotation) {
        // Ignore tiny inadvertent joystick rotations
        if (Math.abs(rotation) <= TWIST_THRESHOLD) {
             rotation = 0.0;
        }

        // Negate y for the joystick.
        yComponent = -yComponent;

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[0] = xComponent + yComponent + rotation;
        wheelSpeeds[1] = -xComponent + yComponent - rotation;
        wheelSpeeds[2] = -xComponent + yComponent + rotation;
        wheelSpeeds[3] = xComponent + yComponent - rotation;

        normalize(wheelSpeeds);
        frontLeft.set(wheelSpeeds[0]);
        frontRight.set(wheelSpeeds[1]);
        backLeft.set(wheelSpeeds[2]);
        backRight.set(wheelSpeeds[3]);
    }

    /** mecanumDrive - Adjust motor speeds according to heading and joystick input.
     * Uses input from the gyroscope to determine field orientation.
     */
    public void mecanumDrive(double xComponent, double yComponent, double rotation, double gyroAngle) {
        // Ignore tiny inadvertent joystick rotations
        if (Math.abs(rotation) <= TWIST_THRESHOLD) {
             rotation = 0.0;
        }

        // Negate y for the joystick.
        yComponent = -yComponent;
        // Compensate for gyro angle.
        double[] rotated = rotateVector(xComponent, yComponent, gyroAngle);
        xComponent = rotated[0];
        yComponent = rotated[1];

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[0] = xComponent + yComponent + rotation;
        wheelSpeeds[1] = -xComponent + yComponent - rotation;
        wheelSpeeds[2] = -xComponent + yComponent + rotation;
        wheelSpeeds[3] = xComponent + yComponent - rotation;

        normalize(wheelSpeeds);
        frontLeft.set(wheelSpeeds[0]);
        frontRight.set(wheelSpeeds[1]);
        backLeft.set(wheelSpeeds[2]);
        backRight.set(wheelSpeeds[3]);
    }

    /** testMotors - Turn all wheels slowly for testing purposes.
     */
    public void testMotors() {
        frontLeft.set(.2);
        frontRight.set(.2);
        backLeft.set(.2);
        backRight.set(.2);
    }

}
