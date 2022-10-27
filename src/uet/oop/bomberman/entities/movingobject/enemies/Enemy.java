package uet.oop.bomberman.entities.movingobject.enemies;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.entities.movingobject.MovingObject;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.GameController.bombsList;

public abstract class Enemy extends MovingObject {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;
    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    CollisionManager collisionManager;
    protected int SPEED;
    protected String dir = "";
    protected boolean changeDirection = false;
    public int indexOfSprite;
    Sprite[] leftSprites;
    Sprite[] rightSprites;
    Sprite[] deadSprites;

    public void loadSprite() {
        if (this instanceof Balloom) {
            leftSprites = new Sprite[3];
            rightSprites = new Sprite[3];
            deadSprites = new Sprite[1];
            leftSprites[0] = Sprite.balloom_left1;
            leftSprites[1] = Sprite.balloom_left2;
            leftSprites[2] = Sprite.balloom_left3;
            rightSprites[0] = Sprite.balloom_right1;
            rightSprites[1] = Sprite.balloom_right2;
            rightSprites[2] = Sprite.balloom_right3;
            deadSprites[0] = Sprite.balloom_dead;
        }
        if (this instanceof Oneal) {
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
        if (this instanceof Doll) {
            leftSprites = new Sprite[3];
            rightSprites = new Sprite[3];
            deadSprites = new Sprite[1];
            leftSprites[0] = Sprite.doll_left1;
            leftSprites[1] = Sprite.doll_left2;
            leftSprites[2] = Sprite.doll_left3;
            rightSprites[0] = Sprite.doll_right1;
            rightSprites[1] = Sprite.doll_right2;
            rightSprites[2] = Sprite.doll_right3;
            deadSprites[0] = Sprite.doll_dead;
        }
    }

    public enum EnemyStatus {
        ALIVE,
        MORIBUND,
        DEAD
    }

    public EnemyStatus getEnemyStatus() {
        return enemyStatus;
    }

    EnemyStatus enemyStatus = EnemyStatus.ALIVE;

    public Enemy(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img);
        this.collisionManager = collisionManager;
        this.SPEED = 1;
        loadSprite();
    }

    /**
     * Random moving for all enemies.
     */
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
        if (collisionManager.collide(x, y, dir, SPEED)
                || collisionManager.collidebBomb(x, y, dir, SPEED)) {
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

    public abstract void move();

    private void updateEnemyStatus() {
        for (Entity i : bombsList) {
            if (((Bomb) i).insideBombRange_Pixel(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                    && ((Bomb) i).getBombStatus() == Bomb.BombStatus.EXPLODED) {
                enemyStatus = EnemyStatus.MORIBUND;
                indexOfSprite = 0;
            }
        }
    }

    @Override
    public void update() {
        if (enemyStatus == EnemyStatus.ALIVE) {
            updateEnemyStatus();
        }
        if (enemyStatus == EnemyStatus.ALIVE) {
            move();
        }
        if (enemyStatus == EnemyStatus.MORIBUND) {
            if (indexOfSprite == 20) enemyStatus = EnemyStatus.DEAD;
            else setImg(deadSprites[indexOfSprite%deadSprites.length]);
            indexOfSprite++;
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (enemyStatus != EnemyStatus.DEAD) super.render(gc);
    }

    public boolean collideBomber(int xPixel, int yPixel) {
        if (xPixel + Bomber.WIDTH < x || xPixel > x + Sprite.SCALED_SIZE) return false;
        if (yPixel + Bomber.HEIGHT < y || yPixel > y + Sprite.SCALED_SIZE) return false;
        return true;
    }

    public void randomSpeed(int x, int y) {
        SPEED = ((int) (Math.random() * (y - x)) + x);
    }
}
