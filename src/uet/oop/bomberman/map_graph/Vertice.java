package uet.oop.bomberman.map_graph;

import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;

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

    public boolean isAVerticeInGraph(Map map) {

        if (!(map.getEntityAt(xTilePos, yTilePos + 1) instanceof Bomb
                && map.getEntityAt(xTilePos, yTilePos - 1) instanceof Bomb
                && map.getEntityAt(xTilePos, yTilePos + 1) instanceof Brick
                && map.getEntityAt(xTilePos, yTilePos - 1) instanceof Brick
                && map.getEntityAt(xTilePos, yTilePos + 1) instanceof Wall
                && map.getEntityAt(xTilePos, yTilePos - 1) instanceof Wall
                && map.getEntityAt(xTilePos + 1, yTilePos) == null
                && map.getEntityAt(xTilePos - 1, yTilePos) == null
        )
                && !(map.getEntityAt(xTilePos, yTilePos + 1) == null
                && map.getEntityAt(xTilePos, yTilePos - 1) == null
                && map.getEntityAt(xTilePos + 1, yTilePos) instanceof Bomb
                && map.getEntityAt(xTilePos - 1, yTilePos) instanceof Bomb
                && map.getEntityAt(xTilePos + 1, yTilePos) instanceof Brick
                && map.getEntityAt(xTilePos - 1, yTilePos) instanceof Brick
                && map.getEntityAt(xTilePos + 1, yTilePos) instanceof Wall
                && map.getEntityAt(xTilePos - 1, yTilePos) instanceof Wall

        )) {
            return true;
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