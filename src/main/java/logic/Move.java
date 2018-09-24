package logic;


import com.vividsolutions.jts.geom.Point;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Static class to execute certain predefined movements on units.
 */
public class Move {

    public enum Type {
        Straight1, Straight2, Straight3, Straight4, Straight5,
        LBank1, LBank2, LBank3, RBank1, RBank2, RBank3,
        LTurn1, LTurn2, LTurn3, RTurn1, RTurn2, RTurn3
    }

    /**
     * Moves a unit according to a given predefined movement. Calls "Board.collision" if there is one.
     *
     * @param unit  The position to move.
     * @param move  The type of movement.
     * @param board The board state to consider. The move is only executed
     */
    public static void applyMove(Unit unit, Type move, Board board) {
        BiFunction<Position, Double, Position> executor = typeToFunction(move);

        // First, see if the movement can be performed without any intersection of the center point with an obstacle.
        List<Double> stepList = stepList();
        for (double scale : stepList) {
            Point newPos = executor.apply(unit.centerPosition, scale).toPoint();
            for (Obstacle obstacle : board.obstacles) {
                if (newPos.intersects(obstacle.hitbox())) {
                    board.collision(unit, obstacle);
                }
            }
        }

        // Now move the ship as far as possible without overlapping with another ship.
        Collections.reverse(stepList);
        Position oldPos = unit.centerPosition;
        for (double scale : stepList) {
            unit.centerPosition = executor.apply(oldPos, scale);
            for (Unit otherUnit : board.units) {
                if (unit.hitbox().intersects(otherUnit.hitbox())) {
                    board.collision(unit, otherUnit);
                } else {
                    break;
                }
            }
        }

        // Finally, check if the ship collides with an obstacle at its final position.
        for (Obstacle obstacle : board.obstacles) {
            if (unit.centerPosition.toPoint().intersects(obstacle.hitbox())) {
                board.collision(unit, obstacle);
            }
        }
    }

    static List<Double> stepList() {
        double stepSize = 0.05;
        LinkedList<Double> steps = new LinkedList<>();
        steps.add(0.0);
        for (double x = 0.01; x < 1; x += stepSize)
            steps.add(x);
        steps.add(1.0);
        return steps;
    }

    /**
     * Moves a unit (or rather, its center) along a circle.
     *
     * @param position  The position of the unit.
     * @param radius    The radius of the circle.
     * @param clockwise If true, the unit moves clockwise along the circle. Anti-clockwise otherwise.
     * @param distance  The distance that the unit moves along the circle.
     */
    private static void moveCircleByDistance(Position position, double radius, boolean clockwise, double distance) {
        double angle = distance / (2 * Math.PI);
        moveCircleByAngle(position, radius, clockwise, angle);
    }

    /**
     * Moves a unit (or rather, its center) along a circle.
     *
     * @param position  The position of the unit.
     * @param radius    The radius of the circle.
     * @param clockwise If true, the unit moves clockwise along the circle. Anti-clockwise otherwise.
     * @param angle     The angle that the unit moves along the circle.
     */
    private static void moveCircleByAngle(Position position, double radius, boolean clockwise, double angle) {
        position.forward(Math.sin(angle) * radius);
        int lrMod = (clockwise ? 1 : -1);
        position.right((1 - Math.cos(angle)) * radius * lrMod);
        position.turn(angle * lrMod);
    }

    /**
     * Finds the "execution function" for a given movement type.
     *
     * @param move The movement type.
     * @return A function that takes in a position to operate on and a scale from 0 to 1 how far to perform the action.
     */
    static BiFunction<Position, Double, Position> typeToFunction(Type move) {
        switch (move) {
            case Straight1:
                return straightLambda(1);
            case Straight2:
                return straightLambda(2);
            case Straight3:
                return straightLambda(3);
            case Straight4:
                return straightLambda(4);
            case Straight5:
                return straightLambda(5);
            case LBank1:
                return bankLambda(1, false);
            case LBank2:
                return bankLambda(2, false);
            case LBank3:
                return bankLambda(3, false);
            case RBank1:
                return bankLambda(1, true);
            case RBank2:
                return bankLambda(2, true);
            case RBank3:
                return bankLambda(3, true);
            case LTurn1:
                return turnLambda(1, false);
            case LTurn2:
                return turnLambda(2, false);
            case LTurn3:
                return turnLambda(3, false);
            case RTurn1:
                return turnLambda(1, true);
            case RTurn2:
                return turnLambda(2, true);
            case RTurn3:
                return turnLambda(3, true);
        }
        throw new RuntimeException("invalid movement type");
    }

    /**
     * Moves a unit straight forward.
     *
     * @param level "Distance" level of the movement.
     */
    static BiFunction<Position, Double, Position> straightLambda(int level) {
        return (pos, scale) -> {
            Position newPos = new Position(pos);
            double distance = (level == 1 ? 4 : level == 2 ? 8 : level == 3 ? 12 : level == 4 ? 16 : 20);
            newPos.forward(distance * scale);
            return newPos;
        };
    }

    /**
     * Predefined movement that puts the backside of a unit to the point reached by a 45° turn from the frontside.
     *
     * @param level "Distance" level of the movement.
     * @param right True if the turn is to the right; false if it is to the left.
     */
    static BiFunction<Position, Double, Position> bankLambda(int level, boolean right) {
        return (pos, scale) -> {
            Position newPos = new Position(pos);
            double radius = (level == 1 ? 7.8 : level == 2 ? 13 : 18.1);
            if (scale > 0)
                newPos.forward(2);
            moveCircleByAngle(newPos, radius, right, scale * Math.PI / 4);
            if (scale == 1)
                newPos.forward(2);
            return newPos;
        };
    }

    /**
     * Predefined movement that puts the backside of a unit to the point reached by a 90° turn from the frontside.
     *
     * @param level "Distance" level of the movement.
     * @param right True if the turn is to the right; false if it is to the left.
     */
    static BiFunction<Position, Double, Position> turnLambda(int level, boolean right) {
        return (pos, scale) -> {
            Position newPos = new Position(pos);
            double radius = (level == 1 ? 3.7 : level == 2 ? 6.3 : 9);
            if (scale > 0)
                newPos.forward(2);
            moveCircleByAngle(newPos, radius, right, scale * Math.PI / 2);
            if (scale == 1)
                newPos.forward(2);
            return newPos;
        };
    }

}
