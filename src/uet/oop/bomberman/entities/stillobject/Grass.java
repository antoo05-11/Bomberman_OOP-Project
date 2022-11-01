package uet.oop.bomberman.entities.stillobject;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;

public class Grass extends StillObject implements CanBePassedThrough {
    /**
     * Constructor of grass.
     */
    public Grass(int x, int y, Image img) {
        super(x, y, img);
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
    }
}
