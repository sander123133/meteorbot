import Controller_package.RouteRijdenController;
import TI.*;
import boebot_hardware.Motor;

import java.awt.*;

public class RobotMain {
    private static int backlight = 1;

    public static void main(String[] args) {
        BoeBot.wait(5000);
        Thread thread = new Thread(new RouteRijdenController());
        thread.start();
    }
}



