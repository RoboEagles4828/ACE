package org.usfirst.frc.team4828;

import org.junit.Assert;
import org.junit.Test;

import java.awt.*;

public class RobotTest {
    @Test
    public void testTheThings() {
        System.out.println("Test ran!");
        Assert.assertTrue(true);
    }

    @Test
    public void anotherTest() {
        int a = 1;
        int b = 1;
        System.out.println("Checking that " + a + " is equal to " + b);
        Assert.assertEquals(1, 1);
    }


}
