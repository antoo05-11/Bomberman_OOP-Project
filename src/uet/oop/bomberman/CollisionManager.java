package uet.oop.bomberman;

import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;
import uet.oop.bomberman.entities.stillobjectmaster.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Direction.DIRECTION;

public class CollisionManager {
    private Map map;
    private static final int FIX_WIDTH = 5;
    private static final int FIX_HEIGHT = 5;

    public CollisionManager(Map map) {
        this.map = map;
    }

    public boolean collide(int x, int y, String dir) {
        Entity object1;
        Entity object2;
        Entity object3;
        Entity object4;
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
        }

        object1 = map.getEntityAt(curX, curY);
        object2 = map.getEntityAt(curX + 20, curY);
        object3 = map.getEntityAt(curX, curY + 30);
        object4 = map.getEntityAt(curX + 20, curY + 30);

        return object1 instanceof StillObjects || object2 instanceof StillObjects
                || object3 instanceof StillObjects || object4 instanceof StillObjects;
    }

}
