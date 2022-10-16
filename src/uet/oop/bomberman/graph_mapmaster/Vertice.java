package uet.oop.bomberman.graph_mapmaster;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;

import java.util.List;

public class Vertice {
    /**
     * Tile position on map.
     */
    private int xTilePos;
    private int yTilePos;

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

    public boolean isAVerticeInGraph(List<List<Entity>> mapInfo) {
        if (mapInfo.get(yTilePos).get(xTilePos) instanceof Grass) {
            if (!(mapInfo.get(yTilePos + 1).get(xTilePos) instanceof StillObjects
                    && mapInfo.get(yTilePos - 1).get(xTilePos) instanceof StillObjects
                    && mapInfo.get(yTilePos).get(xTilePos + 1) instanceof Grass
                    && mapInfo.get(yTilePos).get(xTilePos - 1) instanceof Grass)
                    && !(mapInfo.get(yTilePos + 1).get(xTilePos) instanceof Grass
                    && mapInfo.get(yTilePos - 1).get(xTilePos) instanceof Grass
                    && mapInfo.get(yTilePos).get(xTilePos + 1) instanceof StillObjects
                    && mapInfo.get(yTilePos).get(xTilePos - 1) instanceof StillObjects)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("(%d,%d)", xTilePos, yTilePos);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertice) {
            Vertice vertice = (Vertice) obj;
            if (vertice.getxTilePos() == xTilePos && vertice.getyTilePos() == yTilePos) return true;
        }
        return false;
    }
}
