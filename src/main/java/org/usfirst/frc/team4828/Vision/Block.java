package org.usfirst.frc.team4828.Vision;

import java.util.Comparator;

public class Block implements Comparator<Block> {
    static final int X_CENTER = 319 / 2;
    //private static final int Y_CENTER = 199/2;
    double angle;
    private int frame;
    private int block_type;
    private int signature;
    private int x;
    private int y;
    private int width;
    private int height;

    /**
     * Create block object storing the relevant fields.
     *
     * @param data raw string with space separated fields
     */
    Block(String[] data) {
        frame = Integer.parseInt(data[0]);
        block_type = Integer.parseInt(data[1]);
        signature = Integer.parseInt(data[2]);
        x = Integer.parseInt(data[3]);
        y = Integer.parseInt(data[4]);
        width = Integer.parseInt(data[5]);
        height = Integer.parseInt(data[6]);
    }

    /**
     * Create block object storing relevant fields
     *
     * @param frame frame pixy
     * @param block_type  from pixy
     * @param signature from pixy
     * @param x in pixels
     * @param y in pixels
     * @param width of block
     * @param height of block
     */
    Block(int frame, int block_type, int signature, int x, int y, int width, int height) {
        this.frame = frame;
        this.block_type = block_type;
        this.signature = signature;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

     int getFrame() {
        return frame;
    }

     int getBlock_type() {
        return block_type;
    }

     int getSignature() {
        return signature;
    }

     int getWidth() {
        return width;
    }

     int getHeight() {
        return height;
    }

    /**
     *
     * @param pixelConstant conversion constant pixels -> inches
     * @param distance distance to wall in inches from ultrasonic sensor
     * @return angle to detected block
     */
     double computeAngle(double pixelConstant, double distance) {
        double horizontalDistance = (x - X_CENTER) * pixelConstant;
        return Math.toDegrees(Math.atan(horizontalDistance / distance));
    }

     int getX() {
        return x;
    }

     int getY() {
        return y;
    }

     double getAngle() {
        return angle;
    }

    @Override
    public int compare(Block o1, Block o2) {
        if((o1.getHeight() * o1.getWidth()) < (o2.getHeight() * o2.getWidth())){
            return -1;
        }
        else if((o1.getHeight() * o1.getWidth()) > (o2.getHeight() * o2.getWidth())){
            return 1;
        }
        else{
            return 0;
        }
    }
}