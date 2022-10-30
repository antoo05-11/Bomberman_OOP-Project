package uet.oop.bomberman.entities.movingobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.Entity;

public abstract class MovingObject extends Entity implements CanBePassedThrough {
    public enum MovingObjectStatus {
        ALIVE,
        MORIBUND,
        DEAD
    }

    public MovingObject(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        indexOfSprite = 0;
        objectStatus = MovingObjectStatus.ALIVE;
    }

    protected MovingObjectStatus objectStatus;
    protected int indexOfSprite;

    public void setObjectStatus(MovingObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
        if(objectStatus == MovingObjectStatus.MORIBUND) indexOfSprite = 0;
    }

    public MovingObjectStatus getObjectStatus() {
        return objectStatus;
    }
}
