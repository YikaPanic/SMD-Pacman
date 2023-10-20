package src.autoPlay;

import src.Game;
import src.PacActor;

import java.util.Random;

public class AutoPlay2 implements AutoPlay {
    private Game game = null;
    private PacActor pacActor = null;
    private Random randomiser = null;

    public AutoPlay2(Game game, PacActor pacActor, Random randomiser) {
        this.game = game;
        this.pacActor = pacActor;
        this.randomiser = randomiser;
    }

    @Override
    public void moveInAutoMode() {

    }
}
