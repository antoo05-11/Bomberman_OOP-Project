package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;

public class Balloom extends Enemy {
    public Balloom(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img, collisionManager);
    }
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;

    @Override
    public void move() {
        randomMoving();
    }
}