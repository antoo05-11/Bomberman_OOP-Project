package uet.oop.bomberman;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.Direction.DIRECTION;
public class CollisionManager {
    private Map map;
    private static final int FIX_WIDTH = 6;
    private static final int FIX_HEIGHT = 5;

    public CollisionManager(Map map) {
        this.map = map;
    }
    
    public boolean collide(int x, int y, String dir) {
        Entity object1;
        Entity object2;
        switch (dir) {
            case "UP":
                object1 = map.getEntityAt(x + FIX_WIDTH, y + FIX_HEIGHT);
                object2 = map.getEntityAt(x + 20 - FIX_WIDTH, y + FIX_HEIGHT);
                break;
            case "DOWN":
                object1 = map.getEntityAt(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                object2 = map.getEntityAt(x + 20 - FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            case "LEFT":
                object1 = map.getEntityAt(x + FIX_WIDTH, y + FIX_HEIGHT);
                object2 = map.getEntityAt(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            case "RIGHT":
                object1 = map.getEntityAt(x + 20, y + FIX_HEIGHT);
                object2 = map.getEntityAt(x + 20, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                break;
            default:
                object1 = map.getEntityAt(x, y);
                object2 = map.getEntityAt(x, y);
                break;
        }
        if (object1 instanceof StillObjects || object2 instanceof StillObjects) {
            return true;
        }
        else return false;
    }

}
