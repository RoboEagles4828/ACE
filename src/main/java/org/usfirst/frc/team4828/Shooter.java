package org.usfirst.frc.team4828;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Servo;

public class Shooter {
    private CANTalon shooterMotor;
    private Servo s1, s2;

    public void Shooter(int motorPort, int masterPort, int slavePort){
        shooterMotor = new CANTalon(motorPort);
        s1 = new Servo(masterPort);
        s2 = new Servo(slavePort);
    }

    /** Change the angle of the shooter's track.
     *
     * @param a
     */
    public void setAngle(double a){
        s1.set(a);
        s2.set(a);
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
