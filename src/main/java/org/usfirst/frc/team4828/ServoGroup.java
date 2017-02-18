package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class ServoGroup {
    private static final double STEP_SIZE = 0.01;

    private Servo master;
    private Servo slave;
    private double[] masterRange;
    private double[] slaveRange;
    private double position;

    ServoGroup(int masterPort, int slavePort) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
        masterRange = new double[2];
        slaveRange = new double[2];
    }

    ServoGroup(int masterPort, int slavePort, double masterMin, double masterMax, double slaveMin, double slaveMax) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
        masterRange = new double[2];
        slaveRange = new double[2];
        calibrate(1, masterMin, masterMax);
        calibrate(2, slaveMin, slaveMax);
        position = 0;
    }

    public void raise(double step) {
        set(position + step);
    }

    public void raise() {
        raise(STEP_SIZE);
    }

    public void lower(double step) {
        set(position - step);
    }

    public void lower() {
        lower(STEP_SIZE);
    }


    public void set(double pos) {
        position = pos;
        master.set((masterRange[1] - masterRange[0]) * position + masterRange[0]);
        slave.set((slaveRange[1] - slaveRange[0]) * position + slaveRange[0]);
    }

    /**
     * Configure the servos' range of motion.
     *
     * @param sel 0 = master 1 = slave
     * @param min double lowest angle
     * @param max double highest angle
     */
    public void calibrate(int sel, double min, double max) {
        if (sel == 1) {
            masterRange[0] = min;
            masterRange[1] = max;
        } else {
            slaveRange[0] = min;
            slaveRange[1] = max;
        }
    }

    public String toString() {
        return "Master: " + master.get() + " Slave: " + slave.get();
    }

}
