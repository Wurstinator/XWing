package xwing.visual;

import xwing.logic.Position;
import xwing.logic.Unit;

import java.io.Closeable;

public class VUnit implements Closeable {
    private VPolygon polygon;
    private VLine line;
    private Unit unit;

    public VUnit(Unit unit) {
        this.unit = unit;
        this.polygon = new VPolygon(unit.hitbox(new Position(0, 0, 0)));
        this.line = new VLine(2);
    }

    public void draw() {
        this.polygon.setPosition(unit.centerPosition);
        this.polygon.draw();
        this.line.setPosition(unit.centerPosition);
        this.line.draw();
    }

    @Override
    public void close() {
        polygon.close();
        line.close();
    }
}
