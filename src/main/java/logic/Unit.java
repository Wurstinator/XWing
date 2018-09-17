package logic;


// Represents a live unit on the board.
public class Unit {
    private final Ship ship;
    private int damage;
    private int stress;
    private Position centerPosition;

    public Unit(Ship ship, Position centerPosition) {
        this.ship = ship;
        this.damage = 0;
        this.stress = 0;
        this.centerPosition = centerPosition;
    }
}
