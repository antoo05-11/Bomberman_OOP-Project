package uet.oop.bomberman.entities.stillobject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.graphics.Sprite;

public class Brick extends StillObject implements CannotBePassedThrough {
    int indexOfSprite = 0;

    public Brick(int xUnit, int yUnit, Image img) {
        super(xUnit, yUnit, img);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }
    public void destroyBrick(int xTile, int yTile){
        indexOfSprite++;
        setImg(Sprite.movingSprite(Sprite.brick_exploded,
                Sprite.brick_exploded1,
                Sprite.brick_exploded2, indexOfSprite, 40));
        if (indexOfSprite == 40){
            indexOfSprite = 0;
            GameController.mapList.get(GameController.LEVEL).replace(yTile, xTile, null);
        }
    }
}
