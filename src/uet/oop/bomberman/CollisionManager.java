package uet.oop.bomberman;

import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.Entity;

public class CollisionManager {
    private Map map;
    private static final int FIX_WIDTH = 5;
    private static final int FIX_HEIGHT = 5;
    private int CENTER_OBJECT_HEIGHT;
    private int CENTER_OBJECT_WIDTH;

    public CollisionManager(Map map, int objectWidth, int objectHeight) {
        this.map = map;
        CENTER_OBJECT_WIDTH = objectWidth;
        CENTER_OBJECT_HEIGHT = objectHeight;
    }

    public Map getMap() {
        return map;
    }

    public Entity topLeft;
    public Entity topRight;
    public Entity downLeft;
    public Entity downRight;

    /**
     * Check if object collides with wall in map.
     * x and y are pixel position of object,
     * dir and speed is updated continually.
     */
    public boolean collide(int x, int y, String dir, int OBJECT_SPEED) {
        int curX = x;
        int curY = y;
        switch (dir) {
            case "UP":
                curY -= OBJECT_SPEED;
                break;
            case "DOWN":
                curY += OBJECT_SPEED;
                break;
            case "LEFT":
                curX -= OBJECT_SPEED;
                break;
            case "RIGHT":
                curX += OBJECT_SPEED;
                break;
            default:
        }
        topLeft = map.getEntityAt(curX, curY);
        topRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY);
        downLeft = map.getEntityAt(curX, curY + CENTER_OBJECT_HEIGHT);
        downRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY + CENTER_OBJECT_HEIGHT);
        return topLeft instanceof CannotBePassedThrough || topRight instanceof CannotBePassedThrough
                || downLeft instanceof CannotBePassedThrough || downRight instanceof CannotBePassedThrough;
    }
}
