package org.usfirst.frc.team4828.Vision;

import edu.wpi.first.wpilibj.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

public class Vision {
    private PixyThread pixy;

    public Vision(int port){
        pixy = new PixyThread(port);
        pixy.start();
        pixy.terminate();
    }

    private void firstMove(){
        double b1Angle = pixy.lastFrame.frameData.get(0).getAngle();
        double b2Angle = pixy.lastFrame.frameData.get(1).getAngle();
        if(pixy.lastFrame.numBlocks() == 2){
            if(b1Angle<0 && b2Angle<0){
                //do stuff
            }
        }
        else{
            //move back ~2ft
            firstMove();
        }
    }

    private void errorCorrection(){
        if(pixy.lastFrame.numBlocks() == 2){
            double b1Angle = pixy.lastFrame.frameData.get(0).getAngle();
            double b2Angle = pixy.lastFrame.frameData.get(1).getAngle();
            int b1x = pixy.lastFrame.frameData.get(0).getX();
            int b2x = pixy.lastFrame.frameData.get(1).getX();
            // if diffAngle is small we are on one side
            // if diffAngle is large we are centered
            //if angle is positive, we are on the right side of the block
            //if angle is negative we are on the left side of the block
        }
    }
}
