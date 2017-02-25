package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class GearGobbler {
    private Servo servo;
    private double openPos, closePos;

    GearGobbler(int servoPort) {
        servo = new Servo(servoPort);
    }

    GearGobbler(int servoPort, double openPos, double closePos) {
        servo = new Servo(servoPort);
        calibrate(openPos, closePos);
    }

    public void calibrate(double open, double close) {
        openPos = open;
        closePos = close;
    }

    public void setAbsolute(double pos) {
        servo.set(pos);
    }

    public void open() {
        servo.set(openPos);
    }

    public void close() {
        servo.set(closePos);
    }
}
