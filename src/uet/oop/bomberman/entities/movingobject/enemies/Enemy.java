package uet.oop.bomberman.entities.movingobject.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.movingobject.MovingObject;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Enemy extends MovingObject {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    CollisionManager collisionManager;
    protected int SPEED;
    protected String dir = "";
    protected boolean changeDirection = false;
    Sprite[] leftSprites;
    Sprite[] rightSprites;
    Sprite[] deadSprites;

    /**
     * Load sprite of types of enemies into sprites lists.
     */
    public abstract void loadSprite();

    /**
     * Constructor for enemy.
     */
    public Enemy(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img, collisionManager);
        this.collisionManager = collisionManager;
        this.SPEED = 1;
        loadSprite();
        objectStatus = MovingObjectStatus.ALIVE;
    }

    /**
     * Random moving for all enemies satisfying condition: not colliding with still object.
     */
    public void randomMovingWhenCollidingWithWall() {
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
        if (collisionManager.collide(this, x, y, dir, SPEED)) {
            changeDirection = false;
            indexOfSprite = 0;
        } else {
            if (dir.equals("LEFT")) {
                x -= SPEED;
                setImg(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20));
            }
            if (dir.equals("RIGHT")) {
                x += SPEED;
                setImg(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20));
            }
            if (dir.equals("UP")) {
                y -= SPEED;
                setImg(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20));
            }
            if (dir.equals("DOWN")) {
                y += SPEED;
                setImg(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20));
            }
            changeDirection = true;
        }
    }

    /**
     * Update path to go for enemy, implemented by completing a specific path.
     */
    public abstract void move();

    @Override
    public void update() {
        if (objectStatus == MovingObjectStatus.ALIVE) {
            move();
        }
        if (objectStatus == MovingObjectStatus.MORIBUND) {
            if (indexOfSprite == 20) objectStatus = MovingObjectStatus.DEAD;
            else setImg(deadSprites[indexOfSprite % deadSprites.length]);
            indexOfSprite++;
        }
    }

    /**
     * Only render when enemy status is DEAD.
     */
    @Override
    public void render(GraphicsContext gc) {
        if (objectStatus != MovingObjectStatus.DEAD) super.render(gc);
    }

    /**
     * Check if enemy collide with bomber.
     *
     * @param xPixel int
     * @param yPixel int
     * @return true/false
     */
    public boolean collideBomber(int xPixel, int yPixel) {
        return yPixel + Bomber.HEIGHT - Sprite.SCALED_SIZE / 10 >= y
                && yPixel + Sprite.SCALED_SIZE / 10 <= y + Enemy.HEIGHT
                && xPixel + Bomber.WIDTH - Sprite.SCALED_SIZE / 10 >= x
                && xPixel + Sprite.SCALED_SIZE / 10 <= x + Enemy.WIDTH;
    }

    /**
     * Get points from enemy type.
     *
     * @return Reward point for types of dead enemies.
     */
    public int getRewardPoint() {
        if (this instanceof EasyEnemy) return EasyEnemy.rewardPoint;
        else if (this instanceof MediumEnemy) return MediumEnemy.rewardPoint;
        else if (this instanceof HardEnemy) return HardEnemy.rewardPoint;
        return 0;
    }

    /**
     * Random new speed for enemy.
     *
     * @param x is lower bound.
     * @param y is upper bound.
     */
    public void randomSpeed(int x, int y) {
        SPEED = ((int) (Math.random() * (y - x)) + x);
    }
}
