package org.usfirst.frc.team4828;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Servo;

public class Shooter extends Thread {
    private CANTalon shooterMotor;
    private Servo indexer;
    public ServoGroup servos;

    private static final double SHOOTER_SPEED = 0.8;
    private static final double SERVO_MULTIPLIER = 0.8;

    private static final double P = .5;
    private static final double I = 0;
    private static final double D = 0;

    private double indexerOpen = .8;
    private double indexerClose = .2;

    /**
     * Create shooter object that encapsulates all associated servos and motors.
     *
     * @param motorPort port of main shooter wheel
     * @param indexerPort port of indexer servo
     * @param masterPort port of master angle control servo
     * @param slavePort port of slave angle control servo
     */
    public Shooter(int motorPort, int indexerPort, int masterPort, int slavePort) {
        shooterMotor = new CANTalon(motorPort);
        shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
        shooterMotor.setPID(P, I, D);
        indexer = new Servo(indexerPort);
        servos = new ServoGroup(masterPort, slavePort);
    }

    /**
     * Calibrate the indexer.
     *
     * @param open double 0-1 open position
     * @param close double 0-1 close position
     */
    public void calibrateIndexer(double open, double close){
        indexerOpen = open;
        indexerClose = close;
    }

    /**
     * Start the shooter wheel using the default shooter speed.
     */
    public void spinUp() {
        shooterMotor.set(SHOOTER_SPEED * (1 - servos.get() * SERVO_MULTIPLIER));
    }

    /**
     * Start the shooter wheel.
     *
     * @param speed double 0-1 dictates fire speed
     */
    public void spinUp(double speed) {
        shooterMotor.set(speed * (1 - servos.get() * SERVO_MULTIPLIER));
    }

    /**
     * Slow the shooter wheel to a halt.
     */
    public void spinDown() {
        shooterMotor.set(0);
    }

    /**
     * Open indexer servo to allow fuel to contact the shooter wheel.
     */
    public void startShooter(){
        indexer.set(indexerOpen);
    }

    /**
     * Close indexer servo to block fuel from contacting the shooter wheel.
     */
    public void stopShooter(){
        indexer.set(indexerClose);
    }

    /**
     * Change the shooter motor back to normal speed control instead of PID.
     *
     * If they don't give us enough time to calibrate shooter PID, we will
     * have to resort to the tried and true way of spinning the shooter
     * wheel and not worry about it slowing down from contacting fuel.
     */
    public void superSecretSafetyBackup(){
        shooterMotor.changeControlMode(CANTalon.TalonControlMode.PercentVbus);
    }

}
