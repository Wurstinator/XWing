import logic.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Ship foo;
        try {
            foo = ShipReader.ReadShipFromFile("/home/wurst/Documents/git/XWing/ships.json");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Unit bar = new Unit(foo, new Position(0, 0, Math.PI));
        bar.centerPosition = Move.applyMove(Move.Type.LTurn1, bar.centerPosition);
    }
}
