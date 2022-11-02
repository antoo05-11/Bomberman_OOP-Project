package uet.oop.bomberman.entities.stillobject.bomb;

import javafx.scene.canvas.GraphicsContext;

import uet.oop.bomberman.map_graph.Map;
import uet.oop.bomberman.entities.stillobject.StillObject;
import uet.oop.bomberman.graphics.Sprite;

public class FlameAround extends StillObject {
    private int indexOfSprite = 0;

    protected enum FlameType {
        TOP,
        DOWN,
        LEFT,
        RIGHT,
        CENTER,
        HORIZON,
        VERTICAL
    }

    private FlameType type;
    private Bomb.BombStatus flameStatus;

    public Bomb.BombStatus getStatus() {
        return flameStatus;
    }

    /**
     * Constructor for flame.
     */
    public FlameAround(int xUnit, int yUnit, FlameType type, Map map) {
        super(xUnit, yUnit, null, map);
        switch (type) {
            case HORIZON:
                setImg(Sprite.explosion_horizontal);
                break;
            case VERTICAL:
                setImg(Sprite.explosion_vertical);
                break;
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
        }
        this.type = type;
        this.map = map;
        flameStatus = Bomb.BombStatus.NotExplodedYet;
    }

    /**
     * This is set flame status.
     * @param flameStatus BombStatus
     */
    public void setFlameStatus(Bomb.BombStatus flameStatus) {
        this.flameStatus = flameStatus;
    }

    /**
     * This is update.
     */
    @Override
    public void update() {
        if (flameStatus == Bomb.BombStatus.EXPLODED) {
            indexOfSprite++;
            switch (type) {
                case CENTER:
                    setImg(Sprite.movingSprite(Sprite.bomb_exploded,
                            Sprite.bomb_exploded1,
                            Sprite.bomb_exploded2,
                            indexOfSprite, 20));
                    break;
                case TOP:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                            Sprite.explosion_vertical_top_last1,
                            Sprite.explosion_vertical_top_last2,
                            indexOfSprite, 20));
                    break;
                case DOWN:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                            Sprite.explosion_vertical_down_last1,
                            Sprite.explosion_vertical_down_last2,
                            indexOfSprite, 20));
                    break;
                case RIGHT:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                            Sprite.explosion_horizontal_right_last1,
                            Sprite.explosion_horizontal_right_last2,
                            indexOfSprite, 20));
                    break;
                case LEFT:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                            Sprite.explosion_horizontal_left_last1,
                            Sprite.explosion_horizontal_left_last2,
                            indexOfSprite, 20));
                    break;

                case HORIZON:
                    setImg(Sprite.movingSprite(Sprite.explosion_horizontal,
                            Sprite.explosion_horizontal1,
                            Sprite.explosion_horizontal2,
                            indexOfSprite, 20));
                    break;
                case VERTICAL:
                    setImg(Sprite.movingSprite(Sprite.explosion_vertical,
                            Sprite.explosion_vertical1,
                            Sprite.explosion_vertical2,
                            indexOfSprite, 20));
                    break;

            }
            if (indexOfSprite == 20) {
                flameStatus = Bomb.BombStatus.DISAPPEAR;
            }
        }
    }

    /**
     * This is render.
     * @param gc GraphicsContext
     */
    @Override
    public void render(GraphicsContext gc) {
        if ((map.getEntityAt(x, y) instanceof Bomb) || map.getEntityAt(x,y) == null) {
            super.render(gc);
        }
    }
}
