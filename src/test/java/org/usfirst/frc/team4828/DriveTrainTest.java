package org.usfirst.frc.team4828;

import org.junit.Assert;
import org.junit.Test;
import java.util.Random;

public class DriveTrainTest {
    private static final int TEST_CASES = 420420;

    @Test
    public void testNormalize() {
        Random r = new Random();
        DriveTrain d = new DriveTrain();
        for(int j = 1; j < TEST_CASES + 1; j++) {
            double[] testData = new double[4];
            for (int i = 0; i < 4; i++) {
                testData[i] = r.nextDouble() * 2;
            }
            d.normalize(testData);
            for (int i = 0; i < 4; i++) {
                if (testData[i] > 1.0) {
                    System.out.println("Normalization failed: " + testData[i] + " > 1.0 (" + j + " of " + TEST_CASES + ")");
                    Assert.fail();
                }
            }
//            String output = "Normalization tests passed (" + j + " of " + TEST_CASES + ")";
//            for (int i = 0; i < output.length(); i++) {
//                System.out.print("\b");
//            }
//            System.out.print("\r" + output);

        }
    }
}
