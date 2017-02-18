package org.usfirst.frc.team4828;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4828.Vision.Vision;

public class Robot extends IterativeRobot {

    private Joystick driveStick;
    private DriveTrain drive;
    private AHRS navx;
    private Vision vision;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        driveStick = new Joystick(0);
        /*drive = new DriveTrain(
                Ports.DT_FRONT_LEFT,
                Ports.DT_BACK_LEFT,
                Ports.DT_FRONT_RIGHT,
                Ports.DT_BACK_RIGHT
        );*/
        navx = new AHRS(SPI.Port.kMXP);
        vision = new Vision(Ports.US_CHANNEL);
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
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
        vision.start();
    }

    @Override
    public void testPeriodic() {
//        System.out.println("Ultrasonic Dist: " + us.distIn + " inches");
//        Timer.delay(0.1);
        System.out.println(vision);
        Timer.delay(0.1);
    }

    @Override
    public void disabledInit() {
        vision.terminate();
        System.out.println("Stopping thread");
    }
}
