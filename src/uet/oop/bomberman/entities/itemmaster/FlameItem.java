package uet.oop.bomberman.entities.itemmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Bomber;

public class FlameItem extends Item{
    public static final int code = 2;
    public FlameItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Bomber.BOMB_RADIUS += 1;
    }
}
