package org.usfirst.frc.team4828;

import com.ctre.CANTalon;

public class Shooter {
    private CANTalon shooterMotor;
    public ServoGroup servos;

    public void Shooter(int motorPort, int masterPort, int slavePort){
        shooterMotor = new CANTalon(motorPort);
        servos = new ServoGroup(masterPort, slavePort);
    }

    /** Start the shooter wheel.
     *
     * @param speed double 0-1 dictates fire speed
     */
    public void start(double speed){
        shooterMotor.set(speed);
    }

    /** Stop the shooter wheel.
     *
     */
    public void stop(){
        shooterMotor.set(0);
    }
}
