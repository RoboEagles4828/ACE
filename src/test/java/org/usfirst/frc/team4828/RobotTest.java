package org.usfirst.frc.team4828;

import org.junit.Assert;
import org.junit.Test;

public class RobotTest {
    @Test
    public void testTheThings() {
        System.out.println("Test ran!");
        Assert.assertTrue(true);
    }

    @Test
    public void anotherTest() {
        int firstTest = 1;
        int secondTest = 1;
        System.out.println("Checking that " + firstTest + " is equal to " + secondTest);
        Assert.assertEquals(1, 1);
    }


}
