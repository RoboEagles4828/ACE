package org.usfirst.frc.team4828;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A Thread for finding distance using the MB1200 XL-MaxSonar-EZ0 Sensor.
 * Can be used to find both Inches and Centimeters. Uses a Median Filter.
 * @author Jackie Chen
 */

public class UltraThread extends Thread {

    private static final int WINDOW_SIZE = 5;
    private static final int SUPPLIED_VOLTAGE = 5;
    
    private Thread t;
    private String threadName = "ultrasonic thread";
    AnalogInput sensor;
    double distCm;
    double distIn;
    boolean enabled;
    List<Double> valuesCm;
    List<Double> valuesIn;

    /** UltraThread - Create a new UltraThread with a given port.
     * Port should be an Analog port.
     * @param port the port that the sensor is connected to
     */

    public UltraThread(int port) {
        sensor = new AnalogInput(port);
        valuesCm = new ArrayList<Double>();
        valuesIn = new ArrayList<Double>();
        System.out.println("Thread starting");
    }

    /**
     * Finds the median in a List of values.
     * @param values a List of Doubles
     * @return       the median
     */

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

    /** toCm - Converts a number to Centimeters.
     * @param voltage number to convert
     * @return  converted number
     */

    private double toCm(double voltage) {
        return voltage / (SUPPLIED_VOLTAGE / 1024);
    }

    /** toIn - Converts a number to Inches.
     * @param voltage number to convert
     * @return  converted number
     */

    private double toIn(double voltage) {
        return toCm(voltage) / 2.54;
    }

    /** The contents of the thread
     *  loops while it's alive
     */
    public void run() {
    	while(enabled){
        valuesCm.add(toCm(sensor.getVoltage()));
        valuesCm.add(toIn(sensor.getVoltage()));

        if(valuesCm.size() > WINDOW_SIZE) {
            valuesCm.remove(0);
        }
        if(valuesIn.size() > WINDOW_SIZE) {
            valuesIn.remove(0);
        }

        distCm = medianFilter(valuesCm);
        distIn = medianFilter(valuesIn);
        Timer.delay(.05);
    	}
    }
    
    /** Starts the thread
     *  
     */
    public void start () {
    	enabled = true;
        if (t == null) {
           t = new Thread (this, threadName);
           t.start ();
        }
     }
    
    /** Stops the thread
     * 
     */
    public void terminate(){
    	enabled = false;
    	t = null;
    }
}
