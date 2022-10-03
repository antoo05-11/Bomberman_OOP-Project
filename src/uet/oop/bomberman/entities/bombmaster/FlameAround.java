package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class FlameAround extends Entity {

    public FlameAround(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }
    public void setSprite(Image img) {
        this.img = img;
    }
    public Image getSprite() {
        return img;
    }
    @Override
    public void update() {

    }
}
