package org.usfirst.frc.team4828.Vision;

public class Block {
    private static final int X_CENTER = 319 / 2;
    //private static final int Y_CENTER = 199/2;
    public double angle;
    private int frame;
    private int signature;
    private int x;
    private int y;
    private int width;
    private int height;

    public Block(String[] data) {
        frame = Integer.parseInt(data[0]);
        signature = Integer.parseInt(data[1]);
        x = Integer.parseInt(data[2]);
        y = Integer.parseInt(data[3]);
        width = Integer.parseInt(data[4]);
        height = Integer.parseInt(data[5]);
    }

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