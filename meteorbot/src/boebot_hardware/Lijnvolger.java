package boebot_hardware;

import TI.BoeBot;

public class Lijnvolger {
    private int inputPin;
    public Lijnvolger(int inputPin){
        this.inputPin = inputPin;
    }

    public boolean lijndetected(){
       // int value = BoeBot.analogRead(inputPin);
        //System.out.println(value);
        return BoeBot.analogRead(inputPin) > 400;
    }
}
