package uet.oop.bomberman.entities.movingobject.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.map_graph.Graph;
import uet.oop.bomberman.map_graph.Vertice;
import uet.oop.bomberman.graphics.Sprite;

import java.util.List;

public class Oneal extends Enemy implements HardEnemy {


    private List<Vertice> path;
    private Entity bomber;

    /**
     * Full constructor with param bomber for update continually position of bomberman.
     */
    public Oneal(int xUnit, int yUnit, Image img, CollisionManager collisionManager, Entity bomber) {
        super(xUnit, yUnit, img, collisionManager);
        this.bomber = bomber;
    }

    /**
     * Make oneal move along path.
     */
    public void moveAlongPath() {
        Vertice src = path.get(0);
        Vertice dst = path.get(1);
        indexOfSprite++;

        // Case 1: left
        if (src.getxTilePos() >= dst.getxTilePos()) {
            if (x > dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "LEFT", SPEED)
                        && !collisionManager.collideBomb(this, "LEFT", SPEED)) {
                    setImg(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], indexOfSprite, 20));
                    x -= SPEED;
                }
            }
        }
        // Case 2: right
        if (src.getxTilePos() <= dst.getxTilePos()) {
            if (x < dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "RIGHT", SPEED)
                        && !collisionManager.collideBomb(this, "RIGHT", SPEED)) {
                    setImg(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20));
                    x += SPEED;
                }
            }
        }

        // Case 3: up
        if (src.getyTilePos() >= dst.getyTilePos()) {
            if (y > dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "UP", SPEED)
                        && !collisionManager.collideBomb(this, "UP", SPEED)) {
                    setImg(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], indexOfSprite, 20));
                    y -= SPEED;
                }
            }
        }

        // Case 4: down
        if (src.getyTilePos() <= dst.getyTilePos()) {
            if (y < dst.getyTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(x, y, "DOWN", SPEED)
                        && !collisionManager.collideBomb(this, "DOWN", SPEED)) {
                    setImg(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], indexOfSprite, 20));
                    y += SPEED;
                }
            }
        }
    }

    /**
     * Oneal moving.
     */
    public void move() {
        int onealIndex = Graph.getVerticeIndex(x + Oneal.WIDTH / 2, y + Oneal.HEIGHT / 2);
        int bomberIndex = Graph.getVerticeIndex(bomber.getX(), bomber.getY());
        path = collisionManager.getMap().getGraph().breathFirstSearch(onealIndex, bomberIndex);
        if (path != null) {
            moveAlongPath();
        } else {
            randomMoving();
        }

    }

    /**
     * This is update.
     */
    @Override
    public void update() {
        super.update();
    }
}
