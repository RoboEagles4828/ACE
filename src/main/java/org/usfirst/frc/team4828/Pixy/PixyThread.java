package org.usfirst.frc.team4828.Pixy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

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
    private String visionData;
    private Thread t;
    private boolean enabled;
    private String threadName = "pixy thread";


    public PixyThread() {
        try {
            soc = new Socket(HOST, PORT);
            in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (enabled) {
            try {
                visionData = in.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(visionData);
            frame = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            type = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            sig = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            x = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            y = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            width = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
            visionData = visionData.substring(visionData.indexOf(' '));
            height = Integer.parseInt(visionData);
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
