package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * Created by cheez on 1/31/2017.
 */
public class UltraSensor extends AnalogInput {

    AnalogInput sensor;

    public UltraSensor(int port) {
        super(port);
    }

    public double getCm() {
        return 0.0;
    }

    public double getIn() {
        return 0.0;
    }
}
