package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;

public class Robot extends IterativeRobot{

    Joystick discoStick;
    RobotDrive drive;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        discoStick = new Joystick(0);
        drive = new RobotDrive(1,2,3,4);
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
        drive.mecanumDrive_Cartesian(discoStick.getX(), discoStick.getY(), discoStick.getTwist(),0);
    }
}
