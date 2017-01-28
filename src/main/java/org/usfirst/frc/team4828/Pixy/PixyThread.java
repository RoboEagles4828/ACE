package org.usfirst.frc.team4828.Pixy;

public class PixyThread extends Thread
{
    private PixyI2C pixy;

    public PixyThread()
    {
        pixy = new PixyI2C();
        System.out.println("Creating Pixy Thread");
    }

    public void run()
    {
        while (true){
            try{
                pixy.readPacket(1);
            }
            catch (PixyException e){
                System.out.println(e.getMessage());
            }
            System.out.println("PixyX: " + pixy.getX());
            System.out.println("PixyY: " + pixy.getY());
        }
    }
}
