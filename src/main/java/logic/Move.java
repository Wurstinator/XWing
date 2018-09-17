package logic;

public class Move {
    public enum Type {
        Straight1, Straight2, Straight3, Straight4, Straight5,
        LBank1, LBank2, LBank3, RBank1, RBank2, RBank3,
        LTurn1, LTurn2, LTurn3, RTurn1, RTurn2, RTurn3
    }

    public static Position applyMove(Type move, Position position) {
        switch (move) {
            case Straight1:
                moveStraight(4, position);
                break;
            case Straight2:
                moveStraight(8, position);
                break;
            case Straight3:
                moveStraight(12, position);
                break;
            case Straight4:
                moveStraight(16, position);
                break;
            case Straight5:
                moveStraight(20, position);
                break;
            case LBank1:
                moveLBank(7.8, position);
                break;
            case LBank2:
                moveLBank(13, position);
                break;
            case LBank3:
                moveLBank(18.1, position);
                break;
            case RBank1:
                moveRBank(7.8, position);
                break;
            case RBank2:
                moveRBank(13, position);
                break;
            case RBank3:
                moveRBank(18.1, position);
                break;
            case LTurn1:
                moveLTurn(3.7, position);
                break;
            case LTurn2:
                moveLTurn(6.3, position);
                break;
            case LTurn3:
                moveLTurn(9, position);
                break;
            case RTurn1:
                moveRTurn(3.7, position);
                break;
            case RTurn2:
                moveRTurn(6.3, position);
                break;
            case RTurn3:
                moveRTurn(9, position);
                break;
        }
        return position;
    }

    private static void moveStraight(double distance, Position position) {
        position.forward(distance);
    }

    // radius of the center line
    private static void moveLBank(double radius, Position position) {
        position.forward(2);
        position.forward(radius * Math.sin(Math.PI / 4));
        position.right(radius * (Math.cos(Math.PI / 4) - 1));
        position.turn270();
        position.forward(2);
    }

    // radius of the center line
    private static void moveRBank(double radius, Position position) {
        position.forward(2);
        position.forward(radius * Math.sin(Math.PI / 4));
        position.right(radius * (1 - Math.cos(Math.PI / 4)));
        position.turn90();
        position.forward(2);
    }

    // radius of the center line
    private static void moveLTurn(double radius, Position position) {
        position.forward(2);
        position.forward(radius);
        position.right(-radius);
        position.turn270();
        position.forward(2);
    }

    // radius of the center line
    private static void moveRTurn(double radius, Position position) {
        position.forward(2);
        position.forward(radius);
        position.right(radius);
        position.turn90();
        position.forward(2);
    }
}
