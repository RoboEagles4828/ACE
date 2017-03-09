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
    private static final double pixyOffset = 0;
    volatile Frame lastFrame;
    private  boolean enabled;
    private volatile Frame currentFrame;
    private double distCm = 0;
    private volatile double distIn = 0;
    private BufferedReader in;
    private Socket soc;
    private AnalogInput sensor;
    private Queue<Double> values;
    private boolean blocksDetected = false;

    /**
     * loops while it's alive
     * Create object encapsulating the last frame and ultrasonic data.
     */
    public PixyThread(int port) {
        System.out.println("constructing pixythread");
        sensor = new AnalogInput(port);
        values = new LinkedList<>();
        String[] temp = {"0 1 2 3 4 5 6"};
        currentFrame = new Frame(temp, .5);
        lastFrame = new Frame(temp, .5);
        enabled = false;
    }

    public boolean isBlocksDetected() {
        return blocksDetected;
    }

    public double horizontalOffset() {
        if (lastFrame.numBlocks() == 2) {
            return lastFrame.getRealDistance(((lastFrame.getFrameData().get(0).getX()
                    + lastFrame.getFrameData().get(1).getX()) / 2) - Block.X_CENTER) + pixyOffset;
        }
        //if only one vision target is detected
        else if (lastFrame.numBlocks() == 1) {
            return lastFrame.getRealDistance(lastFrame.getFrameData().get(0).getX() - Block.X_CENTER) + pixyOffset;
        }
        //if no vision targets are detected
        blocksDetected = false;
        return 1000;
    }

    /**
     * @return distance from robot to the end of the peg inches
     */
    public double distanceFromLift() {
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
        System.out.println("STARTING RUN");
        while (enabled) {
            try {
                soc = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                break;
            } catch (IOException e) {
                System.out.println("Connect failed, waiting and trying again");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        }
        System.out.println("Socket connection established");
        int i = 0;
        while (enabled) {
            System.out.println("sampled " + i);
            try {
                currentFrame = new Frame(in.readLine().split(","), distIn);
                if (currentFrame.numBlocks() >= 2 || (lastFrame == null && currentFrame.numBlocks() == 1)) {
                    blocksDetected = true;
                    lastFrame = currentFrame;
                }

                values.add(sensor.getVoltage());
                while (values.size() > WINDOW_SIZE) {
                    values.remove();
                }
                i++;
                distCm = toCm(medianFilter(values));
                distIn = toIn(medianFilter(values));
                edu.wpi.first.wpilibj.Timer.delay(0.1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void start(){
        enabled = true;
        System.out.println("STARTING THREAD");
        super.start();
    }

    public void terminate() {
        System.out.println("DISABLING THREAD");
        try {
            in.close();
            soc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        blocksDetected = false;
        enabled = false;
    }

    @Override
    public String toString() {
        if (lastFrame != null) {
            return lastFrame.toString();
        } else {
            return currentFrame.toString();
        }
    }
}