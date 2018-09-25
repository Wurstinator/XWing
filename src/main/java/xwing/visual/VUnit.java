package xwing.visual;

import xwing.logic.Position;
import xwing.logic.Unit;

import java.io.Closeable;

public class VUnit implements Closeable {
    private VPolygon polygon;
    private VLine line;
    private VText text;
    private Unit unit;

    public VUnit(Unit unit) {
        this.unit = unit;
        this.polygon = new VPolygon(unit.hitbox(new Position(0, 0, 0)));
        this.line = new VLine(2);
        this.text = new VText(unit.getShip().getPilotName() + " (" + unit.getShip().getShipName() + ")");
    }

    void draw() {
        this.polygon.setPosition(unit.centerPosition);
        this.polygon.draw();
        this.line.setPosition(unit.centerPosition);
        this.line.draw();
        this.text.setPosition(unit.centerPosition);
        this.text.draw();
    }

    @Override
    public void close() {
        polygon.close();
        line.close();
        text.close();
    }
}
