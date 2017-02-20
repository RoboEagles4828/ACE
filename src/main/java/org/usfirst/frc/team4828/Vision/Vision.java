package org.usfirst.frc.team4828.Vision;

public class Vision {
    public PixyThread pixy;

    /**
     * Create an object encapsulating the vision and ultrasonic thread.
     *
     * @param port port of the ultrasonic sensor
     */
    public Vision(int port) {
        pixy = new PixyThread(port);
    }

    /**
     * @return horizontal offset in inches of lift or vision target from camera, if only one block is detected
     */
    public double horizontalOffset() {
        if (pixy.lastFrame.numBlocks() == 2) {
            return pixy.lastFrame.getRealDistance(((pixy.lastFrame.getFrameData().get(0).getX()
                    + pixy.lastFrame.getFrameData().get(1).getX()) / 2) - Block.X_CENTER);
        }
        //if only one vision target is detected
        else if (pixy.lastFrame.numBlocks() == 1) {
            return pixy.lastFrame.getRealDistance(pixy.lastFrame.getFrameData().get(0).getX() - Block.X_CENTER);
        }
        //if no vision targets are detected
        return 1000;
    }

    /**
     * @return distance from robot to the end of the peg inches
     */
    public double transverseOffset() {
        return pixy.getDistIn() - 10.5;
    }

    public void terminate() {
        pixy.terminate();
    }

    public void start() {
        pixy.start();
    }

    @Override
    public String toString() {
        return pixy.toString();
    }
}
