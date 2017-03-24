package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class GearGobbler {
    public ServoGroup servo;
    public Servo pusher;

    GearGobbler(int leftServoPort, int rightServoPort) {
        servo = new ServoGroup(leftServoPort, rightServoPort);
        pusher = new Servo(Ports.PUSH_GEAR_GOBBLER);
    }

    public void debug(double pos){
        pusher.set(pos);
        System.out.println(pusher.get());
    }

    public void push(){
        pusher.set(0);
    }

    public void retract(){
        pusher.set(.45);
    }

    public void open() {
        servo.set(0);
    }

    public void close() {
        servo.set(.8);
    }
}
