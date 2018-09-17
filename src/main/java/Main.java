import logic.Ship;
import logic.ShipReader;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Ship test = ShipReader.ReadShipFromFile("/home/wurst/Documents/git/XWing/ships.json");
            System.out.println(test);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
