package logic;


import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * Represents the position of a unit on a board.
 */
public class Position {
    public double x;
    public double y;
    private double angle; // in radians; clockwise; angle 0 = pointing upwards

    public Position(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle % (Math.PI * 2);
    }

    public Position(Position other) {
        this.x = other.x;
        this.y = other.y;
        this.angle = other.angle;
    }

    public void moveTo(double distance, double directionAngle) {
        x += Math.sin(directionAngle) * distance;
        y += Math.cos(directionAngle) * distance;
    }

    public void turn(double turn) {
        angle = (angle + turn) % (Math.PI * 2);
    }

    public void forward(double distance) {
        moveTo(distance, angle);
    }

    public void right(double distance) {
        moveTo(distance, (angle + Math.PI / 4) % (Math.PI * 2));
    }

    public Point toPoint() {
        return (new GeometryFactory()).createPoint(new Coordinate(x, y));
    }
}
