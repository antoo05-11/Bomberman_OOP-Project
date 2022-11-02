package uet.oop.bomberman.entities.movingobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.map_graph.CollisionManager;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.map_graph.Map;

import java.util.List;

public abstract class MovingObject extends Entity {
    protected MovingObjectStatus objectStatus;
    protected int indexOfSprite;
    Map map;
    CollisionManager collisionManager;

    public abstract List<Class> getCannotPassEntityList();

    public enum MovingObjectStatus {
        ALIVE,
        MORIBUND,
        DEAD
    }

    /**
     * Constructor of MovingObject.
     */
    public MovingObject(int xUnit, int yUnit, Image img, CollisionManager collisionManager) {
        super(xUnit, yUnit, img);
        this.collisionManager  = collisionManager;
        map = collisionManager.getMap();
        indexOfSprite = 0;
        objectStatus = MovingObjectStatus.ALIVE;
    }

    public void setObjectStatus(MovingObjectStatus objectStatus) {
        this.objectStatus = objectStatus;
        if (objectStatus == MovingObjectStatus.MORIBUND) indexOfSprite = 0;
    }

    public MovingObjectStatus getObjectStatus() {
        return objectStatus;
    }
}
