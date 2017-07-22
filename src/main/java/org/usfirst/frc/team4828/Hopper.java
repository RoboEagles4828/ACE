package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Spark;

public class Hopper {

    private static final double AGITATOR_SPEED = 0.7;
    private static final double INTAKE_SPEED = 0.95;

    private Spark agitator1;
    private Spark agitator2;
    private Spark intakeMotor;

    /**
     * Create hopper encapsulating the intake and agitator functionality.
     *
     * @param agitatorPort port of the agitator motor
     * @param intakePort   port of the intake motor
     */
    public Hopper(int agitator1Port, int agitator2Port, int intakePort) {
        agitator1 = new Spark(agitator1Port);
        agitator2 = new Spark(agitator2Port);
        intakeMotor = new Spark(intakePort);
    }

    /**
     * Start stirring the hopper.
     */
    public void stir() {
        agitator1.set(AGITATOR_SPEED);
        agitator2.set(AGITATOR_SPEED);
    }

    /**
     * Stop stirring the hopper.
     */
    public void stopStir() {
        agitator1.set(0);
        agitator2.set(0);
    }

    /**
     * Start the intake.
     */
    public void intake() {
        intakeMotor.set(INTAKE_SPEED);
    }

    /**
     * Stop the intake.
     */
    public void stopIntake() {
        intakeMotor.set(0);
    }

}
