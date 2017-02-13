package org.usfirst.frc.team4828;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;


public class ShooterThread extends Thread{
    private CANTalon shooterMotor;
    public ServoGroup servos;

    private static final int SHOOTER_SPEED = 1;
    private static final int P = 0;
    private static final int I = 0;
    private static final int D = 0;

    private Thread t;
    private String threadName = "shooter thread";
    private AnalogInput sensor;
    private boolean enabled;

    public ShooterThread(int motorPort, int masterPort, int slavePort){
        shooterMotor = new CANTalon(motorPort);
        servos = new ServoGroup(masterPort, slavePort);
    }

    public void run() {
        while (enabled) {
            startShooter(SHOOTER_SPEED);
            Timer.delay(0.1);
        }
    }

    /** Start the shooter wheel.
     *
     * @param speed double 0-1 dictates fire speed
     */
    public void startShooter(double speed){
        shooterMotor.set(speed);
        shooterMotor.setPID(P,I,D);
        shooterMotor.changeControlMode(CANTalon.TalonControlMode.Speed);
    }

    /** Stop the shooter wheel.
     */
    public void stopShooter(){
        shooterMotor.set(0);
    }

    /**
     * Starts the thread.
     */
    public void start() {
        enabled = true;
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }

    /**
     * Stops the thread.
     */
    public void terminate() {
        enabled = false;
        t = null;
    }
}
