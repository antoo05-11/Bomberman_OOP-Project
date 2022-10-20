package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bombmaster.Bomb;
import uet.oop.bomberman.graphics.Sprite;

import static uet.oop.bomberman.GameController.bombsList;

public abstract class Enemy extends Entity {

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    CollisionManager collisionManager;
    protected int SPEED;
    private String dir = "";
    private boolean goNext = false;
    public int indexOfSprite;
    Sprite[] leftSprites = new Sprite[3];
    Sprite[] rightSprites = new Sprite[3];
    public enum EnemyStatus {
        ALIVE,
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
    }

    /**
     * Random moving for all enemies.
     */
    public void randomMoving() {
        if (this instanceof Balloom) {
            leftSprites[0] = Sprite.balloom_left1;
            leftSprites[1] = Sprite.balloom_left2;
            leftSprites[2] = Sprite.balloom_left3;
            rightSprites[0] = Sprite.balloom_right1;
            rightSprites[1] = Sprite.balloom_right2;
            rightSprites[2] = Sprite.balloom_right3;
        }
        if (this instanceof Oneal) {
            leftSprites[0] = Sprite.oneal_left1;
            leftSprites[1] = Sprite.oneal_left2;
            leftSprites[2] = Sprite.oneal_left3;
            rightSprites[0] = Sprite.oneal_right1;
            rightSprites[1] = Sprite.oneal_right2;
            rightSprites[2] = Sprite.oneal_right3;
        }
        indexOfSprite++;
        if (!goNext) {
            int rand = (int) (Math.random() * 8);
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
        if (collisionManager.collide(x, y, dir, SPEED)) {
            //System.out.println(this.getClass());
            goNext = false;
            indexOfSprite = 0;
        } else {
            if (dir == "LEFT") {
                x -= SPEED;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "RIGHT") {
                x += SPEED;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "UP") {
                y -= SPEED;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "DOWN") {
                y += SPEED;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            goNext = true;
        }
    }
    public abstract void move();
    private void updateEnemyStatus() {
        for (Entity i : bombsList) {
            if (((Bomb) i).insideBombRange_Pixel(x + Bomber.WIDTH / 2, y + Bomber.HEIGHT / 2)
                    && ((Bomb) i).getBombStatus() == Bomb.BombStatus.EXPLODED) {
                enemyStatus = EnemyStatus.DEAD;
            }
        }
    }
    @Override
    public void update() {
        updateEnemyStatus();
        if(enemyStatus == EnemyStatus.ALIVE) move();
    }

    @Override
    public void render(GraphicsContext gc) {
        if(enemyStatus == EnemyStatus.ALIVE) super.render(gc);
    }

    public boolean collideBomber(int xPixel, int yPixel) {
        if (xPixel + Bomber.WIDTH < x || xPixel > x + Sprite.SCALED_SIZE) return false;
        if (yPixel + Bomber.HEIGHT < y || yPixel > y + Sprite.SCALED_SIZE) return false;
        return true;
    }
}
