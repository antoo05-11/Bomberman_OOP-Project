package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;

public class SpeedItem extends Item {
    public static final int code = 3;
    private final int bonus = 1;

    /**
     * Constructor of speed item.
     */
    public SpeedItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    /**
     * Update speed item.
     */
    @Override
    public void update() {
        Bomber.SPEED += bonus;
    }
}
