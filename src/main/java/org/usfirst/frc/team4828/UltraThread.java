package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.AnalogInput;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jackie on 1/31/2017.
 */
public class UltraThread extends Thread{

    private static final int WINDOW_SIZE = 5;

    AnalogInput sensor;
    double dist;
    List<Double> values;

    public UltraThread(int port) {
        sensor = new AnalogInput(port);
    }

    private double medianFilter(List<Double> values) {
        int half = WINDOW_SIZE / 2;
        double med;
        if (values.size() < WINDOW_SIZE) {
            return 0.0;
        }

        Collections.sort(values);

        if (WINDOW_SIZE % 2 == 1) {
            return values.get(half);
        } else {
            return (values.get(half - 1) + values.get(half)) / 2;
        }
    }

    private double toCm(double v) {
        return 0.0;
    }

    private double toIn(double v) {
        return 0.0;
    }

    public void run() {
        values.add(toCm(sensor.getVoltage())); // Change to toIn for inches

        if(values.size() > WINDOW_SIZE) {
            values.remove(0);
        }

        dist = medianFilter(values);
    }
}
