package uet.oop.bomberman.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.map_graph.Map;

public abstract class Entity {
    protected int x; //X coordinate from top left corner in Canvas
    protected int y; //Y coordinate from top left corner in Canvas
    protected Image img;

    /**
     * Constructor for entity.
     */
    public Entity(int xUnit, int yUnit, Image img) {
        this.x = xUnit * Sprite.SCALED_SIZE;
        this.y = yUnit * Sprite.SCALED_SIZE;
        this.img = img;
    }

    /**
     * Render entities.
     * @param gc GraphicsContext
     */
    public void render(GraphicsContext gc) {
        gc.drawImage(img, x - Map.dx_gc, y - Map.dy_gc);
    }

    public abstract void update();

    /**
     * Get x coordinate.
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * Get y coordinate.
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Set image.
     * @param sprite Sprite
     */
    public void setImg(Sprite sprite) {
        this.img = sprite.getFxImage();
    }
}
