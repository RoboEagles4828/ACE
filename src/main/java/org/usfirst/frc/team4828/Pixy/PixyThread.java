package org.usfirst.frc.team4828.Pixy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;
import org.usfirst.frc.team4828.UltraThread;

public class PixyThread extends Thread {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    private BufferedReader in;
    private Socket soc;
    private Thread t;
    private boolean enabled;
    private String threadName = "pixy thread";
    public frame lastFrame;
    private UltraThread us;

    private class frame{
        //8.5 in between targets
        private static final double WIDTH_BETWEEN_TARGET = 8.5;
        public List<block> frameData;

        public class block {
            private static final int X_CENTER = 319/2;
            private static final int Y_CENTER = 199/2;
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
            }

            public double getAngle(){
                double constant = getPixelConstant();
                double horizontalDistance = (x - X_CENTER) * constant;
                double lateralDistance = us.distIn();
                return Math.toDegrees(Math.atan(horizontalDistance/ lateralDistance));
            }

            public int getX(){return x}
            public int getY(){return y}
        }

        public double getPixelConstant(){
            return WIDTH_BETWEEN_TARGET/(Math.abs(frameData.get(0).getX() - frameData.get(1).getY()));
        }

        public frame(String[] data){
            for (String item : data){
                frameData.add(new block(item.split(" "));
            }
        }

        public int numBlocks(){return frameData.size()}
    }

    public PixyThread() {
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
        us = new UltraThread(0);
        us.start();
    }

    @Override
    public void run() {
        while (enabled) {
            try {
                lastFrame = new frame(in.readLine().split(","));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}
