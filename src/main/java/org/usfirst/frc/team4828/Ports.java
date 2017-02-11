package org.usfirst.frc.team4828;

/**
 * Created by Jackie on 2/11/2017.
 */
public class Ports {
    private Ports(){
        throw new AssertionError(
                "A Summoner has Disconnected.\n" +
                "(Assertion Error 'cause you instantiated class Ports.\n" +
                "What part of private don't you understand, Nikhil...\n" +
                "Sigh.)\n"
        );
    }
    // PWM
    public static final int INTAKE = 9;
    public static final int CLIMBER = 8;
    public static final int AGITATOR_LEFT = 6;
    public static final int AGITATOR_RIGHT = 7;

    // Drivetrain
    public static final int DT_FRONT_LEFT = 1;
    public static final int DT_BACK_LEFT = 2;
    public static final int DT_FRONT_RIGHT = 3;
    public static final int DT_BACK_RIGHT = 4;

    // Shooter
    public static final int SERVO_LEFT_1 = 0;
    public static final int SERVO_LEFT_2 = 1;
    public static final int SERVO_RIGHT_1 = 2;
    public static final int SERVO_RIGHT_2 = 3;

    // Sensors
    public static final int IR_CHANNEL = 2;
    public static final int US_CHANNEL = 0;
}
