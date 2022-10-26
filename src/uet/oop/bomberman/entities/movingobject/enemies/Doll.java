package uet.oop.bomberman.entities.movingobject.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.graphics.Sprite;

public class Doll extends Enemy {
    public Doll(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img, collisionManager);
    }
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;

    @Override
    public void randomMoving() {
        indexOfSprite++;
        if (!changeDirection) {
            int rand = (int) (Math.random() * 10);
            switch (rand % 4) {
                case 0:
                    dir = "LEFT";
                    break;
                case 1:
                    dir = "RIGHT";
                    break;
                case 2:
                    dir = "UP";
                    break;
                case 3:
                    dir = "DOWN";
                    break;
                default:
                    break;
            }
        }
        if (collideForDoll(x, y, dir, SPEED)
                || collisionManager.collidebBomb(x, y, dir, SPEED)) {
            changeDirection = false;
            indexOfSprite = 0;
        } else {
            if (dir.equals("LEFT")) {
                x -= SPEED;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir.equals("RIGHT")) {
                x += SPEED;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir.equals("UP")) {
                y -= SPEED;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir.equals("DOWN")) {
                y += SPEED;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            changeDirection = true;
        }
    }

    public boolean collideForDoll(int x, int y, String dir, int OBJECT_SPEED) {
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
        }
        Entity topLeft = GameController.mapList.get(GameController.LEVEL).getEntityAt(curX, curY);
        Entity topRight = GameController.mapList.get(GameController.LEVEL).getEntityAt(curX + WIDTH, curY);
        Entity downLeft = GameController.mapList.get(GameController.LEVEL).getEntityAt(curX, curY + HEIGHT);
        Entity downRight = GameController.mapList.get(GameController.LEVEL).getEntityAt(curX + WIDTH, curY + HEIGHT);
        return topLeft instanceof Wall || topRight instanceof Wall
                || downLeft instanceof Wall || downRight instanceof Wall;
    }
    @Override
    public void move() {
        randomMoving();
    }
}
