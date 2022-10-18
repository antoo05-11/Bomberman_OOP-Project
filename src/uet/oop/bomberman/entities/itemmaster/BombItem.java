package uet.oop.bomberman.entities.itemmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Bomber;

public class BombItem extends Item{
    public static final int code = 1;
    public BombItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Bomber.MAX_BOMB++;
    }
}
