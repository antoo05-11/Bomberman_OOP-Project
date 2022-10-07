package uet.oop.bomberman;

import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.enemiesmaster.Enemy;
import uet.oop.bomberman.entities.stillobjectmaster.StillObjects;

public class CollisionManager {
    private Map map;
    private static final int FIX_WIDTH = 5;
    private static final int FIX_HEIGHT = 5;

    public CollisionManager(Map map) {
        this.map = map;
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
        topRight = map.getEntityAt(curX + 20, curY);
        downLeft = map.getEntityAt(curX, curY + 30);
        downRight = map.getEntityAt(curX + 20, curY + 30);
        return topLeft instanceof StillObjects || topRight instanceof StillObjects
                || downLeft instanceof StillObjects || downRight instanceof StillObjects;
    }

    /*public boolean collideForEnemy(int x, int y, String dir){
        int curX = x;
        int curY = y;

        switch (dir){
            case "LEFT":
                curX -= 1;
                break;
            case "RIGHT":
                curX += 1;
            case "UP":
                curY -= 1;
            case "DOWN":
               curY += 1;
            default:
        }
        topLeft = map.getEntityAt(curX, curY);
        topRight = map.getEntityAt(curX + 31, curY);
        downLeft = map.getEntityAt(curX, curY + 31);
        downRight = map.getEntityAt(curX + 31, curY + 31);
        return topLeft instanceof StillObjects || topRight instanceof StillObjects
                || downLeft instanceof StillObjects || downRight instanceof StillObjects;
    }*/
}
