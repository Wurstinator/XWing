package xwing.logic;


import com.vividsolutions.jts.geom.Polygon;

/**
 * Some kind of entity on the board; usually a unit or an obstacle.
 */
public interface Entity {
    /**
     * The hit box of this entity as a polygon.
     *
     * @return The Polygon object that defines the outline.
     */
    Polygon hitbox();
}
