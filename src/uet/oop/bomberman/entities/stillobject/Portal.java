package uet.oop.bomberman.entities.stillobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Portal extends StillObject implements CanBePassedThrough {
    public static final int code = 4;
    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }
}
