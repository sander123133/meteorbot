package Controller_package;

import TI.BoeBot;
import boebot_hardware.Lijnvolger;
import boebot_hardware.Motor;

public class RouteRijdenController implements Runnable {
    private Motor motor;
    private Lijnvolger links, midden, rechts;
    private int snelheid = 50;
    boolean driving = true;
    boolean draaien = false;
    boolean intermission;

    int cycle = 0;
    int startturning = 0, startintermission = 0;


    public RouteRijdenController() {
        motor = new Motor();
        links = new Lijnvolger(0);
        midden = new Lijnvolger(1);
        rechts = new Lijnvolger(2);
    }


    @Override
    public void run() {
        motor.rijden(50,50);

        while(true){
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




        /*for(int i = snelheid; i > 0; i--){
            motor.rijden(i,i);
            BoeBot.wait(10);
        }
        */
    }

}
