package boebot_hardware;

import TI.*;

public class Motor {
    private Servo sLinks;
    private Servo sRechts;

    public Motor(){
        sLinks = new Servo(12);
        sRechts = new Servo(13);
        sLinks.start();
        sRechts.start();
    }

    public void stoppen(){
        sLinks.update(1500);
        sRechts.update(1500);
    }

    public void rijden(int speed){
       sLinks.update(1500 + (3 * speed));
       sRechts.update(1500 - (3 * speed));
    }

    public void naarLinks(int tijd){
        Timer tijdStip = new Timer(tijd);
        sLinks.update(1800);
        sRechts.update(1800);
        while(!tijdStip.timeout()){
            BoeBot.wait(10);
        }

        sLinks.update(1500);
        sRechts.update(1500);
    }

    public void naarRechts(int tijd){
        Timer tijdStip = new Timer(tijd);
        sLinks.update(1300);
        sRechts.update(1300);
        while(!tijdStip.timeout()){
            BoeBot.wait(10);
        }
        sLinks.update(1500);
        sRechts.update(1500);
    }
}
