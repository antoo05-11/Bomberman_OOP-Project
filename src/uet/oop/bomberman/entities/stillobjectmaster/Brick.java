package uet.oop.bomberman.entities.stillobjectmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends Entity implements StillObjects{
    int indexOfSprite = 0;
    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
    }

    public void destroyBrick(int xTile, int yTile){
        indexOfSprite++;
        setSprite(Sprite.movingSprite(Sprite.brick_exploded,
                Sprite.brick_exploded1,
                Sprite.brick_exploded2, indexOfSprite, 40).getFxImage());
        if (indexOfSprite == 40){
            GameController.mapList.get(GameController.LEVEL).replace(yTile, xTile, new Grass(xTile, yTile, Sprite.grass.getFxImage()));
        }
    }
}
