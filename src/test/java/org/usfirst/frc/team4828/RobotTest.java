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
        System.out.println("Checking that " + 2 + " is equal to " + 2);
        Assert.assertEquals(2, 2);
    }
}
