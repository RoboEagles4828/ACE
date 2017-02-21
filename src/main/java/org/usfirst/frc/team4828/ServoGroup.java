package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class ServoGroup {
    private static final double STEP_SIZE = 0.01;

    private Servo master;
    private Servo slave;
    private double[] masterRange;
    private double[] slaveRange;
    private double position;

    /**
     * Create servo group object using the given ports.
     *
     * @param masterPort port of the master servo
     * @param slavePort port of the slave servo
     */
    ServoGroup(int masterPort, int slavePort) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
        masterRange = new double[2];
        slaveRange = new double[2];
    }

    /**
     * Create servo group object using the given ports and range values.
     *
     * @param masterPort port of the master servo
     * @param slavePort port of the slave servo
     * @param masterMin minimum position of the master servo
     * @param masterMax maximum position of the master servo
     * @param slaveMin minimum position of the slave servo
     * @param slaveMax maximum position of the slave servo
     */
    ServoGroup(int masterPort, int slavePort, double masterMin, double masterMax, double slaveMin, double slaveMax) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
        masterRange = new double[2];
        slaveRange = new double[2];
        calibrate(1, masterMin, masterMax);
        calibrate(2, slaveMin, slaveMax);
        position = 0;
    }

    /**
     * Raise the servos by a given step size.
     *
     * @param step double step size
     */
    public void raise(double step) {
        set(position + step);
    }

    /**
     * Raise the servos by the default step size.
     */
    public void raise() {
        raise(STEP_SIZE);
    }

    /**
     * Lower the servos by a given step size.
     *
     * @param step double step size
     */
    public void lower(double step) {
        set(position - step);
    }

    /**
     * Lower the servos by the default step size.
     */
    public void lower() {
        lower(STEP_SIZE);
    }

    /**
     * Set the servos' position.
     *
     * @param pos double 0-1 relative position within range
     */
    public void set(double pos) {
        position = pos;
        master.set((masterRange[1] - masterRange[0]) * position + masterRange[0]);
        slave.set((slaveRange[1] - slaveRange[0]) * position + slaveRange[0]);
    }

    /**
     * Get the current position.
     *
     * @return double position
     */
    public double get() {
        return position;
    }

    /**
     * Configure the servos' range of motion.
     *
     * @param sel 1 = master, 2 = slave
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

    /**
     * Get a String stating the position of each servo.
     *
     * @return String
     */
    public String toString() {
        return "Master: " + master.get() + " Slave: " + slave.get();
    }

}
