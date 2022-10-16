package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.graph_mapmaster.Graph;
import uet.oop.bomberman.graph_mapmaster.Vertice;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Oneal extends Enemy {
    public static int SPEED = 1;
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    OnealStatus onealStatus;

    public enum OnealStatus {
        CONNECTED,
        NOT_CONNECTED,
        INVALID
    }

    private Graph graph;
    private List<Vertice> path;

    public Oneal(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img, collisionManager);
        onealStatus = OnealStatus.NOT_CONNECTED;
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
        // TODO: 4 cases of direction
        //Case 1: left
        if (src.getxTilePos() >= dst.getxTilePos()) {
            if (x > dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "LEFT")) {
                    setSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], indexOfSprite, 20).getFxImage());
                    x -= SPEED;
                }
            }
        }
        //Case 2: right
        if (src.getxTilePos() <= dst.getxTilePos()) {
            if (x < dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "RIGHT")) {
                    setSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20).getFxImage());
                    x += SPEED;
                }
            }
        }

        //Case 3: up
        if (src.getyTilePos() >= dst.getyTilePos()) {
            if (y > dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "UP")) {
                    setSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20).getFxImage());
                    y -= SPEED;
                }
            }
        }

        //Case 4: down
        if (src.getyTilePos() <= dst.getyTilePos()) {
            if (y < dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "DOWN")){
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
        if (onealStatus == OnealStatus.NOT_CONNECTED) {
            graph = collisionManager.getMap().convertToGraph(new Vertice(x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE));
            if (graph.isConnected()) {
                onealStatus = OnealStatus.CONNECTED;
            }
        }
        if (onealStatus != OnealStatus.CONNECTED) {
            super.move(); // If no connection to bomber then random move or has other oneal chase bomber.
        } else {
            graph = collisionManager.getMap().convertToGraph(new Vertice((x + Oneal.WIDTH / 2) / Sprite.SCALED_SIZE, (y + Oneal.HEIGHT / 2) / Sprite.SCALED_SIZE));
            path = graph.findWay(1, 0);
            moveAlongPath();
        }
    }

}
