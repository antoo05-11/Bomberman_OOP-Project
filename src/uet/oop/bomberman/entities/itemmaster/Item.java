package uet.oop.bomberman.entities.itemmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Item extends Entity {
    public static boolean pickUp = false;
    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }
}
