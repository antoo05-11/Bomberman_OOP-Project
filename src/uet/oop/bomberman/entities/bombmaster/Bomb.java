package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import uet.oop.bomberman.Map;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity {

    public enum BombStatus {
        NotExplodedYet,
        EXPLODED,
        DISAPPEAR
    }

    int waitForExplodingTime = 3;
    int indexOfSprite = 0;
    protected BombStatus bombStatus;
    protected List<Entity> flameAroundList = new ArrayList<>();
    private Map map;

    /**
     * Timer for counting 3 seconds before exploding.
     */
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        int i = 0;

        @Override
        public void run() {
            i++;
            if ((waitForExplodingTime - i) <= 0) {
                bombStatus = BombStatus.EXPLODED;
                timer.cancel();
                indexOfSprite = 0;
            }
        }
    };

    /**
     * Constructor for Bomb, run timer and add all flame sprite around.
     */
    public Bomb(int x, int y, Image img, Map map) {
        super(x, y, img);
        this.map = map;
        timer.schedule(task, 0, 1000);
        bombStatus = BombStatus.NotExplodedYet;
        flameAroundList.add(new FlameAround(x, y + 1, FlameAround.FlameType.DOWN, map));
        flameAroundList.add(new FlameAround(x, y - 1, FlameAround.FlameType.TOP, map));
        flameAroundList.add(new FlameAround(x - 1, y, FlameAround.FlameType.LEFT, map));
        flameAroundList.add(new FlameAround(x + 1, y, FlameAround.FlameType.RIGHT, map));
        flameAroundList.add(new FlameAround(x, y, FlameAround.FlameType.HORIZON, map));
        flameAroundList.add(new FlameAround(x, y, FlameAround.FlameType.VERTICAL, map));
    }

    public BombStatus getBombStatus() {
        return bombStatus;
    }

    private void setSprite(Image img) {
        this.img = img;
    }

    @Override
    public void render(GraphicsContext gc) {
        if (bombStatus == BombStatus.NotExplodedYet) super.render(gc);
        if (bombStatus == BombStatus.EXPLODED) flameAroundList.forEach(g -> g.render(gc));
    }

    @Override
    public void update() {
        if (bombStatus == BombStatus.NotExplodedYet) {
            indexOfSprite = (indexOfSprite < 1000) ? indexOfSprite + 1 : 0;
            setSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, indexOfSprite, 30).getFxImage());
        }
        if (bombStatus == BombStatus.EXPLODED) {
            bombStatus = ((FlameAround) flameAroundList.get(0)).getStatus();
            flameAroundList.forEach(Entity::update);
        }
    }
}
