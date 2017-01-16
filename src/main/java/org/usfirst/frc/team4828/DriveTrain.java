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
}
