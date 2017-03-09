package org.usfirst.frc.team4828.Vision;

import org.usfirst.frc.team4828.Ultrasonic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Pixy implements Runnable {
    private static final String HOST = "pixyco.local";
    private static final int PORT = 5800;
    private static final double PIXY_OFFSET = 0;
    private boolean enabled, blocksDetected;
    private volatile Frame currentFrame, lastFrame;
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
        us = ultra;
    }

    public boolean blocksDetected() {
        return blocksDetected;
    }

    public double horizontalOffset() {
        if (lastFrame.numBlocks() == 2) {
            return lastFrame.getRealDistance(((lastFrame.getFrameData().get(0).getX()
                    + lastFrame.getFrameData().get(1).getX()) / 2) - Block.X_CENTER) + PIXY_OFFSET;
        }
        //if only one vision target is detected
        else if (lastFrame.numBlocks() == 1) {
            return lastFrame.getRealDistance(lastFrame.getFrameData().get(0).getX() - Block.X_CENTER) + PIXY_OFFSET;
        }
        //if no vision targets are detected
        blocksDetected = false;
        return 4828;
    }

    @Override
    public void run() {
        System.out.println("Searching for socket connection...");
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
                currentFrame = new Frame(in.readLine().split(","), us.getDist());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (currentFrame.numBlocks() >= 2 || (lastFrame == null && currentFrame.numBlocks() == 1)) {
                blocksDetected = true;
                lastFrame = currentFrame;
            }
            edu.wpi.first.wpilibj.Timer.delay(0.1);
        }
    }

    public void terminate() {
        System.out.println("DISABLING THREAD");
        if (enabled) {
            try {
                in.close();
                soc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        blocksDetected = false;
        enabled = false;
    }

    Frame getLastFrame() {
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