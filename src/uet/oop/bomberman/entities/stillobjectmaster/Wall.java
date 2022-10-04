package uet.oop.bomberman.entities.stillobjectmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public class Wall extends Entity implements StillObjects{

    public Wall(int x, int y, Image img) {
        super(x, y, img);
    }

    @Override
    public void update() {

    }
}
