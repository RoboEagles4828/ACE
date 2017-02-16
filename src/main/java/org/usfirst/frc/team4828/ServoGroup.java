package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class ServoGroup {
    private static final double STEP_SIZE = 0.01;

    private Servo master;
    private Servo slave;
    private double[] masterRange;
    private double[] slaveRange;

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
    }

    public void raise(double step) {
        master.set(master.get() + step);
        slave.set(slave.get() - step);
    }

    public void raise() {
        master.set(master.get() + STEP_SIZE);
        slave.set(slave.get() - STEP_SIZE);
    }

    public void lower(double step) {
        master.set(master.get() - step);
        slave.set(slave.get() + step);
    }

    public void lower() {
        master.set(master.get() - STEP_SIZE);
        slave.set(slave.get() + STEP_SIZE);
    }


    public void set(double pos) {
        master.set((masterRange[1] - masterRange[0]) * pos + masterRange[0]);
        slave.set((slaveRange[1] - slaveRange[0]) * pos + slaveRange[0]);
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
