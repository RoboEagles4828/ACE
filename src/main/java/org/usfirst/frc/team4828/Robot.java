package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4828.Vision.PixyThread;

public class Robot extends IterativeRobot {
    private Joystick driveStick;
    private DriveTrain drive;
    private Shooter shoot;
    private PixyThread pixy;
    private Shooter rightShooter, leftShooter;
    private DigitalInput[] dipSwitch;
    private int autonSelect;
    private Climber climb;
    private Hopper hopper;
    private GearGobbler gearGobbler;

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

        climb = new Climber(Ports.CLIMBER_1, Ports.CLIMBER_2);
        hopper = new Hopper(Ports.AGITATOR, Ports.INTAKE);
        // Master is the one on the right if you are looking at the back of the shooter
        rightShooter = new Shooter(Ports.MOTOR_RIGHT, Ports.SERVO_RIGHT_MASTER, Ports.SERVO_RIGHT_SLAVE);
        leftShooter = new Shooter(Ports.MOTOR_LEFT, Ports.SERVO_LEFT_MASTER, Ports.SERVO_LEFT_SLAVE);
        rightShooter.servos.calibrate(1, .3, 0);
        rightShooter.servos.calibrate(2, .6, 1);
        leftShooter.servos.calibrate(1, .75, .35);
        leftShooter.servos.calibrate(2, .3, .75);

        dipSwitch = new DigitalInput[4];
        dipSwitch[0] = new DigitalInput(Ports.DIPSWITCH_1);
        dipSwitch[1] = new DigitalInput(Ports.DIPSWITCH_2);
        dipSwitch[2] = new DigitalInput(Ports.DIPSWITCH_3);
        dipSwitch[3] = new DigitalInput(Ports.DIPSWITCH_4);

        gearGobbler = new GearGobbler(Ports.SERVO_GEAR_GOBBLER);
        gearGobbler.calibrate(0, 1);

        // Start pointing straight up
        //TODO: check if setting servos in robotInit actually works
        rightShooter.servos.set(0);
        leftShooter.servos.set(0);

        pixy = new PixyThread(Ports.US_CHANNEL);
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        // Bit shift the switches repeatedly to read it into an int
        autonSelect = 0;
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
        double distance = 93.3; //inches to move from wall for gear placement
        switch (autonSelect) {
            case 0:
                // DO NOTHING
                break;
            case 1:
                // Place gear on right side
                drive.moveDistance(distance);
                drive.placeGear(3, pixy, gearGobbler);
                break;
            case 2:
                // Place gear on center
                drive.moveDistance(distance / 2);
                drive.placeGear(2, pixy, gearGobbler);
                break;
            case 3:
                // Place gear on left side
                drive.moveDistance(distance);
                drive.placeGear(1, pixy, gearGobbler);
                break;
            case 4:
                // Shoot 10 fuel
                break;
            case 5:
                // The crazy running into hopper and shooting tons of balls plan
                break;
            case 15:
                System.out.println("Safe Auton... doing nothing");
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
        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist() / 4);
        if (driveStick.getRawButton(11)) {
            drive.reset();
        }
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
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
