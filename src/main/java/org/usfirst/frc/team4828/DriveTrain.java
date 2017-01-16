package org.usfirst.frc.team4828;


import com.ctre.CANTalon;

public class DriveTrain {
    CANTalon frontLeft, frontRight, backLeft, backRight;

    public DriveTrain(int frontLeftPort, int backLeftPort, int frontRightPort, int backRightPort) {
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

    /**
     * Rotate A vector in cartesian space
     *
     * @param x     X component of the vector
     * @param y     Y component of the vector
     * @param angle Angle by which to rotate the vector
     * @return The resultant vector as a double[2]
     */
    private static double[] rotateVector(double x, double y, double angle) {
        double cosA = Math.cos(angle * (3.14159 / 180.0));
        double sinA = Math.sin(angle * (3.14159 / 180.0));
        double[] out = new double[2];
        out[0] = x * cosA - y * sinA;
        out[1] = x * sinA + y * cosA;
        return out;
    }

    public void mecanumDrive(double x, double y, double rotation, double gyroAngle) {
        // Negate y for the joystick.
        y = -y;
        // Compenstate for gyro angle.
        double[] rotated = rotateVector(x, y, gyroAngle);
        x = rotated[0];
        y = rotated[1];

        double[] wheelSpeeds = new double[4];
        wheelSpeeds[0] = x + y + rotation;
        wheelSpeeds[1] = -x + y - rotation;
        wheelSpeeds[2] = -x + y + rotation;
        wheelSpeeds[3] = x + y - rotation;

        normalize(wheelSpeeds);
        frontLeft.set(wheelSpeeds[0]);
        frontRight.set(wheelSpeeds[1]);
        backLeft.set(wheelSpeeds[2]);
        backRight.set(wheelSpeeds[3]);
    }

    public void testMotors() {
        frontLeft.set(.5);
        frontRight.set(.5);
        backLeft.set(.5);
        backRight.set(.5);
    }

}
