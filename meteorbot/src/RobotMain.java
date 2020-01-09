import Controller_package.JsonToRouteController;
import Controller_package.RouteRijdenController;
import TI.*;
import boebot_hardware.Motor;

import java.awt.*;

public class RobotMain {
    private static int backlight = 1;

    public static void main(String[] args) {
        while(true) {
            JsonToRouteController json = new JsonToRouteController();
            Thread thread = new Thread(new RouteRijdenController(json.route));
            thread.start();
            Servo mijnServo = new Servo(12);
            Servo mijnServo2 = new Servo(13);
            mijnServo.update(1700);
            mijnServo2.update(1300);
            while(thread.isAlive()){
                BoeBot.wait(100);
            }
        }
    }
}



