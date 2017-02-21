package org.usfirst.frc.team4828;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4828.Vision.Vision;

import javax.sound.sampled.Port;

public class Robot extends IterativeRobot {
    private Joystick driveStick;
    private DriveTrain drive;
    private AHRS navx;
    private DigitalInput ir;
    private Shooter shoot;
    private Vision vision;
    private DigitalInput[] dipSwitch;
    private int autonSelect;
    private Climber climb;

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
        climb = new Climber(Ports.CLIMBER_1, Ports.CLIMBER_2, Ports.HALLEFFECT_PORT);
        navx = new AHRS(SPI.Port.kMXP);
        shoot = new Shooter(Ports.MOTOR_LEFT, Ports.INDEXER_LEFT, Ports.SERVO_LEFT_MASTER, Ports.SERVO_LEFT_SLAVE);
        // Master is the one on the right if you are looking at the back of the shooter
        shoot.servos.calibrate(1, .3, 0);
        shoot.servos.calibrate(2, .6, 1);

        dipSwitch = new DigitalInput[4];
        dipSwitch[0] = new DigitalInput(Ports.DIPSWITCH_1);
        dipSwitch[1] = new DigitalInput(Ports.DIPSWITCH_2);
        dipSwitch[2] = new DigitalInput(Ports.DIPSWITCH_3);
        dipSwitch[3] = new DigitalInput(Ports.DIPSWITCH_4);
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
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
        shoot.servos.set(.5);
        vision = new Vision(Ports.US_CHANNEL);
        vision.start();
    }

    @Override
    public void testPeriodic() {
//        System.out.println("Ultrasonic Dist: " + us.distIn + " inches");
//        Timer.delay(0.1);
//        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist());
//        Timer.delay(0.1);
        drive.moveDistance(1000);
        Timer.delay(1);
        drive.moveDistance(-1000);
        Timer.delay(1);
//         if (driveStick.getRawButton(9)) {
//             System.out.println("RAISING");
//             shoot.servos.raise();
//         }
//         if (driveStick.getRawButton(10)) { //slave  = back left .66 - 1  master .33 - 0
//             System.out.println("LOWERING");
//             shoot.servos.lower();
//         }
//         if (driveStick.getRawButton(11)) {
//             shoot.servos.set(0);
//         }
//         if (driveStick.getRawButton(12)) {
//             shoot.servos.set(1);
//         }
//         System.out.println(shoot.servos);
//         System.out.println(vision);
//         System.out.println("move: " + vision.findX());
//         Timer.delay(0.1);
    }

    @Override
    public void disabledInit() {
        System.out.println("Disabling robot");
        if (vision != null) {
            vision.terminate();
        }
        System.out.println("Stopping thread");
    }
}
