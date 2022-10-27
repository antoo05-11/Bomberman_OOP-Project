package uet.oop.bomberman.entities.stillobject.bomb;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.CannotBePassedThrough;
import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.entities.CanBePassedThrough;
import uet.oop.bomberman.entities.stillobject.Grass;
import uet.oop.bomberman.entities.stillobject.StillObject;
import uet.oop.bomberman.graphics.Sprite;

public class FlameAround extends StillObject implements CanBePassedThrough {
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
    private Bomb.BombStatus flameStatus;
    private Map map; //for render flame not in wall.

    public Bomb.BombStatus getStatus() {
        return flameStatus;
    }

    public FlameAround(int xUnit, int yUnit, FlameType type, Map map) {
        super(xUnit, yUnit, null);
        switch (type) {
            case DOWN:
                setImg(Sprite.explosion_vertical_down_last);
                break;
            case TOP:
                setImg(Sprite.explosion_vertical_top_last);
                break;
            case RIGHT:
                setImg(Sprite.explosion_horizontal_right_last);
                break;
            case LEFT:
                setImg(Sprite.explosion_horizontal_left_last);
                break;
            case CENTER:
                setImg(Sprite.bomb_exploded2);
                break;
            case HORIZON:
                setImg(Sprite.explosion_horizontal);
                break;
            case VERTICAL:
                setImg(Sprite.explosion_vertical);
                break;
        }

        this.type = type;
        this.map = map;
        flameStatus = Bomb.BombStatus.NotExplodedYet;
    }

    public void setFlameStatus(Bomb.BombStatus flameStatus) {
        this.flameStatus = flameStatus;
    }

    @Override
    public void update() {
        if (flameStatus == Bomb.BombStatus.EXPLODED) {
            indexOfSprite++;
            switch (type) {
                case LEFT:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                            Sprite.explosion_horizontal_left_last1,
                            Sprite.explosion_horizontal_left_last2,
                            indexOfSprite, 30));
                    break;
                case RIGHT:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                            Sprite.explosion_horizontal_right_last1,
                            Sprite.explosion_horizontal_right_last2,
                            indexOfSprite, 30));
                    break;
                case TOP:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                            Sprite.explosion_vertical_top_last1,
                            Sprite.explosion_vertical_top_last2,
                            indexOfSprite, 30));
                    break;
                case DOWN:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                            Sprite.explosion_vertical_down_last1,
                            Sprite.explosion_vertical_down_last2,
                            indexOfSprite, 30));
                    break;
                case HORIZON:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal,
                            Sprite.explosion_horizontal1,
                            Sprite.explosion_horizontal2,
                            indexOfSprite, 30));
                    break;
                case VERTICAL:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical,
                            Sprite.explosion_vertical1,
                            Sprite.explosion_vertical2,
                            indexOfSprite, 30));
                    break;
                case CENTER:
                    setImg(Sprite.movingSprite(Sprite.bomb_exploded,
                            Sprite.bomb_exploded1,
                            Sprite.bomb_exploded2,
                            indexOfSprite, 30));
                    break;
            }
            if (indexOfSprite == 30) {
                flameStatus = Bomb.BombStatus.DISAPPEAR;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        if (!(map.getEntityAt(x, y) instanceof CannotBePassedThrough)) {
            super.render(gc);
        }
    }
}
