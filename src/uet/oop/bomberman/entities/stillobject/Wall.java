package uet.oop.bomberman.entities.stillobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CannotBePassedThrough;

public class Wall extends StillObject implements CannotBePassedThrough {
    /**
     * Constructor of wall.
     */
    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
    }
}
