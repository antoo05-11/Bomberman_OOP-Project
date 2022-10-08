package uet.oop.bomberman.entities.enemiesmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;

public abstract class Enemy extends Entity {
    public static final int HEIGHT = 30;
    public static final int WIDTH = 30;
    public Enemy(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    public void setSprite(Image img){
        this.img = img;
    }
    @Override
    public void update() {

    }
}
