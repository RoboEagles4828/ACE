package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Spark;

public class Hopper {

    private static final double AGITATOR_SPEED = 0.5;
    private static final double INTAKE_SPEED = 0.5;

    private Spark agitator;
    private Spark intakeMotor;

    /**
     * Create hopper encapsulating the intake and agitator functionality.
     * @param agitatorPort port of the agitator motor
     * @param intakePort port of the intake motor
     */
    public Hopper(int agitatorPort, int intakePort){
        agitator = new Spark(agitatorPort);
        intakeMotor = new Spark(intakePort);
    }

    public void stir(){
        agitator.set(AGITATOR_SPEED);
    }

    public void stopStir(){
        agitator.set(0);
    }

    public void intake(){
        intakeMotor.set(INTAKE_SPEED);
    }

    public void stopIntake(){
        intakeMotor.set(0);
    }

}
