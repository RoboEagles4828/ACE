package org.usfirst.frc.team4828.Pixy;

import edu.wpi.first.wpilibj.Timer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class PixyThread extends Thread {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    public static int frame = 0;
    public static int type = 0;
    public static int sig = 0;
    public static int x = 0;
    public static int y = 0;
    public static int width = 0;
    public static int height = 0;
    private BufferedReader in;
    private Socket soc;
    private String[] visionData;
    private Thread t;
    private boolean enabled;
    private String threadName = "pixy thread";


    public PixyThread() {
        System.out.println("Thread starting: " + threadName);
        try {
            soc = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Socket connection established");
    }

    @Override
    public void run() {
        while (enabled) {
            try {
                visionData = in.readLine().split(" ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(Arrays.toString(visionData));
            frame = Integer.parseInt(visionData[0]);
            type = Integer.parseInt(visionData[1]);
            sig = Integer.parseInt(visionData[2]);
            x = Integer.parseInt(visionData[3]);
            y = Integer.parseInt(visionData[4]);
            width = Integer.parseInt(visionData[5]);
            height = Integer.parseInt(visionData[6]);
        }
    }

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
