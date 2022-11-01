package uet.oop.bomberman;

import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class CollisionManager {
    private final Map map;
    private final int CENTER_OBJECT_HEIGHT;
    private final int CENTER_OBJECT_WIDTH;

    /**
     * Constructor for collision.
     */
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
    public boolean collide(int curX, int curY, String dir, int OBJECT_SPEED) {
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
        }
        topLeft = map.getEntityAt(curX, curY);
        topRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY);
        downLeft = map.getEntityAt(curX, curY + CENTER_OBJECT_HEIGHT);
        downRight = map.getEntityAt(curX + CENTER_OBJECT_WIDTH, curY + CENTER_OBJECT_HEIGHT);
        return topLeft instanceof CannotBePassedThrough || topRight instanceof CannotBePassedThrough
                || downLeft instanceof CannotBePassedThrough || downRight instanceof CannotBePassedThrough;
    }

    /**
     * Check if enemy collide with bomb and change speed.
     * @param enemy Enemy
     * @param dir String
     * @param OBJECT_SPEED int
     * @return true/false
     */
    public boolean collideBomb(Enemy enemy, String dir, int OBJECT_SPEED) {
        int curX = enemy.getX(), curY = enemy.getY();
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
        }
        int xTile = curX / Sprite.SCALED_SIZE;
        int yTile = curY / Sprite.SCALED_SIZE;
        int widthTile = (curX + CENTER_OBJECT_WIDTH) / Sprite.SCALED_SIZE;
        int heightTile = (curY + CENTER_OBJECT_WIDTH) / Sprite.SCALED_SIZE;

        for (Entity b : map.getBombsList()) {
            int xBomb = (b.getX() + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            int yBomb = (b.getY() + Sprite.SCALED_SIZE / 2) / Sprite.SCALED_SIZE;
            if ((widthTile == xBomb && yTile == yBomb)
                    || (xTile == xBomb && heightTile == yBomb)
                    || (widthTile == xBomb && heightTile == yBomb)) {
                {
                    enemy.randomSpeed(1, 3);
                    return true;
                }
            }
        }
        return false;
    }
}
