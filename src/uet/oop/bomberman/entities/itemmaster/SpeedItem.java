package uet.oop.bomberman.entities.itemmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Bomber;

public class SpeedItem extends Item{
    public static final int code = 3;
    public SpeedItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Bomber.SPEED += 1;
    }
}
