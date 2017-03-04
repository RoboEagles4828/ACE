package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;

public class Climber {

    private Victor climberMotor1;
    private Victor climberMotor2;
    private DigitalInput halleffect;
    private static final double MOTOR_SPEED = 0.8;
    private static final double RESET_SPEED = 0.2;

    /**
     * Create climber object encapsulating the climber motor.
     *
     * @param motorPort1 port of the first climber motor
     * @param motorPort2 port of the second climber motor
     */
    public Climber(int motorPort1, int motorPort2, int halleffectPort) {
        climberMotor1 = new Victor(motorPort1);
        climberMotor2 = new Victor(motorPort2);
        halleffect = new DigitalInput(halleffectPort);
    }

    /**
     * Raise the robot at a given speed.
     *
     * @param speed double speed
     */
    public void raise(double speed) {
        climberMotor1.set(-speed);
        climberMotor2.set(speed);
    }

    /**
     * Raise the robot at the default speed.
     */
    public void raise() {
        climberMotor1.set(-MOTOR_SPEED);
        climberMotor2.set(MOTOR_SPEED);
    }

    public void reset() {
        if (halleffect.get()) {
            raise(RESET_SPEED);
        }
        else{
            stop();
        }
    }

    public void reset(double speed) {
        if (halleffect.get()) {
            raise(speed);
        }
        else{
            stop();
        }
    }

    /**
     * Lower the robot at a given speed.
     *
     * @param speed double speed (positive)
     */
    public void lower(double speed) {
        climberMotor1.set(speed);
        climberMotor2.set(-speed);
    }

    /**
     * Lower the robot at the default speed.
     */
    public void lower() {
        climberMotor1.set(MOTOR_SPEED);
        climberMotor2.set(-MOTOR_SPEED);
    }

    /**
     * Stop raising or lowering the robot.
     */
    public void stop() {
        climberMotor1.set(0);
        climberMotor2.set(0);
    }

    public void printDebug() {
        System.out.println(halleffect.get());
    }

}
