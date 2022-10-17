package uet.oop.bomberman;

import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemiesmaster.Enemy;
import uet.oop.bomberman.entities.itemmaster.Item;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;

public class CollisionManager {
    private Map map;
    private static final int FIX_WIDTH = 5;
    private static final int FIX_HEIGHT = 5;
    private int CENTER_OBJECT_HEIGHT;
    private int CENTER_OBJECT_WIDTH;


    public CollisionManager(Map map, int width, int height) {
        this.map = map;
        CENTER_OBJECT_WIDTH = width;
        CENTER_OBJECT_HEIGHT = height;
    }

    public Map getMap() {
        return map;
    }

    public Entity topLeft;
    public Entity topRight;
    public Entity downLeft;
    public Entity downRight;

    public boolean collide(int x, int y, String dir) {
        int curX = x;
        int curY = y;
        switch (dir) {
            case "UP":
                curY -= Bomber.SPEED;
                break;
            case "DOWN":
                curY += Bomber.SPEED;
                break;
            case "LEFT":
                curX -= Bomber.SPEED;
                break;
            case "RIGHT":
                curX += Bomber.SPEED;
                break;
            default:
        }
        topLeft = map.getEntityAt(curX, curY);
        topRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY);
        downLeft = map.getEntityAt(curX, curY + CENTER_OBJECT_HEIGHT);
        downRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY + CENTER_OBJECT_HEIGHT);
        return topLeft instanceof StillObjects || topRight instanceof StillObjects
                || downLeft instanceof StillObjects || downRight instanceof StillObjects;
    }
}
