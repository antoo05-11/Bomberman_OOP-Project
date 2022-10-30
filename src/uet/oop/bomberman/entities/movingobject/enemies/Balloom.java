package uet.oop.bomberman.entities.movingobject.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.CollisionManager;

public class Balloom extends Enemy implements EasyEnemy {
    public Balloom(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img, collisionManager);
    }

    @Override
    public void move() {
        randomMoving();
    }
}