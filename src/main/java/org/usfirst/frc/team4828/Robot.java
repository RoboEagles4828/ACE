package org.usfirst.frc.team4828;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;

public class Robot extends IterativeRobot {
    private Joystick driveStick;
    private DriveTrain drive;
    private AHRS navx;
    private DigitalInput ir;
    private UltraThread us;
    private DigitalInput[] dipSwitch;
    private int autonSelect;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        driveStick = new Joystick(0);
        drive = new DriveTrain(1, 2, 3, 4);
        navx = new AHRS(SPI.Port.kMXP);
        ir = new DigitalInput(2);
        us = new UltraThread(0);
        dipSwitch = new DigitalInput[4];
        dipSwitch[0] = new DigitalInput(6);
        dipSwitch[1] = new DigitalInput(4);
        dipSwitch[2] = new DigitalInput(5);
        dipSwitch[3] = new DigitalInput(3);
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        for (int i = 0; i < 4; i++) {
            autonSelect += (dipSwitch[i].get() ? 1 : 0) * (1 << i);
        }
        System.out.println("Entering auton number " + autonSelect);
        switch (autonSelect) {
            case 0:
                // Shoot 10 fuel
                break;
            case 1:
                // Place gear on right side
                break;
            case 2:
                // Place gear on center
                break;
            case 3:
                // Place gear on center
                break;
            case 4:
                // Shoot 10 fuel and place gear on left side
                break;
            case 5:
                // The crazy running into hopper and shooting tons of balls plan
                break;
            default:
                // Do nothing
        }
    }

    @Override
    public void autonomousPeriodic() {
        Timer.delay(.1);
        super.autonomousPeriodic();
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        System.out.println("Entering teleop...");
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist(), navx.getAngle());
        if (driveStick.getRawButton(11)) {
            navx.reset();
        }

        //System.out.println("Angle: " + navx.getAngle());
        //System.out.println("IR Status: " + ir.get());
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
        //.start();
    }

    @Override
    public void testPeriodic() {
        for (int i = 0; i < 4; i++) {
            System.out.print(" " + dipSwitch[i].get());
        }
        System.out.println();
        //System.out.println("Ultrasonic Dist: " + us.distIn + " inches");
        Timer.delay(0.1);
    }

    @Override
    public void disabledInit() {
        us.terminate();
        System.out.println("Stopping thread");
    }
}
