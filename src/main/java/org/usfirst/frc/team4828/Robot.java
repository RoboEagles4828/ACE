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
    private Shooter shoot;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        driveStick = new Joystick(0);
        drive = new DriveTrain(
                Ports.DT_FRONT_LEFT,
                Ports.DT_BACK_LEFT,
                Ports.DT_FRONT_RIGHT,
                Ports.DT_BACK_RIGHT
        );
        navx = new AHRS(SPI.Port.kMXP);
        us = new UltraThread(Ports.US_CHANNEL);
        shoot = new Shooter(Ports.MOTOR_LEFT, Ports.INDEXER_LEFT, Ports.SERVO_LEFT_MASTER, Ports.SERVO_LEFT_SLAVE);
        // Master is the one on the right if you are looking at the back of the shooter
        shoot.servos.calibrate(1, .3, 0);
        shoot.servos.calibrate(2, .6, 1);
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        System.out.println("Entering auton...");
    }

    @Override
    public void autonomousPeriodic() {
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
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
        shoot.servos.set(.5);
    }

    @Override
    public void testPeriodic() {
        if (driveStick.getRawButton(9)) {
            System.out.println("RAISING");
            shoot.servos.raise();
        }
        if (driveStick.getRawButton(10)) { //slave  = back left .66 - 1  master .33 - 0
            System.out.println("LOWERING");
            shoot.servos.lower();
        }
        if (driveStick.getRawButton(11)) {
            shoot.servos.set(0);
        }
        if (driveStick.getRawButton(12)) {
            shoot.servos.set(1);
        }
        System.out.println(shoot.servos);
        Timer.delay(0.1);
    }

    @Override
    public void disabledInit() {
        System.out.println("Disabling robot");
    }
}
