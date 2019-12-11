package boebot_hardware;

public class Ultrasone {
    int trigpin, outputpin;
    public Ultrasone(int trigpin, int outputpin){
        this.trigpin = trigpin;
        this.outputpin = outputpin;
    }

    public boolean ObstakelGevonden(){
        return false;
    }
}
