package logic;


import com.vividsolutions.jts.geom.Polygon;

/**
 * Some kind of entity on the board; usually a unit or an obstacle.
 */
public interface Entity {
    /**
     * The hitbox of this entity as a polygon.
     *
     * @return The Polygon object that defines the outline.
     */
    public Polygon hitbox();
}
