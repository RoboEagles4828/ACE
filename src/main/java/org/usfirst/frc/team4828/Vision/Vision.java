package org.usfirst.frc.team4828.Vision;

import com.kauailabs.navx.frc.AHRS;

public class Vision {
    private PixyThread pixy;

    /**
     * Create an object encapsulating the vision and ultrasonic thread.
     *
     * @param port port of the ultrasonic sensor
     */
    public Vision(int port) {
        pixy = new PixyThread(port);
    }

    /**
     *
     * @return horizontal offset in inches of lift or vision target, if only one block is detected
     */
    public double findHorizontalOffset() {
        if (pixy.lastFrame.numBlocks() == 2) {
            return pixy.lastFrame.getRealDistance(((pixy.lastFrame.getFrameData().get(0).getX()
                    + pixy.lastFrame.getFrameData().get(1).getX()) / 2) - Block.X_CENTER);
        }
        //if only one vision target is detected
        else if(pixy.lastFrame.numBlocks() == 1){
            return pixy.lastFrame.getRealDistance(pixy.lastFrame.getFrameData().get(0).getX() - Block.X_CENTER);
        }
        return 0;
    }

    public void terminate() {
        pixy.terminate();
    }

    public void start() {
        pixy.start();
    }

    public String toString() {
        return pixy.toString();
    }
}
