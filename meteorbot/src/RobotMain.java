import TI.BoeBot;
import boebot_hardware.Motor;

public class RobotMain {

    public static void main(String[] args) {
        boolean state = true;
        Motor motor = new Motor();
        BoeBot.wait(5000);
        while (true) {
            for(int index = 0; index < 75; index++){
                motor.rijden(index);
                BoeBot.wait(50);
            }
            BoeBot.wait(750);

            motor.naarLinks(5000);
            motor.naarRechts(5000);
            motor.stoppen();
            BoeBot.wait(20000);
        }
    }
}
