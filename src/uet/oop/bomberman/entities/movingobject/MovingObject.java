package uet.oop.bomberman.entities.movingobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.Entity;

public abstract class  MovingObject extends Entity implements CanBePassedThrough {
    public MovingObject(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
}
