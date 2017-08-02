package org.usfirst.frc.team4828.vision;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.usfirst.frc.team4828.Ultrasonic;

public class Pixy implements Runnable {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    private static final int PIXY_SIDE = 1;  //-1 for right, 1 for left
    private boolean enabled;
    private boolean connected;
    private boolean blocksDetected;
    private volatile Frame currentFrame;
    private volatile Frame lastFrame;
    private BufferedReader in;
    private Socket soc;
    private Ultrasonic us;

    /**
     * loops while it's alive
     * Create object encapsulating the last frame and ultrasonic data.
     */
    public Pixy(Ultrasonic ultra) {
        System.out.println("Constructing pixy thread");
        String[] temp = {"0 1 2 3 4 5 6"};
        currentFrame = new Frame(temp, .5);
        lastFrame = new Frame(temp, .5);
        enabled = false;
        blocksDetected = false;
        connected = false;
        us = ultra;
    }

    public boolean blocksDetected() {
        return blocksDetected;
    }

    public double horizontalOffset() {
        //if two blocks are detected return position of peg
        if (currentFrame.numBlocks() == 2) {
            return lastFrame.getRealDistance(((lastFrame.getFrameData().get(0).getX()
                    + lastFrame.getFrameData().get(1).getX()) / 2) - Block.X_CENTER);
        }
        //if only one vision target is detected guess position of peg
        else if (currentFrame.numBlocks() == 1) {
            blocksDetected = false;
            double pegPos = ((currentFrame.getFrameData().get(0).getX() - Block.X_CENTER) > 0)
                    ? 4.125 : -4.125;
            return currentFrame.getRealDistance(currentFrame.getFrameData().get(0).getX()
                    - Block.X_CENTER) + pegPos;
        }
        //if no vision targets are detected
        blocksDetected = false;
        return 4828;
    }

    @Override
    public void run() {
        System.out.println("Searching for socket connection...");
        enabled = true;
        while (enabled) {
            try {
                soc = new Socket(HOST, PORT);
                in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                System.out.print("Socket connection established on ip: " + soc.getInetAddress());
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
        connected = true;
        while (enabled) {
            try {
                currentFrame = new Frame(in.readLine().split(","), us.getDist());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (currentFrame.numBlocks() >= 2
                    || (lastFrame == null && currentFrame.numBlocks() == 1)) {
                blocksDetected = true;
                lastFrame = currentFrame;
            }
            edu.wpi.first.wpilibj.Timer.delay(0.01);
        }
    }

    public void terminate() {
        System.out.println("DISABLING THREAD");
        if (enabled && connected) {
            try {
                in.close();
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blocksDetected = false;
        connected = false;
        enabled = false;
    }

    public int getWidth() {
        return lastFrame.getFrameData().get(0).getWidth();
    }

    public Frame getLastFrame() {
        if (lastFrame != null) {
            return lastFrame;
        }
        return currentFrame;
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