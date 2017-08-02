package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Ultrasonic implements Runnable{
    private static final int WINDOW_SIZE = 50;
    private static final double SUPPLIED_VOLTAGE = 5.0;
    private volatile double distIn = 0;
    private AnalogInput sensor;
    private Queue<Double> values;
    private boolean enabled;

    public Ultrasonic(int port){
        System.out.println("Constructing ultrasonic thread");
        sensor = new AnalogInput(port);
        values = new LinkedList<>();
        enabled = false;
    }

    /**
     * Finds the median in a List of values.
     *
     * @param values a List of Doubles
     * @return the median
     */
    private double medianFilter(Queue<Double> values) {
        int half = values.size() / 2;
        if (values.size() == 0) {
            return 0.0;
        }
        List<Double> temp = new ArrayList<>(values);
        Collections.sort(temp);
        if (values.size() % 2 == 1) {
            return temp.get(half);
        } else {
            return (temp.get(half - 1) + temp.get(half)) / 2;
        }
    }

    /**
     * Converts a number to Centimeters.
     *
     * @param voltage number to convert
     * @return converted number
     */
    private double toCm(double voltage) {
        return voltage / (SUPPLIED_VOLTAGE / 512);
    }

    /**
     * Converts from Centimeters to Inches.
     *
     * @param voltage number to convert
     * @return converted number
     */
    private double toIn(double voltage) {
        return toCm(voltage) / 2.54;
    }

    /**
     * @return distance from robot to the end of the peg inches
     */
    public double getDist() {
        return distIn;
    }



    @Override
    public void run() {
        enabled = true;
        while(enabled) {
            values.add(sensor.getVoltage());
            while (values.size() > WINDOW_SIZE) {
                values.remove();
            }
            distIn = toIn(medianFilter(values));
            Timer.delay(.01);
        }
    }

    public void terminate(){
        sensor.free();
        enabled = false;
        System.out.println("Killing ultrasonic thread");
    }
}
