package Controller_package;

import TI.BoeBot;
import boebot_hardware.Lijnvolger;
import boebot_hardware.Motor;
import boebot_hardware.Ultrasone;

public class RouteRijdenController implements Runnable {
    private Motor motor;
    private Lijnvolger links, midden, rechts;
    private int snelheid = 50;
    private Ultrasone ultrasone;
    boolean driving = true;
    boolean draaien = false;
    boolean intermission;
    boolean pinged = false;
    boolean trigpinaan = false;
    int pulse = 0;

    int cycle = 0;
    int startturning = 0, startintermission = 0;


    public RouteRijdenController() {
        motor = Motor.createMotor();
        links = new Lijnvolger(0);
        midden = new Lijnvolger(1);
        rechts = new Lijnvolger(2);
        ultrasone = new Ultrasone(9,10);
    }


    @Override
    public void run() {
        motor.rijden(50,50);
        boolean everythingisfine = true;
        while(everythingisfine){

            if(!trigpinaan && cycle % 1 == 0 && !pinged){
                trigpinaan = true;
                ultrasone.trigger(trigpinaan);
            }
            else if(trigpinaan){
                trigpinaan = false;
                pinged = true;
                pulse = ultrasone.trigger(trigpinaan);
            }

            if(pinged && cycle % 52 == 0){
                if(pulse/58 <= 5){
                    motor.rijden(0,0);
                    everythingisfine = false;
                }
            }

            if(driving && (cycle%25 == 0)){
                boolean linkselezer = links.lijndetected();
                boolean middellezer = midden.lijndetected();
                boolean rechtselezer = rechts.lijndetected();

                if(linkselezer && middellezer && rechtselezer){
                    motor.rijden(50,50);
                    driving = false;
                    intermission = true;
                    startintermission = cycle;
                } else if(rechtselezer && middellezer){
                    motor.rijden(snelheid - (snelheid / 2), snelheid);
                } else if(linkselezer && middellezer){
                    motor.rijden(snelheid, snelheid - (snelheid / 2));
                } else if(rechtselezer){
                    motor.rijden(snelheid - (snelheid / 3), snelheid);
                } else if(linkselezer){
                    motor.rijden(snelheid, snelheid - (snelheid / 3));
                } else if(middellezer){
                    motor.rijden(snelheid, snelheid);
                }

            }

            if(intermission && (cycle%25 == 0)){
                motor.rijden(50,50);
                if((startintermission + 200) <  cycle){
                    intermission = false;
                    draaien = true;
                    motor.rijden(30, -10);
                    startturning = cycle;
                }
            }


            if(draaien && (cycle%25 == 0) && (startturning + 200) < cycle ){
                boolean middenlezer = midden.lijndetected();
                if (middenlezer){
                    motor.rijden(0,0);
                    draaien = false;
                }
            }

            BoeBot.wait(1);
            cycle++;
        }
    }

}
