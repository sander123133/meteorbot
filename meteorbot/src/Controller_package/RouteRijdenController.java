package Controller_package;

import TI.BoeBot;
import TI.Timer;
import View.LedView;
import boebot_hardware.*;

import java.util.ArrayList;

public class RouteRijdenController implements Runnable {
    private Motor motor;
    private Lijnvolger links, midden, rechts;
    private int snelheid = 25;
    private Ultrasone ultrasone;
    boolean driving = true;
    boolean draaien = false;
    boolean intermission;
    boolean pinged = false;
    boolean trigpinaan = false;
    LedView ledView;
    int pulse = 0;
    ArrayList<Point> route = new ArrayList<>();

    public enum Facing {
        NORTH, SOUTH, EAST, WEST
    }

    Facing boebotFacing = Facing.NORTH;

    int cycle = 0;
    int startturning = 0, startintermission = 0;


    public RouteRijdenController() {
        motor = Motor.createMotor();
        links = new Lijnvolger(0);
        midden = new Lijnvolger(1);
        rechts = new Lijnvolger(2);
        ultrasone = new Ultrasone(10, 11);
        ledView = new LedView();

    }


    @Override
    public void run() {
        route.add(new Point(0, 0));
        route.add(new Point(0, 1));
        route.add(new Point(1, 1));
        route.add(new Point(1,2));
        route.add(new Point(0,2));
        route.add(new Point(0,1));
        checkifTurningDirection(route.get(0),route.get(1));
        boolean everythingisfine = true;
        Thread ultrasSonethread = new Thread(ultrasone);
        ultrasSonethread.start();
        Thread ledThread = new Thread(ledView);
        ledThread.start();
        ledView.changeStatus(Status.DRIVING);
        while (everythingisfine) {
            if(cycle % 52 == 0){
                if(ultrasone.objectDetected){
                    everythingisfine = false;
                    ledView.changeStatus(Status.OBSTACLEFOUND);
                }
            }

            if (driving && (cycle % 10 == 0)) {
                boolean linkselezer = links.lijndetected();
                boolean middellezer = midden.lijndetected();
                boolean rechtselezer = rechts.lijndetected();

                if (linkselezer && middellezer && rechtselezer) {
                    motor.rijden(25, 25);
                    driving = false;
                    intermission = true;
                    startintermission = cycle;
                    ledView.aanpasLedAan(true);
                } else if (rechtselezer && middellezer) {
                    motor.rijden(snelheid - (snelheid / 3), snelheid);
                    ledView.aanpasLedAan(false);
                } else if (linkselezer && middellezer) {
                    motor.rijden(snelheid, snelheid - (snelheid / 3));
                    ledView.aanpasLedAan(false);
                } else if (rechtselezer) {
                    motor.rijden(snelheid - (snelheid / 4), snelheid);
                    ledView.aanpasLedAan(false);
                } else if (linkselezer) {
                    motor.rijden(snelheid, snelheid - (snelheid / 4));
                    ledView.aanpasLedAan(false);
                } else if (middellezer) {
                    motor.rijden(snelheid, snelheid);
                    ledView.aanpasLedAan(true);
                }

            }

            if (intermission && (cycle % 25 == 0)) {
                motor.rijden(25, 25);
                if ((startintermission + 400) < cycle) {
                    intermission = false;
                    if (route.size() > 1) {
                            if ((draaien = checkifTurningDirection(route.get(0), route.get(1)))) {
                                startturning = cycle;
                                ledView.changeStatus(Status.TURNING);
                            }

                    }
                    else {
                        everythingisfine = false;
                    }
                    route.remove(0);
                }
            }


                if (draaien && (cycle % 25 == 0) && (startturning + 200) < cycle) {
                    boolean middenlezer = midden.lijndetected();
                    if (middenlezer) {
                        motor.rijden(snelheid, snelheid);
                        draaien = false;
                        if(route.size() > 0) {
                            driving = true;
                            ledView.changeStatus(Status.DRIVING);
                        }
                        else{
                            everythingisfine = false;
                        }
                    }
                }

                BoeBot.wait(1);
                cycle++;
                if(cycle > 3000000){
                    cycle = 0;
                }

        }
        motor.rijden(0,0);
        ledView.statusLedAan(true);
    }

    public boolean checkifTurningDirection(Point startingPoint, Point endingPoint) {
        boolean turning = false;
        if (startingPoint.getX() - endingPoint.getX() == -1) {
            switch (boebotFacing) {
                case SOUTH:
                    motor.rijden(-10, 30);
                    turning = true;
                    break;
                case NORTH:
                    motor.rijden(30, -10);
                    turning = true;
                    break;
                case WEST:
                    motor.rijden(snelheid, snelheid);
                    driving = true;
                    break;
            }
            boebotFacing = Facing.WEST;
        } else if (startingPoint.getX() - endingPoint.getX() == 1) {
            switch (boebotFacing) {
                case SOUTH:
                    motor.rijden(30, -10);
                    turning = true;
                    break;
                case NORTH:
                    motor.rijden(-10, 30);
                    turning = true;
                    break;
                case EAST:
                    motor.rijden(snelheid, snelheid);
                    driving = true;
                    break;
            }
            boebotFacing = Facing.EAST;

        } else if (startingPoint.getY() - endingPoint.getY() == -1) {
            switch (boebotFacing) {
                case NORTH:
                    motor.rijden(snelheid, snelheid);
                    driving = true;
                    break;
                case WEST:
                    motor.rijden(-10, 30);
                    turning = true;
                    break;
                case EAST:
                    motor.rijden(30, -10);
                    turning = true;
                    break;
            }
            boebotFacing = Facing.NORTH;
        }else if (startingPoint.getY() - endingPoint.getY() == 1) {
            switch (boebotFacing) {
                case SOUTH:
                    motor.rijden(snelheid, snelheid);
                    driving = true;
                    break;
                case WEST:
                    motor.rijden(30, -10);
                    turning = true;
                    break;
                case EAST:
                    motor.rijden(-10, 30);
                    turning = true;
                    break;
            }
            boebotFacing = Facing.SOUTH;
        }
        return turning;

    }
}
