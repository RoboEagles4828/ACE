package org.usfirst.frc.team4828.Pixy;

public class PixySerialThread extends Thread
{
    private PixyI2C pixy;

    public PixySerialThread()
    {
        pixy = new PixyI2C();
        System.out.println("Creating Pixy Thread");
    }

    @Override
    public void run()
    {
        for(int i =0; i<20; i++){
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
