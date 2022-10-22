package uet.oop.bomberman.graph_mapmaster;

import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.Grass;

import java.util.List;

public class Vertice {
    /**
     * Tile position on map.
     */
    private final int xTilePos;
    private final int yTilePos;

    public Vertice(int xTilePos, int yTilePos) {
        this.xTilePos = xTilePos;
        this.yTilePos = yTilePos;
    }

    public int getxTilePos() {
        return xTilePos;
    }

    public int getyTilePos() {
        return yTilePos;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", xTilePos, yTilePos);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertice) {
            Vertice vertice = (Vertice) obj;
            return vertice.getxTilePos() == xTilePos && vertice.getyTilePos() == yTilePos;
        }
        return false;
    }
}
