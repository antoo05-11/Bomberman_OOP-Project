package uet.oop.bomberman.entities.stillobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.Entity;

public class Portal extends StillObject implements CanBePassedThrough {

    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }
}
