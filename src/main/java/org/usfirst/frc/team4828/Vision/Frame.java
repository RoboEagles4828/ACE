package org.usfirst.frc.team4828.Vision;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    //8.5 in between targets
    private static final double WIDTH_BETWEEN_TARGET = 8.5;
    private List<Block> frameData;

    /**
     * Create frame holding a list of all blocks found.
     * @param data raw string  array of comma separated blocks
     * @param dist current ultrasonic sensor reading
     */
    public Frame(String[] data, double dist) {
        frameData = new ArrayList<>();
        for (String i : data) {
            frameData.add(new Block(i.split(" ")));
        }
        filter();
        for (Block i : frameData) {
            i.angle = i.computeAngle(getPixelConstant(), dist);
        }
    }

    public double getPixelConstant() {
        if (numBlocks() >= 2) {
            return WIDTH_BETWEEN_TARGET / (Math.abs(frameData.get(0).getX() - frameData.get(1).getX()));
        }
        return -1;
    }

    public List<Block> getFrameData() {
        return frameData;
    }

    public int numBlocks() {
        return frameData.size();
    }

    private void filter(){;
        for (int i=0; i<frameData.size(); i++){
            for (int o=0; o<frameData.size(); o++){
                if (i != o && (Math.abs(frameData.get(i).getX() - frameData.get(o).getX()) < 10)){
                    frameData.add(new Block(
                            frameData.get(i).getFrame(),
                            frameData.get(i).getBlock_type(),
                            frameData.get(i).getSignature(),
                            (frameData.get(i).getX() + frameData.get(o).getX()) / 2,
                            (frameData.get(i).getY() + frameData.get(o).getY()) / 2,
                            (frameData.get(i).getWidth() + frameData.get(o).getWidth()) / 2,
                            frameData.get(i).getHeight() + frameData.get(o).getHeight() / 2));
                    frameData.remove(i);
                    frameData.remove(o);
                }
            }
        }
    }

    public String toString() {
        if (numBlocks() == 0) {
            return "NO BLOCKS DETECTED";
        }
        String temp = "";
        for (Block i : frameData) {
            temp += " " + i.getX() + " ";
        }
        return "Detected " + frameData.size() + " blocks at: " + temp;
    }
}
