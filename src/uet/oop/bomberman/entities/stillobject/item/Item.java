package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.stillobject.StillObject;
import uet.oop.bomberman.graphics.Sprite;

public abstract class Item extends StillObject implements CanBePassedThrough {
    /**
     * Constructor for item.
     */
    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Update items.
     */
    @Override
    public void update() {

    }
}
