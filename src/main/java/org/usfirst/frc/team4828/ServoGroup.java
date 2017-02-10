package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.Servo;

public class ServoGroup {
    private static final double STEP_SIZE = 0.05;

    private Servo master;
    private Servo slave;
    private double masterOffset = 0.0;
    private double slaveOffset = 0.0;
    private int masterDirection = 1;
    private int slaveDirection = 1;

    ServoGroup(int masterPort, int slavePort) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
    }

    ServoGroup(int masterPort, int slavePort, double trainedMasterOffset, double trainedSlaveOffset) {
        master = new Servo(masterPort);
        slave = new Servo(slavePort);
        masterOffset = trainedMasterOffset;
        slaveOffset = trainedSlaveOffset;
    }

    public void raise(double distance) {
        master.set((master.get() + distance + masterOffset) * masterDirection);
        slave.set((slave.get() - distance + slaveOffset) * slaveDirection);
    }

    public void raise() {
        master.set((master.get() + STEP_SIZE + masterOffset) * masterDirection);
        slave.set((slave.get() - STEP_SIZE + slaveOffset) * slaveDirection);
    }

    public void lower(double distance) {
        master.set((master.get() - distance + masterOffset) * masterDirection);
        slave.set((slave.get() + distance + slaveOffset) * slaveDirection);
    }

    public void lower() {
        master.set((master.get() - STEP_SIZE + masterOffset) * masterDirection);
        slave.set((slave.get() + STEP_SIZE + slaveOffset) * slaveDirection);
    }

    public void calibratePosition(boolean selection, double offset) {
        if (selection) {
            masterOffset = offset;
        } else {
            slaveOffset = offset;
        }
    }

    public void calibrateDirection(boolean selection, int direction) {
        if (selection) {
            masterDirection = direction;
        } else {
            slaveDirection = direction;
        }
    }
}
