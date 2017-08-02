package org.usfirst.frc.team4828;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

/**
 * Testing class dedicated to the DriveTrain Class.
 *
 * @see DriveTrain
 */
public class DriveTrainTest {
    private static final int TEST_CASES = 420;

    /**
     * Tests the Normalization function in DriveTrain Class
     * Test an array of 4 random numbers multiple times. Should be less than 1.
     *
     * @see DriveTrain
     */
    @Test
    public void testNormalize() {
        Random rand = new Random();
        for (int j = 1; j < TEST_CASES + 1; j++) {
            double[] testData = new double[4];
            for (int i = 0; i < 4; i++) {
                testData[i] = rand.nextDouble() * 2;
            }
            DriveTrain.normalize(testData);
            for (int i = 0; i < 4; i++) {
                if (testData[i] > 1.0) {
                    System.out.println("Normalization failed: " + testData[i]
                            + " > 1.0 (" + j + " of " + TEST_CASES + ")");
                    Assert.fail();
                }
            }
        }
        System.out.println("Finished " + TEST_CASES + " Normalization Tests");
    }

//    @Test
//    public void closestAngle(){
//        double a = 45;
//        for(double b = 0; b < 360; b++){
//            System.out.println(a + ", " + b + ": " + DriveTrain.closestAngle(a, b));
//        }
//    }
}
