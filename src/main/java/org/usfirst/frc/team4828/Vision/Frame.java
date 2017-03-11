package org.usfirst.frc.team4828.Vision;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Frame {
    //8.25 in between targets
    private static final double WIDTH_BETWEEN_TARGET = 8.25;
    private List<Block> frameData;

    /**
     * Create frame holding a list of all blocks found.
     *
     * @param data raw string  array of comma separated blocks
     * @param dist current ultrasonic sensor reading
     */
    Frame(String[] data, double dist) {
        frameData = new ArrayList<>();
        for (String i : data) {
            frameData.add(new Block(i.split(" ")));
        }
        filter();
        for (Block i : frameData) {
            i.angle = i.computeAngle(getPixelConstant(), dist);
        }
    }

    /**
     * @return conversion constant pixels -> inches
     */
    private double getPixelConstant() {
        if (numBlocks() >= 2) {
            return WIDTH_BETWEEN_TARGET / (Math.abs(frameData.get(0).getX() - frameData.get(1).getX()));
        }
        return -1;
    }

    double getRealDistance(int pixels) {
        return pixels * getPixelConstant();
    }

    public List<Block> getFrameData() {
        return frameData;
    }

    int numBlocks() {
        return frameData.size();
    }

    /**
     * tries to filter frameData down to 2 blocks using various methods
     */
    private void filter() {
        //filter by x coordinate
        if (numBlocks() > 2) {
            for (int i = 0; i < frameData.size(); i++) {
                for (int j = 0; j < frameData.size(); j++) {
                    if (i != j && (Math.abs(frameData.get(i).getX() - frameData.get(j).getX()) < 10)) {
                        frameData.add(new Block(
                                frameData.get(i).getFrame(),
                                frameData.get(i).getBlock_type(),
                                frameData.get(i).getSignature(),
                                (frameData.get(i).getX() + frameData.get(j).getX()) / 2,
                                (frameData.get(i).getY() + frameData.get(j).getY()) / 2,
                                (frameData.get(i).getWidth() + frameData.get(j).getWidth()) / 2,
                                frameData.get(i).getHeight() + frameData.get(j).getHeight() / 2));
                        frameData.remove(i);
                        frameData.remove(j);
                    }
                }
            }
        }
        //filter by dimensions, accuracy decreases with distance to target
        if (numBlocks() > 2) {
            for (int i = 0; i < frameData.size(); i++) {
                if (frameData.get(i).getHeight() < frameData.get(i).getWidth()) {
                    frameData.remove(i);
                }
            }
        }
        //nuclear option, remove all but two largest blocks
        if (numBlocks() > 2) {
            frameData.sort(Comparator.comparing(Block::getArea));
            for (int i = 0; i < frameData.size() - 2; i++) {
                frameData.remove(i);
            }
        }
    }

    @Override
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
