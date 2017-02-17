package org.usfirst.frc.team4828.Vision;

import java.util.List;

public class Frame {
    //8.5 in between targets
    private static final double WIDTH_BETWEEN_TARGET = 8.5;
    public List<Block> frameData;

    public Frame(String[] data, double dist) {
        for (String item : data) {
            frameData.add(new Block(item.split(" ")));
        }
        for (Block item : frameData) {
            item.angle = item.computeAngle(getPixelConstant(), dist);
        }
    }

    public double getPixelConstant() {
        return WIDTH_BETWEEN_TARGET / (Math.abs(frameData.get(0).getX() - frameData.get(1).getY()));
    }

    public int numBlocks() {
        return frameData.size();
    }
}
