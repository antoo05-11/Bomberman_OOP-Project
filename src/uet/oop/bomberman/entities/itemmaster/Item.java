package uet.oop.bomberman.entities.itemmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Item extends Entity {
    public static boolean pickUp = false;
    public Item(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {

    }
    public boolean insideItem_Pixel(int xPos, int yPos) {
        int xTile = xPos / Sprite.SCALED_SIZE;
        int yTile = yPos / Sprite.SCALED_SIZE;
        int xItemTile = x / Sprite.SCALED_SIZE;
        int yItemTile = y / Sprite.SCALED_SIZE;
        if(xTile == xItemTile && yTile == yItemTile) return true;
        return false;
    }
}
