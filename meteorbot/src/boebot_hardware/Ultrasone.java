package boebot_hardware;

import TI.BoeBot;

public class Ultrasone {
    int trigpin, outputpin;

    public Ultrasone(int trigpin, int outputpin) {
        this.trigpin = trigpin;
        this.outputpin = outputpin;
    }

    public int trigger(boolean state) {
        int pulse = 0;
        BoeBot.digitalWrite(trigpin, state);
        if(!state) {
            pulse = BoeBot.pulseIn(10, true, 10000);
        }
        return pulse;
    }


}
