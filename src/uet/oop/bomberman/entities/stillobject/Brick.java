package uet.oop.bomberman.entities.stillobject;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public class Brick extends StillObject {
    int indexOfSprite = 0;

    /**
     * Constructor of brick.
     */
    public Brick(int xUnit, int yUnit, Map map) {
        super(xUnit, yUnit, Sprite.brick.getFxImage(), map);
    }

    /**
     * Destroy brick animation.
     */

    public void destroyBrick(int xTile, int yTile) {
        indexOfSprite++;
        setImg(Sprite.movingSprite(Sprite.brick_exploded,
                Sprite.brick_exploded1,
                Sprite.brick_exploded2, indexOfSprite, 40));
        if (indexOfSprite == 40) {
            indexOfSprite = 0;
            map.removeInMapInfo(yTile, xTile);
        }
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
    }

    /**
     * This is render.
     * @param gc GraphicsContext
     */
    @Override
    public void render(GraphicsContext gc) {
        super.render(gc);
    }

}
