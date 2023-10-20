package src.autoPlay;

import ch.aplu.jgamegrid.Location;
import ch.aplu.jgamegrid.*;
import src.Game;
import src.PacActor;

import java.util.List;
import java.util.Random;

public class AutoPlay1 implements AutoPlay {
    private Game game = null;
    private PacActor pacActor = null;
    private Random randomiser = null;

    public AutoPlay1(Game game, PacActor pacActor, Random randomiser) {
        this.game = game;
        this.pacActor = pacActor;
        this.randomiser = randomiser;
    }

    @Override
    public void moveInAutoMode() {
        Location closestPill = closestPillLocation();
        double oldDirection = pacActor.getDirection();

        Location.CompassDirection compassDir =
                pacActor.getLocation().get4CompassDirectionTo(closestPill);
        Location next = pacActor.getLocation().getNeighbourLocation(compassDir);
        pacActor.setDirection(compassDir);
        if (!pacActor.isVisited(next) && pacActor.canMove(next)) {
            pacActor.setLocation(next);
        } else {
            // normal movement
            int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
            pacActor.setDirection(oldDirection);
            pacActor.turn(sign * 90);  // Try to turn left/right
            next = pacActor.getNextMoveLocation();
            if (pacActor.canMove(next)) {
                pacActor.setLocation(next);
            } else {
                pacActor.setDirection(oldDirection);
                next = pacActor.getNextMoveLocation();
                if (pacActor.canMove(next)) // Try to move forward
                {
                    pacActor.setLocation(next);
                } else {
                    pacActor.setDirection(oldDirection);
                    pacActor.turn(-sign * 90);  // Try to turn right/left
                    next = pacActor.getNextMoveLocation();
                    if (pacActor.canMove(next)) {
                        pacActor.setLocation(next);
                    } else {
                        pacActor.setDirection(oldDirection);
                        pacActor.turn(180);  // Turn backward
                        next = pacActor.getNextMoveLocation();
                        pacActor.setLocation(next);
                    }
                }
            }
        }
        next = game.isPortal(next);//Portal jump. Change the back automatic policy if you want
        pacActor.setLocation(next);
        pacActor.eatPill(next);
        pacActor.addVisitedList(next);
    }


    private Location closestPillLocation() {
        int currentDistance = 1000;
        Location currentLocation = null;
        List<Location> pillAndItemLocations = game.getPillAndItemLocations();
        for (Location location : pillAndItemLocations) {
            int distanceToPill = location.getDistanceTo(pacActor.getLocation());
            if (distanceToPill < currentDistance) {
                currentLocation = location;
                currentDistance = distanceToPill;
            }
        }

        return currentLocation;
    }
}
