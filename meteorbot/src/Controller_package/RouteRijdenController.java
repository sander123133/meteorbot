package Controller_package;

import TI.BoeBot;
import boebot_hardware.Lijnvolger;
import boebot_hardware.Motor;
import boebot_hardware.Point;
import boebot_hardware.Ultrasone;

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
        ultrasone = new Ultrasone(9, 10);

    }


    @Override
    public void run() {
        route.add(new Point(0, 0));
        route.add(new Point(0, 1));
        route.add(new Point(1, 1));
        route.add(new Point(1, 2));
        checkifTurningDirection(route.get(0),route.get(1));
        boolean everythingisfine = true;
        while (everythingisfine) {
            if (!trigpinaan && cycle % 1 == 0 && !pinged) {
                trigpinaan = true;
                ultrasone.trigger(trigpinaan);
            } else if (trigpinaan) {
                trigpinaan = false;
                pinged = true;
                pulse = ultrasone.trigger(trigpinaan);
            }

            if (pinged && cycle % 52 == 0) {
                System.out.println(pulse /58);
                if (pulse / 58 <= 3 && pulse / 58 != 0) {
                    motor.rijden(0, 0);
                    everythingisfine = false;
                }
                pinged = false;
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
                } else if (rechtselezer && middellezer) {
                    motor.rijden(snelheid - (snelheid / 3), snelheid);
                } else if (linkselezer && middellezer) {
                    motor.rijden(snelheid, snelheid - (snelheid / 3));
                } else if (rechtselezer) {
                    motor.rijden(snelheid - (snelheid / 4), snelheid);
                } else if (linkselezer) {
                    motor.rijden(snelheid, snelheid - (snelheid / 4));
                } else if (middellezer) {
                    motor.rijden(snelheid, snelheid);
                }

            }

            if (intermission && (cycle % 25 == 0)) {
                motor.rijden(25, 25);
                if ((startintermission + 400) < cycle) {
                    intermission = false;
                    if (route.size() > 1) {
                            if ((draaien = checkifTurningDirection(route.get(0), route.get(1))))
                                startturning = cycle;

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
