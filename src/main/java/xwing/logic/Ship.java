package xwing.logic;


// Represents a "database" ship-pilot combination, i.e. the fixed values that can be found on a card.
public class Ship {
    public enum Faction {
        Rebellion, Empire
    }

    String pilotName;
    String shipName;
    Faction faction;
    int initiative;
    private int attackValue;
    private int agilityValue;
    private int hullValue;
    private int shieldValue;
    private int forceCapacity;
}
