package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.Ultrasonic;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.AnalogInput;

public class Robot extends IterativeRobot {
    Joystick driveStick;
    DriveTrain drive;
    static AHRS navx;
    DigitalInput ir;
    AnalogInput us;
    double range;

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
        //System.out.println("IR Status: " + ir.get());

        range = us.getVoltage();
        if (range < min_voltage) {
            range = -2.0;
        } else {
            //first, normalize the voltage
            range = (range - min_voltage) / voltage_range;
            //next, denormalize to the unit range
            range = (range * distance_range) + min_distance;
            //finally, convert to centimeters
            range *= 2.54;
        }

        System.out.println("Ultrasonic Dist: " + range * 5 * 10);
        Timer.delay(0.05);
    }

    @Override
    public void testInit() {
        super.testInit();
        System.out.println("Entering test...");
    }

    @Override
    public void testPeriodic() {
        System.out.println("Angle: " + navx.getAngle());
    }
}
