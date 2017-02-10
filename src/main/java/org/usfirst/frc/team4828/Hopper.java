package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Spark;

public class Hopper {

    private static final double AGITATOR_SPEED = 0.5;
    private static final double INTAKE_SPEED = 0.5;

    private Spark left, right, intakeMotor;

    public void Hopper(int leftPort, int rightPort){
        left = new Spark(leftPort);
        right = new Spark(rightPort);
    }

    public void stir(){
        left.set(AGITATOR_SPEED);
        right.set(AGITATOR_SPEED);
    }

    public void stopStir(){
        left.set(0);
        right.set(0);
    }

    public void intake(){
        intakeMotor.set(INTAKE_SPEED);
    }

    public void stopIntake(){
        intakeMotor.set(0);
    }

}
