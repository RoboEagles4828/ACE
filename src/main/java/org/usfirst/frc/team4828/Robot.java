package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.*;
import com.kauailabs.navx.frc.AHRS;

public class Robot extends IterativeRobot {
    private static final int SUPPLIED_VOLTS = 5;

    Joystick driveStick;
    DriveTrain drive;
    static AHRS navx;
    DigitalInput ir;
    AnalogInput us;
    double range;
    AccumulatorResult accum;
    int bits;

    boolean use_units = true;
    double min_voltage = .5;
    double voltage_range = 5.0 - min_voltage;
    double min_distance = 3.0;
    double distance_range = 60.0 - min_distance;

    @Override
    public void robotInit() {
        super.robotInit();
        System.out.println("THE ROBOT TURNED ON");
        driveStick = new Joystick(0);
        drive = new DriveTrain(1, 2, 3, 4);
        navx = new AHRS(SPI.Port.kMXP);
        ir = new DigitalInput(2);
        us = new AnalogInput(0);
        accum = new AccumulatorResult();
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
        drive.mecanumDrive(driveStick.getX() / 2, driveStick.getY() / 2, driveStick.getTwist() / 2, navx.getAngle());
        if (driveStick.getRawButton(11)) {
            navx.reset();
        }

        //System.out.println("Angle: " + navx.getAngle());
        System.out.println("IR Status: " + ir.get());


    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
    }

    public void testPeriodic() {
        us.setOversampleBits(4);
        us.setAverageBits(2);

        range = ((((us.getAverageVoltage())) * (SUPPLIED_VOLTS / 1024.0)));

        System.out.println("LSB: " + us.getLSBWeight() + " Offset: " + us.getOffset());
        System.out.println("Ultrasonic Dist: " + range * 5 * 10);

        Timer.delay(0.05);
    }
}
