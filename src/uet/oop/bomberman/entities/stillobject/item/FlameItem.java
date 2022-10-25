package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;

public class FlameItem extends Item{
    public static final int code = 2;
    private int bonus = 1;
    public FlameItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Bomber.BOMB_RADIUS += bonus;
    }
}
