package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;
import uet.oop.bomberman.graphics.Sprite;

import java.util.Random;

public class Balloom extends Enemy {
    CollisionManager collisionManager;
    private int speed;
    private String dir = "";
    private boolean goNext = false;
    public Balloom(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img);
        this.collisionManager = collisionManager;
        this.speed = 1;
    }

    public void move(){
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
        if (collisionManager.collide(x, y, dir)){
            goNext = false;
        } else {
            if (dir == "LEFT") x--;
            if (dir == "RIGHT") x++;
            if (dir == "UP") y--;
            if (dir == "DOWN") y++;
            goNext = true;
        }
    }
    @Override
    public void update() {
        move();
    }
}