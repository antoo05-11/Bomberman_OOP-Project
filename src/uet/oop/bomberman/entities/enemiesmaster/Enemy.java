package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Enemy extends Entity {

    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    CollisionManager collisionManager;
    private int speed;
    private String dir = "";
    private boolean goNext = false;
    public int indexOfSprite;
    Sprite[] leftSprites = new Sprite[3];
    Sprite[] rightSprites = new Sprite[3];

    public Enemy(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img);
        this.collisionManager = collisionManager;
        this.speed = 1;
    }

    /**
     * Random moving for all enemies.
     */
    public void move() {
        if(this instanceof Balloom) {
            leftSprites[0] = Sprite.balloom_left1;
            leftSprites[1] = Sprite.balloom_left2;
            leftSprites[2] = Sprite.balloom_left3;
            rightSprites[0] = Sprite.balloom_right1;
            rightSprites[1] = Sprite.balloom_right2;
            rightSprites[2] = Sprite.balloom_right3;
        }
        if(this instanceof Oneal) {
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
        if (collisionManager.collide(x, y, dir)) {
            //System.out.println(this.getClass());
            goNext = false;
            indexOfSprite = 0;
        } else {
            if (dir == "LEFT") {
                x--;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "RIGHT") {
                x++;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "UP") {
                y--;
                setSprite(Sprite.movingSprite(
                        rightSprites[0],
                        rightSprites[1],
                        rightSprites[2], indexOfSprite, 20).getFxImage());
            }
            if (dir == "DOWN") {
                y++;
                setSprite(Sprite.movingSprite(
                        leftSprites[0],
                        leftSprites[1],
                        leftSprites[2], indexOfSprite, 20).getFxImage());
            }
            goNext = true;
        }
    }

    @Override
    public void update() {
        move();
    }
}
