package uet.oop.bomberman.entities.stillobject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends StillObject implements CannotBePassedThrough {
    public enum BrickStatus {
        INTACT,
        BEING_DESTROYED,
        DISAPPEAR
    }

    BrickStatus brickStatus;
    int indexOfSprite = 0;

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
        brickStatus = BrickStatus.INTACT;
    }

    @Override
    public void update() {
        if (brickStatus == BrickStatus.BEING_DESTROYED) {
            indexOfSprite++;
            setImg(Sprite.movingSprite(Sprite.brick_exploded, Sprite.brick_exploded1, Sprite.brick_exploded2, indexOfSprite, 20));
            if (indexOfSprite == 20) {
                brickStatus = BrickStatus.DISAPPEAR;
            }
        }

    }

    public BrickStatus getBrickStatus() {
        return brickStatus;
    }

    public void setBrickStatus(BrickStatus brickStatus) {
        this.brickStatus = brickStatus;
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
}
