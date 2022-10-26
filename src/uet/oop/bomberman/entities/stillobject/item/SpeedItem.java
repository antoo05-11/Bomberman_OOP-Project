package uet.oop.bomberman.entities.stillobject.item;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.movingobject.Bomber;

public class SpeedItem extends Item{
    public static final int code = 3;
    private int bonus = 1;
    public SpeedItem(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
        Bomber.SPEED += bonus;
    }
}
