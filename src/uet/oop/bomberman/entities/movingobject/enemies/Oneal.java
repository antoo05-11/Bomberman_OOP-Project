package uet.oop.bomberman.entities.movingobject.enemies;

import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Graph;
import uet.oop.bomberman.map_graph.Vertice;

import java.util.Arrays;
import java.util.List;

public class Oneal extends Enemy implements HardEnemy {
    public static final List<Class> cannotPassEntityList = Arrays.asList(new Class[]{Wall.class, Bomb.class, Brick.class});
    private Graph graph;

    /**
     * Full constructor with param bomber for update continually position of bomberman.
     */
    public Oneal(int xUnit, int yUnit, CollisionManager collisionManager) {
        super(xUnit, yUnit, Sprite.oneal_right1.getFxImage(), collisionManager);
    }

    @Override
    public void loadSprite() {
        leftSprites = new Sprite[3];
        rightSprites = new Sprite[3];
        deadSprites = new Sprite[1];
        leftSprites[0] = Sprite.oneal_left1;
        leftSprites[1] = Sprite.oneal_left2;
        leftSprites[2] = Sprite.oneal_left3;
        rightSprites[0] = Sprite.oneal_right1;
        rightSprites[1] = Sprite.oneal_right2;
        rightSprites[2] = Sprite.oneal_right3;
        deadSprites[0] = Sprite.oneal_dead;
    }

    /**
     * Make oneal move along path.
     */
    public void moveAlongPath(List<Vertice> path) {
        Vertice src = path.get(0);
        Vertice dst = path.get(1);
        indexOfSprite++;

        // Case 1: left
        if (src.getxTilePos() >= dst.getxTilePos()) {
            if (x > dst.getxTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.collide(this, x, y, "LEFT", SPEED)) {
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
                if (!collisionManager.collide(this, x, y, "RIGHT", SPEED)) {
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
                if (!collisionManager.collide(this, x, y, "UP", SPEED)) {
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
                if (!collisionManager.collide(this, x, y, "DOWN", SPEED)) {
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
        graph = collisionManager.getMap().convertMapToGraph(new Vertice(x / Sprite.SCALED_SIZE, y / Sprite.SCALED_SIZE));
        if (!graph.isConnected()) {
            randomMovingWhenCollidingWithWall();
        } else {
            moveAlongPath(graph.findWay(1, 0));
        }
    }

    @Override
    public List<Class> getCannotPassEntityList() {
        return cannotPassEntityList;
    }
}
