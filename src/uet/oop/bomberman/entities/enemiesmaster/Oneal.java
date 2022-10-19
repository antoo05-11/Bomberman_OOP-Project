package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graph_mapmaster.Graph;
import uet.oop.bomberman.graph_mapmaster.Vertice;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Oneal extends Enemy {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    OnealStatus onealStatus;

    public enum OnealStatus {
        CONNECTED,
        NOT_CONNECTED,
        INVALID
    }

    private List<Vertice> path;
    private Entity bomber;

    /**
     * Full constructor with param bomber for update continually position of bomberman.
     */
    public Oneal(int xUnit, int yUnit, Image img, CollisionManager collisionManager, Entity bomber) {
        super(xUnit, yUnit, img, collisionManager);
        onealStatus = OnealStatus.NOT_CONNECTED;
        this.bomber = bomber;
    }

    public void setOnealStatus(OnealStatus onealStatus) {
        this.onealStatus = onealStatus;
    }

    public OnealStatus getOnealStatus() {
        return onealStatus;
    }

    public void moveAlongPath() {
        Vertice src = path.get(0);
        Vertice dst = path.get(1);
        indexOfSprite++;

        // Case 1: left
        if (src.getxTilePos() >= dst.getxTilePos()) {
            if (x > dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "LEFT", SPEED)) {
                    setSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], indexOfSprite, 20).getFxImage());
                    x -= SPEED;
                }
            }
        }
        // Case 2: right
        if (src.getxTilePos() <= dst.getxTilePos()) {
            if (x < dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "RIGHT", SPEED)) {
                    setSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20).getFxImage());
                    x += SPEED;
                }
            }
        }

        // Case 3: up
        if (src.getyTilePos() >= dst.getyTilePos()) {
            if (y > dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "UP", SPEED)) {
                    setSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20).getFxImage());
                    y -= SPEED;
                }
            }
        }

        // Case 4: down
        if (src.getyTilePos() <= dst.getyTilePos()) {
            if (y < dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "DOWN", SPEED)) {
                    setSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], indexOfSprite, 20).getFxImage());
                    y += SPEED;
                }
            }
        }
    }

    public void move() {
        int onealIndex = Graph.getVerticeIndex(x + Oneal.WIDTH / 2, y + Oneal.HEIGHT / 2);
        int bomberIndex = Graph.getVerticeIndex(bomber.getX(), bomber.getY());

        if (onealStatus == OnealStatus.NOT_CONNECTED) {
            path = collisionManager.getMap().getGraph().breathFirstSearch(onealIndex, bomberIndex);
            if (path != null) onealStatus = OnealStatus.CONNECTED;
        }

        if (onealStatus != OnealStatus.CONNECTED) {
            randomMoving(); // If no connection to bomber then random move or has other oneal chase bomber.
        } else {
            path = collisionManager.getMap().getGraph().breathFirstSearch(onealIndex, bomberIndex);
            if (path != null) {
                moveAlongPath();
            }
        }

    }

    public int getDistanceToBomber() {
        if (path == null) return -1;
        return path.size();
    }

}
