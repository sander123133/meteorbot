package boebot_hardware;

import TI.*;

public class Motor {
    private Servo sLinks;
    private Servo sRechts;

    public Motor(){
        sLinks = new Servo(13);
        sRechts = new Servo(12);
        sLinks.start();
        sRechts.start();
    }

    public void rijden(int linkssnelheid, int rechtssnelheid){
        sLinks.update(1500 + linkssnelheid);
        sRechts.update(1500 - rechtssnelheid);
    }

}
