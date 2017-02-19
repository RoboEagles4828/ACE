package org.usfirst.frc.team4828.Vision;

public class Block {
    public static final int X_CENTER = 319 / 2;
    //private static final int Y_CENTER = 199/2;
    public double angle;
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
    public Block(String[] data) {
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
     * @param frame
     * @param block_type
     * @param signature
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public Block(int frame, int block_type, int signature, int x, int y, int width, int height) {
        this.frame = frame;
        this.block_type = block_type;
        this.signature = signature;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getFrame() {
        return frame;
    }

    public int getBlock_type() {
        return block_type;
    }

    public int getSignature() {
        return signature;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /**
     *
     * @param pixelConstant conversion constant pixels -> inches
     * @param distance distance to wall in inches from ultrasonic sensor
     * @return angle to detected block
     */
    public double computeAngle(double pixelConstant, double distance) {
        double horizontalDistance = (x - X_CENTER) * pixelConstant;
        return Math.toDegrees(Math.atan(horizontalDistance / distance));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }
}