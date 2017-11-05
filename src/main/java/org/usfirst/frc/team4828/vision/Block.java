package org.usfirst.frc.team4828.vision;

public class Block {
    static final int X_CENTER = 319 / 2;
    //private static final int Y_CENTER = 199/2;
    double angle;
    private int frame;
    private int blocktype;
    private int signature;
    private int xcoord;
    private int ycoord;
    private int width;
    private int height;

    /**
     * Create block object storing the relevant fields.
     *
     * @param data raw string with space separated fields
     */
    Block(String[] data) {
        frame = Integer.parseInt(data[0]);
        blocktype = Integer.parseInt(data[1]);
        signature = Integer.parseInt(data[2]);
        xcoord = Integer.parseInt(data[3]);
        ycoord = Integer.parseInt(data[4]);
        width = Integer.parseInt(data[5]);
        height = Integer.parseInt(data[6]);
    }

    /**
     * Create block object storing relevant fields
     *
     * @param frame      frame pixy
     * @param blocktype from pixy
     * @param signature  from pixy
     * @param xcoord     in pixels
     * @param ycoord     in pixels
     * @param width      of block
     * @param height     of block
     */
    Block(int frame, int blocktype, int signature, int xcoord, int ycoord, int width, int height) {
        this.frame = frame;
        this.blocktype = blocktype;
        this.signature = signature;
        this.xcoord = xcoord;
        this.ycoord = ycoord;
        this.width = width;
        this.height = height;
    }

    int getArea() {
        return width * height;
    }

    int getFrame() {
        return frame;
    }

    int getBlock_type() {
        return blocktype;
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

    int getX() {
        return xcoord;
    }

    int getYcoord() {
        return ycoord;
    }

    double getAngle() {
        return angle;
    }

    /**
     * @param pixelConstant conversion constant pixels -> inches
     * @param distance      distance to wall in inches from ultrasonic sensor
     * @return angle to detected block
     */
    double computeAngle(double pixelConstant, double distance) {
        double horizontalDistance = (xcoord - X_CENTER) * pixelConstant;
        return Math.toDegrees(Math.atan(horizontalDistance / distance));
    }
}