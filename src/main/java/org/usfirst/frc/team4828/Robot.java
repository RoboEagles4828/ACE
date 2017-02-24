package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import org.usfirst.frc.team4828.Vision.PixyThread;

public class Robot extends IterativeRobot {
    private Joystick driveStick;
    private DriveTrain drive;
    private DigitalInput ir;
    private Shooter shoot;
    private PixyThread pixy;
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
        dipSwitch[3] = new DigitalInput(Ports.DIPSWITCH_4);
        dipSwitch[2] = new DigitalInput(Ports.DIPSWITCH_3);
        System.out.println("robotinit check");
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        for (int i = 0; i < 4; i++) {
            autonSelect += (dipSwitch[i].get() ? 1 : 0) * (1 << i);
        }
        System.out.println("Entering auton number " + autonSelect);
        drive.reset();
        //pixy.start();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
        double distance = 93.3;
        switch (autonSelect) {
            case 0:
                // TODO: Shoot 10 fuel
                shoot.startShooter();
                shoot.spinUp(); //add speed here?
                break;
            case 1:
                // Place gear on right side
                drive.moveDistance(-distance);
                drive.placeGear(3, pixy);
                break;
            case 2:
                // Place gear on center
                drive.placeGear(2, pixy);
                break;
            case 3:
                // Place gear on left side
                drive.moveDistance(distance);
                drive.placeGear(1, pixy);
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
        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist()/4);
        if (driveStick.getRawButton(11)) {
            drive.reset();
        }
        //System.out.println("Angle: " + navx.getAngle());
    }

    @Override
    public void testInit() {
        super.testInit();
        pixy = new PixyThread(Ports.US_CHANNEL);
        pixy.start();
    }

    @Override
    public void testPeriodic() {
        System.out.println(drive);
        System.out.println(pixy);
        System.out.println(pixy.horizontalOffset());
        Timer.delay(.1);
    }

    @Override
    public void disabledInit() {
        System.out.println("Disabling robot");
        if (pixy != null) {
            //System.out.println(pixy);
            pixy.terminate();
            System.out.println("Stopping thread");
        }
    }
}
