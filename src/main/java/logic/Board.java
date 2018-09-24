package logic;

import java.util.ArrayList;

/**
 * Class which contains the board state, meaning mostly a collection of ingame entities.
 */
public class Board {

    ArrayList<Unit> units;
    ArrayList<Obstacle> obstacles;

    public void collision(Entity first, Entity second) {
        //TODO
    }
}
