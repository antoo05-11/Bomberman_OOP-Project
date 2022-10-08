package uet.oop.bomberman.entities.bombmaster;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.Map;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.stillobjectmaster.Grass;
import uet.oop.bomberman.graphics.Sprite;

public class FlameAround extends Entity {

    int indexOfSprite = 0;

    enum FlameType {
        TOP,
        DOWN,
        LEFT,
        RIGHT,
        CENTER,
        HORIZON,
        VERTICAL
    }

    FlameType type;
    private Bomb.BombStatus status;
    private Map map; //for render flame not in wall.

    public Bomb.BombStatus getStatus() {
        return status;
    }

    public FlameAround(int xUnit, int yUnit, FlameType type, Map map) {
        super(xUnit, yUnit, null);
        switch (type) {
            case DOWN:
                img = Sprite.explosion_vertical_down_last.getFxImage();
                break;
            case TOP:
                img = Sprite.explosion_vertical_top_last.getFxImage();
                break;
            case RIGHT:
                img = Sprite.explosion_horizontal_right_last.getFxImage();
                break;
            case LEFT:
                img = Sprite.explosion_horizontal_left_last.getFxImage();
                break;
            case CENTER:
                img = Sprite.bomb_exploded2.getFxImage();
                break;
            case HORIZON:
                img = Sprite.explosion_horizontal.getFxImage();
                break;
            case VERTICAL:
                img = Sprite.explosion_vertical.getFxImage();
                break;
        }

        this.type = type;
        this.map = map;
        status = Bomb.BombStatus.EXPLODED;
    }

    public void setSprite(Image img) {
        this.img = img;
    }

    @Override
    public void update() {
        indexOfSprite++;
        switch (type) {
            case LEFT:
                setSprite(Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                        Sprite.explosion_horizontal_left_last1,
                        Sprite.explosion_horizontal_left_last2,
                        indexOfSprite, 20).getFxImage());
                break;
            case RIGHT:
                setSprite(Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                        Sprite.explosion_horizontal_right_last1,
                        Sprite.explosion_horizontal_right_last2,
                        indexOfSprite, 20).getFxImage());
                break;
            case TOP:
                setSprite(Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                        Sprite.explosion_vertical_top_last1,
                        Sprite.explosion_vertical_top_last2,
                        indexOfSprite, 20).getFxImage());
                break;
            case DOWN:
                setSprite(Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                        Sprite.explosion_vertical_down_last1,
                        Sprite.explosion_vertical_down_last2,
                        indexOfSprite, 20).getFxImage());
                break;
            case HORIZON:
                setSprite(Sprite.movingSprite(Sprite.explosion_horizontal,
                        Sprite.explosion_horizontal1,
                        Sprite.explosion_horizontal2,
                        indexOfSprite, 20).getFxImage());
                break;
            case VERTICAL:
                setSprite(Sprite.movingSprite(Sprite.explosion_vertical,
                        Sprite.explosion_vertical1,
                        Sprite.explosion_vertical2,
                        indexOfSprite, 20).getFxImage());
                break;
            case CENTER:
                setSprite(Sprite.movingSprite(Sprite.bomb_exploded,
                        Sprite.bomb_exploded1,
                        Sprite.bomb_exploded2,
                        indexOfSprite, 20).getFxImage());
                break;
        }
        if (indexOfSprite == 20) status = Bomb.BombStatus.DISAPPEAR;
    }

    @Override
    public void render(GraphicsContext gc) {
        //Check if the flame is in wall.
        if (map.getEntityAt(x, y) instanceof Grass) {
            super.render(gc);
        }
    }
}
