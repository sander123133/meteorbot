package boebot_hardware;

import TI.BoeBot;

public class Lijnvolger {
    private int inputPin;
    public Lijnvolger(int inputPin){
        this.inputPin = inputPin;
    }

    public boolean lijndetected(){
        return BoeBot.analogRead(inputPin) > 400;
    }
}
