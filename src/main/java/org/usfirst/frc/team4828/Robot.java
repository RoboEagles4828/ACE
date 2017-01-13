package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot{
    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
        System.out.println("Teleop running");
    }
}
