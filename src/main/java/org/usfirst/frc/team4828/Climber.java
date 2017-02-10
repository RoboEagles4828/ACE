package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Victor;

public class Climber {

    private Victor climberMotor;
    private static final double RAISE_SPEED = 0.5;
    private static final double LOWER_SPEED = -0.5;

    public Climber(int motorPort){
        climberMotor = new Victor(motorPort);
    }

    public void raise(double speed){
        climberMotor.set(speed);
    }

    public void raise(){
        climberMotor.set(RAISE_SPEED);
    }

    public void lower(double speed){
        climberMotor.set(-speed);
    }

    public void lower(){
        climberMotor.set(LOWER_SPEED);
    }

    public void stop(){
        climberMotor.set(0);
    }

}
