package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.*;
import org.usfirst.frc.team4828.Vision.PixyThread;

public class Robot extends IterativeRobot {
    private Joystick driveStick, secondaryStick;
    private DriveTrain drive;
    private Shooter shoot;
    private PixyThread pixy;
    private Shooter rightShooter, leftShooter;
    private DigitalInput[] dipSwitch;
    private int autonSelect;
    private Climber climb;
    private Hopper hopper;
    private GearGobbler gearGobbler;
    private double startTime = 0.0;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        driveStick = new Joystick(0);
        secondaryStick = new Joystick(1);
        drive = new DriveTrain(
                Ports.DT_FRONT_LEFT,
                Ports.DT_BACK_LEFT,
                Ports.DT_FRONT_RIGHT,
                Ports.DT_BACK_RIGHT
        );

        climb = new Climber(Ports.CLIMBER_1, Ports.CLIMBER_2, Ports.HALLEFFECT_PORT);
        hopper = new Hopper(Ports.AGITATOR_1, Ports.AGITATOR_2, Ports.INTAKE);
        // Master is the one on the right if you are looking at the back of the shooter
        rightShooter = new Shooter(Ports.MOTOR_RIGHT, Ports.SERVO_RIGHT_MASTER, Ports.SERVO_RIGHT_SLAVE, Ports.INDEXER_RIGHT);
        leftShooter = new Shooter(Ports.MOTOR_LEFT, Ports.SERVO_LEFT_MASTER, Ports.SERVO_LEFT_SLAVE, Ports.INDEXER_LEFT);
        rightShooter.servos.calibrate(1, .3, 0);
        rightShooter.servos.calibrate(2, .6, 1);
        leftShooter.servos.calibrate(1, .75, .5);
        leftShooter.servos.calibrate(2, .35, .7);
        gearGobbler = new GearGobbler(Ports.LEFT_GEAR_GOBBLER, Ports.RIGHT_GEAR_GOBBLER);
        gearGobbler.servo.calibrate(2, 1, .8);
        gearGobbler.servo.calibrate(1, 0, .25);

        dipSwitch = new DigitalInput[4];
        dipSwitch[0] = new DigitalInput(Ports.DIPSWITCH_1);
        dipSwitch[1] = new DigitalInput(Ports.DIPSWITCH_2);
        dipSwitch[2] = new DigitalInput(Ports.DIPSWITCH_3);
        dipSwitch[3] = new DigitalInput(Ports.DIPSWITCH_4);

