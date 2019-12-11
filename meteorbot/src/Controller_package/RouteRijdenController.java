package Controller_package;

import TI.BoeBot;
import boebot_hardware.Lijnvolger;
import boebot_hardware.Motor;

public class RouteRijdenController implements Runnable {
    private Motor motor;
    private Lijnvolger links, midden, rechts;
    private int snelheid = 50;

    public RouteRijdenController() {
        motor = new Motor();
        links = new Lijnvolger(0);
        midden = new Lijnvolger(1);
        rechts = new Lijnvolger(2);
    }


    @Override
    public void run() {
        motor.rijden(snelheid,snelheid);
        boolean driving = true;
        while(driving){
            boolean linkselezer = links.lijndetected();
            boolean middellezer = midden.lijndetected();
            boolean rechtselezer = rechts.lijndetected();
            if(linkselezer && middellezer && rechtselezer){
                driving = false;
            }
            else if(rechtselezer && middellezer){
                motor.rijden(snelheid - (snelheid / 2), snelheid);
            }
            else if(linkselezer && middellezer){
                motor.rijden(snelheid, snelheid - (snelheid / 2));
            }
            else if(rechtselezer){
                motor.rijden(snelheid - (snelheid / 3), snelheid);
            }
            else if(linkselezer){
                motor.rijden(snelheid, snelheid - (snelheid / 3));
            }
            if(middellezer){
                motor.rijden(snelheid, snelheid);
            }
            BoeBot.wait(20);
        }
        for(int i = snelheid; i > 0; i--){
            motor.rijden(i,i);
            BoeBot.wait(10);
        }
        motor.rijden(30,-10);
        boolean draaien = true;
        BoeBot.wait(250);
        while(draaien){
            boolean middenlezer = midden.lijndetected();
            if (middenlezer){
                draaien = false;
            }
        }
        motor.rijden(0,0);
    }

}
