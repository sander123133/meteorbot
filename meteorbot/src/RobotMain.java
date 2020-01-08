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
            while(thread.isAlive()){
                BoeBot.wait(100);
            }
        }
    }
}



