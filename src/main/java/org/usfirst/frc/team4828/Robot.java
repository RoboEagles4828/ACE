package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4828.Vision.Vision;

public class Robot extends IterativeRobot {
    private Joystick driveStick;
    private DriveTrain drive;
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
        climb = new Climber(7, 8);

        shoot = new Shooter(Ports.MOTOR_LEFT, Ports.INDEXER_LEFT, Ports.SERVO_LEFT_MASTER, Ports.SERVO_LEFT_SLAVE);
        // Master is the one on the right if you are looking at the back of the shooter
        shoot.servos.calibrate(1, .3, 0);
        shoot.servos.calibrate(2, .6, 1);

        dipSwitch = new DigitalInput[4];
        dipSwitch[0] = new DigitalInput(Ports.DIPSWITCH_1);
        dipSwitch[1] = new DigitalInput(Ports.DIPSWITCH_2);
        dipSwitch[2] = new DigitalInput(Ports.DIPSWITCH_3);
        dipSwitch[3] = new DigitalInput(Ports.DIPSWITCH_4);
        vision = new Vision(Ports.US_CHANNEL);
        vision.start();
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        for (int i = 0; i < 4; i++) {
            autonSelect += (dipSwitch[i].get() ? 1 : 0) * (1 << i);
        }
        System.out.println("Entering auton number " + autonSelect);
        drive.reset();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
        double distance = 93.3;
        switch (autonSelect) {
            case 0:
                // TODO: Shoot 10 fuel
                break;
            case 1:
                // Place gear on right side
                drive.moveDistance(-distance);
                drive.placeGear('R', vision);
                break;
            case 2:
                // Place gear on center
                drive.placeGear('M', vision);
                break;
            case 3:
                // Place gear on left side
                drive.moveDistance(distance);
                drive.placeGear('L', vision);
                break;
            case 4:
                // TODO: Shoot 10 fuel and place gear on left side
                break;
            case 5:
                // TODO: The crazy running into hopper and shooting tons of balls plan
                break;
            default:
                // Do nothing
        }
        Timer.delay(.1);
    }

    @Override
    public void teleopInit() {
        super.teleopInit();
        System.out.println("Entering teleop...");
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();
        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist());
        if (driveStick.getRawButton(11)) {
            drive.reset();
        }
        //System.out.println("Angle: " + navx.getAngle());
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");

        vision = new Vision(Ports.US_CHANNEL);

        vision.start();
    }

    @Override
    public void testPeriodic() {
        drive.moveDistance(1000);
        Timer.delay(1);
        drive.moveDistance(-1000);
        Timer.delay(1);
        System.out.println(vision);
        System.out.println("Move " + vision.findHorizontalOffset() + " inches");
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
