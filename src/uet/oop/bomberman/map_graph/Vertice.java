package uet.oop.bomberman.map_graph;

public class Vertice {
    /**
     * Tile position on map.
     */
    private final int xTilePos;
    private final int yTilePos;

    /**
     * Constructor for vertices.
     */
    public Vertice(int xTilePos, int yTilePos) {
        this.xTilePos = xTilePos;
        this.yTilePos = yTilePos;
    }

    /**
     * Get x tile pos.
     */
    public int getxTilePos() {
        return xTilePos;
    }

    /**
     * Get y tile pos.
     */
    public int getyTilePos() {
        return yTilePos;
    }

    /**
     * This is to string.
     */
    @Override
    public String toString() {
        return String.format("(%d,%d)", xTilePos, yTilePos);
    }

    /**
     * This is equals.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertice) {
            Vertice vertice = (Vertice) obj;
            return vertice.getxTilePos() == xTilePos && vertice.getyTilePos() == yTilePos;
        }
        return false;
    }
}
