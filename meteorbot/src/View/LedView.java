package View;

import TI.BoeBot;
import TI.Timer;
import boebot_hardware.Status;

public class LedView implements Runnable{
    int ledPinStatus = 7;
    int ledPinaanpassen = 6;
    Status state = Status.DRIVING;

    public LedView(){

    }

    public void statusLedAan(boolean aan){
        BoeBot.digitalWrite(ledPinStatus,aan);
    }

    public void aanpasLedAan(boolean aan){
        BoeBot.digitalWrite(ledPinaanpassen,aan);

    }

    public void changeStatus(Status state){
        this.state = state;
    }

    @Override
    public void run() {
        statusLedAan(true);
        aanpasLedAan(true);
        Timer timer = new Timer(100);
        timer.mark();
        boolean objectfound = false;
        while(!objectfound) {
            switch (state){
               case DRIVING:
                    statusLedAan(false);
                    break;
                case TURNING:
                    boolean aan = false;
                    timer.mark();
                    while(state == Status.TURNING){
                        if(timer.timeout()){
                            aan = !aan;
                            statusLedAan(aan);
                        }
                    }
                    break;
                case OBSTACLEFOUND:
                    statusLedAan(true);
                    objectfound = false;
                    break;
                case CROSSPOINT:
                    statusLedAan(false);
                    aanpasLedAan(false);
                    BoeBot.wait(250);
                    statusLedAan(true);
                    aanpasLedAan(true);
                    break;
            }
        }
    }
}