        // Starting servo positions
        //TODO: check if setting servos in robotInit actually works
        rightShooter.servos.set(0);
        leftShooter.servos.set(0);
        gearGobbler.retract();
        gearGobbler.open();
//        rightShooter.calibrateIndexer(0, 1);
//        leftShooter.calibrateIndexer(0, 1);
    }

    @Override
    public void autonomousInit() {
        super.autonomousInit();
        // Bit shift the switches repeatedly to read it into an int
        autonSelect = 0;
        for (int i = 0; i < 4; i++) {
            autonSelect += (dipSwitch[i].get() ? 1 : 0) * (1 << i);
        }
        gearGobbler.retract();
        gearGobbler.open();
        System.out.println("Entering auton number " + autonSelect);
        drive.reset();
        startTime = Timer.getFPGATimestamp();
    }

    @Override
    public void autonomousPeriodic() {
        super.autonomousPeriodic();
        double time = Timer.getFPGATimestamp() - startTime;
        switch (autonSelect) {
            case 0:
                // DO NOTHING
                break;
            case 1:
                // Shoot 10
                rightShooter.servos.set(.6);
                leftShooter.servos.set(.4);
                rightShooter.spinUp(25000);
                leftShooter.spinUp(25000);
                hopper.stir();
                break;
            case 2:
                // Place gear on center
                break;
            case 3:
                if (time < 4) {
                    drive.mecanumDriveAbsolute(.4, 0, 0);
                } else if (time > 6 && time < 8) {
                    drive.mecanumDriveAbsolute(-.4, 0, 0);
                } else if (time < 6) {
                    drive.brake();
                    gearGobbler.close();
                    Timer.delay(.5);
                    gearGobbler.push();
                } else if (time > 10) {
                    gearGobbler.open();
                    gearGobbler.retract();
                } else {
                    drive.brake();
                }

                break;
            case 4:
                // Shoot 10 fuel
                break;
            case 5:
                if (time < 7) {
                    drive.mecanumDriveAbsolute(.4, 0, 0);
                } else {
                    drive.brake();
                }
                break;
            case 15:
                System.out.println("Safe Auton... doing nothing");
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
        pixy.start();
    }

    @Override
    public void teleopPeriodic() {
        super.teleopPeriodic();

        double magnitude = ((-driveStick.getThrottle()) + 1) / 2;

        //DRIVE
        drive.mecanumDrive(driveStick.getX(), driveStick.getY(), driveStick.getTwist() / 2);

        //INTAKE
        if (driveStick.getRawButton(11) || secondaryStick.getRawButton(11)) {
            hopper.intake();
        } else {
            hopper.stopIntake();
        }

        //GYRO and ENCODERS
        if (driveStick.getRawButton(7) || secondaryStick.getRawButton(7)) {
            drive.reset();
            drive.zeroEncoders();
        }

        //CLIMBER
        if (driveStick.getRawButton(8) || secondaryStick.getRawButton(8)) {
            climb.printDebug();
            climb.raise();
        } else if (driveStick.getRawButton(9)) {
            climb.reset();
        } else {
            climb.stop();
        }

        //GEAR PLACEMENT
        if (driveStick.getRawButton(12) || secondaryStick.getRawButton(12)) {
            gearGobbler.close();
            Timer.delay(.5);
            gearGobbler.push();
            Timer.delay(.4);
        } else {
            gearGobbler.retract();
            gearGobbler.open();
        }

        if (driveStick.getRawButton(5)) {
            drive.debugEncoders();
        }

        //SHOOTING
        if (driveStick.getRawButton(1) || secondaryStick.getRawButton(1)) {
            rightShooter.spinUp((int) (magnitude * 30000));
            leftShooter.spinUp((int) (magnitude * 30000));
            //leftShooter.spinUpAbsolute(magnitude);
            //rightShooter.spinUpAbsolute(magnitude*.9);
            hopper.stir();
            System.out.println(leftShooter + "     " + rightShooter);
        } else {
            leftShooter.spinDown();
            rightShooter.spinDown();
            hopper.stopStir();
        }

        //SHOOTER SERVOS
        if (driveStick.getRawButton(10) || secondaryStick.getRawButton(10)) {
            leftShooter.servos.set(magnitude);
            rightShooter.servos.set(magnitude * .8);
            //System.out.println(leftShooter.servos + "      " + rightShooter.servos);
        }

        if (driveStick.getRawButton(2)) {
            System.out.println("navx " + drive + "    pixy data: " + pixy.distanceFromLift());
            System.out.println(magnitude * 30000 + "     " + magnitude);
        }
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
        if(pixy == null){
            pixy = new PixyThread(Ports.US_CHANNEL);
            pixy.start();
        }
    }

    @Override
    public void testPeriodic() {
        double temp = ((-driveStick.getThrottle()) + 1) / 2;
//        if (driveStick.getRawButton(10)) {
//            System.out.print("navx " + drive);
//            drive.turnDegrees(driveStick.getThrottle() * 360);
//        }

//        if (driveStick.getRawButton(9)) {
//            drive.reset();
//            drive.zeroEncoders();
//        }
//        if (driveStick.getRawButton(12)) {
//            drive.debugEncoders();
//        }
        if (driveStick.getRawButton(11)) {
            System.out.println("ultra data: " + pixy.distanceFromLift());
            System.out.println("raw data" + pixy);
            System.out.println("offset" + pixy.horizontalOffset());
        }

//        if (driveStick.getRawButton(10)) {
//            leftShooter.servos.set(temp);
//            System.out.println(leftShooter.servos);
//        }
//        if (driveStick.getRawButton(9)) {
//            rightShooter.servos.set(temp);
//            System.out.println(rightShooter.servos);
//        }
        if (driveStick.getRawButton(2)) {
            System.out.print("joystick pos: " + temp);
            System.out.println(" pov pos: " + driveStick.getPOV());
        }
        Timer.delay(.1);
    }

    @Override
    public void disabledInit() {
        System.out.println("Disabling robot");
        if (pixy != null) {
            pixy.terminate();
        }
    }
}
