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

    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public Faction getFaction() {
        return faction;
    }

    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int initiative) {
        this.initiative = initiative;
    }

    public int getAttackValue() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }

    public int getAgilityValue() {
        return agilityValue;
    }

    public void setAgilityValue(int agilityValue) {
        this.agilityValue = agilityValue;
    }

    public int getHullValue() {
        return hullValue;
    }

    public void setHullValue(int hullValue) {
        this.hullValue = hullValue;
    }

    public int getShieldValue() {
        return shieldValue;
    }

    public void setShieldValue(int shieldValue) {
        this.shieldValue = shieldValue;
    }

    public int getForceCapacity() {
        return forceCapacity;
    }

    public void setForceCapacity(int forceCapacity) {
        this.forceCapacity = forceCapacity;
    }

    private int attackValue;
    private int agilityValue;
    private int hullValue;
    private int shieldValue;
    private int forceCapacity;
}
