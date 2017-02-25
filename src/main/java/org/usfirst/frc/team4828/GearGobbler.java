package org.usfirst.frc.team4828;

public class GearGobbler {
    public ServoGroup servo;

    GearGobbler(int leftServoPort, int rightServoPort) {
        servo = new ServoGroup(leftServoPort, rightServoPort);
    }

    public void open() {
        servo.set(1);
    }

    public void close() {
        servo.set(0);
    }
}
