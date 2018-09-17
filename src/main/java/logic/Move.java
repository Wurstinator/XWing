package logic;

public class Move {
    public enum Type {
        Straight1, Straight2, Straight3, LBank1, LBank2, LBank3, RBank1, RBank2, RBank3,
        LTurn1, LTurn2, LTurn3, RTurn1, RTurn2, RTurn3
    }

    public static Position applyMove(Type move, Position position) {
        switch (move) {
            case Straight1:
                position.forward(4);
                break;
        }
        return position;
    }
}
