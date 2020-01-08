package boebot_hardware;

import TI.BoeBot;

public class Ultrasone implements Runnable {
    int trigpin, outputpin;
    private static Ultrasone ultrasone;
    public boolean objectDetected;

    public static Ultrasone createUltrasone(int trigpin, int outputpin){
        if(ultrasone == null){
            ultrasone = new Ultrasone(trigpin,outputpin);
            return ultrasone;
        }
        else{
            return ultrasone;
        }
    }

    /**
     * creates an instance of Ultrasone
     * @param trigpin is the port that will create a signal
     * @param outputpin is the port that will read the distance
     */
    public Ultrasone(int trigpin, int outputpin) {
        this.trigpin = trigpin;
        this.outputpin = outputpin;
    }

    public int trigger(boolean state) {
        int pulse = 0;
        BoeBot.digitalWrite(trigpin, state);
        if(!state) {
            pulse = BoeBot.pulseIn(trigpin, true, 10000);
        }
        return pulse;
    }


    @Override
    /**
     * thread that periodically activates the ultrasone process
     */
    public void run() {
        while(!objectDetected) {
            BoeBot.digitalWrite(trigpin, true);
            BoeBot.wait(1);
            BoeBot.digitalWrite(trigpin, false);
            int pulse = BoeBot.pulseIn(outputpin, true, 10000);
            System.out.println(pulse);
            System.out.println("detecting");
            if (pulse / 58 < 3 && pulse >= 0) {
                objectDetected = true;
            }
            BoeBot.wait(50);
        }

    }
}
