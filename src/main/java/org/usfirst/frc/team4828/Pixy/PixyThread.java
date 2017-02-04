package org.usfirst.frc.team4828.Pixy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class PixyThread extends Thread {
    public static final String HOST = "pixyco.local";
    public static final int PORT = 5800;
    public static int frame = 0;
    public static int type = 0;
    public static int sig = 0;
    public static int x = 0;
    public static int y = 0;
    public static int width = 0;
    public static int height = 0;

    @Override
    public void run() {
        while (true) {
            try {
                Socket soc = new Socket(HOST, PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                while (true) {
                    String visionData = in.readLine();
                    System.out.println(visionData);
                    frame = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    type = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    sig = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    x = Integer.parseInt(visionData.substring(0, vigsionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    y = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    width = Integer.parseInt(visionData.substring(0, visionData.indexOf(' ')));
                    visionData = visionData.substring(visionData.indexOf(' '));
                    height = Integer.parseInt(visionData);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
