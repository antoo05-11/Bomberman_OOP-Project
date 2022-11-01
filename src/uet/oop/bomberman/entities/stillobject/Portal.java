package uet.oop.bomberman.entities.stillobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;

public class Portal extends StillObject implements CanBePassedThrough {
    public static final int code = 4;

    /**
     * Constructor of portal.
     */
    public Portal(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
    }
}
