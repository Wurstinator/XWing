package xwing.logic;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

/**
 * Represents a ship on the board.
 */
public class Unit implements Entity {
    private final Ship ship;

    public Ship getShip() {
        return ship;
    }

    private int damage;
    private int stress;
    public Position centerPosition;

    public Unit(Ship ship, Position centerPosition) {
        this.ship = ship;
        this.damage = 0;
        this.stress = 0;
        this.centerPosition = centerPosition;
    }

    public Polygon hitbox() {
        return hitbox(centerPosition);
    }

    public Polygon hitbox(Position pos) {
        Position topleft = new Position(pos), botright = new Position(pos);
        topleft.forward(2);
        topleft.right(-1.75);
        botright.forward(-2);
        botright.right(1.75);
        double top = topleft.y, bot = botright.y, left = topleft.x, right = botright.x;
        Coordinate[] coordinates = {
                new Coordinate(left, top),
                new Coordinate(right, top),
                new Coordinate(right, bot),
                new Coordinate(left, bot),
                new Coordinate(left, top)
        };
        return (new GeometryFactory()).createPolygon(coordinates);
    }
}
