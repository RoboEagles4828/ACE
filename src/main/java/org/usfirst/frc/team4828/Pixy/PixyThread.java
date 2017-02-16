package org.usfirst.frc.team4828.Pixy;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class PixyThread extends Thread {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    private static final int WINDOW_SIZE = 30;
    private static final double SUPPLIED_VOLTAGE = 5.0;
    public frame lastFrame;
    public double distCm = 0;
    public volatile double distIn = 0;
    private BufferedReader in;
    private Socket soc;
    private Thread t;
    private boolean enabled;
    private String threadName = "pixy thread";
    private AnalogInput sensor;
    private Queue<Double> values;

    /** The contents of the thread
     * loops while it's alive
     */

    public PixyThread(int port) {
        sensor = new AnalogInput(port);
        values = new LinkedList<>();
        System.out.println("Thread starting: " + threadName);
        boolean scanning=true;
        while(scanning)
        {
            try
            {
                soc = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                scanning=false;
            }
            catch(Exception e)
            {
                System.out.println("Connect failed, waiting and trying again");
                try
                {
                    Thread.sleep(1000);
                }
                catch(InterruptedException ie){
                    ie.printStackTrace();
                }
            }
        }
        System.out.println("Socket connection established");
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
                lastFrame = new frame(in.readLine().split(","));
            } catch (Exception e) {
                e.printStackTrace();
            }
            values.add(sensor.getVoltage());

            while (values.size() > WINDOW_SIZE) {
                values.remove();
            }

            distCm = toCm(medianFilter(values));
            distIn = toIn(medianFilter(values));
            Timer.delay(0.1);
        }
    }

    @Override
    public void start(){
        enabled = true;
        if(t==null){
            t = new Thread(this, threadName);
            t.start();
        }
    }

    public void terminate(){
        enabled = false;
        t = null;
    }

    public class frame{
        //8.5 in between targets
        private static final double WIDTH_BETWEEN_TARGET = 8.5;
        public List<block> frameData;

        public frame(String[] data){
            for (String item : data){
                frameData.add(new block(item.split(" ")));
            }
        }

        public double getPixelConstant(){
            return WIDTH_BETWEEN_TARGET/(Math.abs(frameData.get(0).getX() - frameData.get(1).getY()));
        }

        public int numBlocks(){return frameData.size();}

        public class block {
            private static final int X_CENTER = 319/2;
            public double angle;
            //private static final int Y_CENTER = 199/2;
            private int frame;
            private int signature;
            private int x;
            private int y;
            private int width;
            private int height;

            public block(String[] data){
                frame = Integer.parseInt(data[0]);
                signature = Integer.parseInt(data[1]);
                x = Integer.parseInt(data[2]);
                y = Integer.parseInt(data[3]);
                width = Integer.parseInt(data[4]);
                height = Integer.parseInt(data[5]);
                angle = getAngle();
            }

            public double getAngle(){
                double constant = getPixelConstant();
                double horizontalDistance = (x - X_CENTER) * constant;
                return Math.toDegrees(Math.atan(horizontalDistance/ distIn));
            }

            public int getX(){return x;}
            public int getY(){return y;}
        }
    }
}
