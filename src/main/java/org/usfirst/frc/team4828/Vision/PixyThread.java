package org.usfirst.frc.team4828.Vision;

import edu.wpi.first.wpilibj.AnalogInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class PixyThread extends Thread {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    private static final int WINDOW_SIZE = 30;
    private static final double SUPPLIED_VOLTAGE = 5.0;
    public volatile Frame lastFrame;
    public volatile Frame currentFrame;
    public double distCm = 0;
    public double distIn = 0;
    private BufferedReader in;
    private Socket soc;
    private Thread t;
    private boolean enabled;
    private String threadName = "pixy thread";
    private AnalogInput sensor;
    private Queue<Double> values;

    /**
     * Create object encapsulating the last frame and ultrasonic data.
     * loops while it's alive
     */
    public PixyThread(int port) {
        sensor = new AnalogInput(port);
        values = new LinkedList<>();
        String[] temp = {"0 1 2 3 4 5 6"};
        currentFrame = new Frame(temp, .5);
        lastFrame = new Frame(temp, .5);
        //start();
    }

    public double getDistIn() {
        return distIn;
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
        return voltage / (SUPPLIED_VOLTAGE / 1024);
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

    @Override
    public void run() {
        while (enabled) {
            try {
                currentFrame = new Frame(in.readLine().split(","), distIn);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (currentFrame.numBlocks() >= 2) {
                lastFrame = currentFrame;
            }
            values.add(sensor.getVoltage());
            while (values.size() > WINDOW_SIZE) {
                values.remove();
            }

            distCm = toCm(medianFilter(values));
            distIn = toIn(medianFilter(values));
            edu.wpi.first.wpilibj.Timer.delay(0.1);
        }
    }

    @Override
    public void start() {
        enabled = true;
        if (t == null) {
            System.out.println("Thread starting: " + threadName);
            boolean scanning = true;
            while (scanning) {
                try {
                    soc = new Socket(HOST, PORT);
                    in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                    scanning = false;
                } catch (Exception e) {
                    System.out.println("Connect failed, waiting and trying again");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            System.out.println("Socket connection established");
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void terminate() {
        try {
            soc.close();
            in.close();
        } catch (IOException e) {

        }
        enabled = false;
        t = null;
    }

    public String toString() {
        return lastFrame.toString();
    }
}