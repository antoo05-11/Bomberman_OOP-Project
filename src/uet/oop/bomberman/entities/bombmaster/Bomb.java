package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.image.Image;
import jdk.net.SocketFlow;
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
    public Bomb(int x, int y, Image img) {
        super(x, y, img);
        timer.schedule(task, 0, 1000);
        bombStatus = BombStatus.NotExplodedYet;
        flameAroundList.add(new FlameAround(x, y + 1, Sprite.explosion_vertical_down_last.getFxImage()));
        flameAroundList.add(new FlameAround(x, y - 1, Sprite.explosion_vertical_top_last.getFxImage()));
        flameAroundList.add(new FlameAround(x - 1, y, Sprite.explosion_horizontal_left_last.getFxImage()));
        flameAroundList.add(new FlameAround(x + 1, y, Sprite.explosion_horizontal_right_last.getFxImage()));
        flameAroundList.add(new FlameAround(x, y, Sprite.explosion_horizontal.getFxImage()));
        flameAroundList.add(new FlameAround(x, y, Sprite.explosion_vertical.getFxImage()));
    }

    public BombStatus getBombStatus() {
        return bombStatus;
    }
    private void setSprite(Image img) {
        this.img = img;
    }

    @Override
    public void update() {
        if (bombStatus == BombStatus.NotExplodedYet) {
            indexOfSprite = (indexOfSprite < 1000) ? indexOfSprite + 1 : 0;
            setSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, indexOfSprite, 30).getFxImage());
        }

        if (bombStatus == BombStatus.EXPLODED) {
            indexOfSprite = (indexOfSprite < 1000) ? indexOfSprite + 1 : 0;
            System.out.println(indexOfSprite);
            ((FlameAround) flameAroundList.get(0)).setSprite(Sprite.movingSprite(Sprite.explosion_vertical_down_last,
                    Sprite.explosion_vertical_down_last1,
                    Sprite.explosion_vertical_down_last2,
                    indexOfSprite, 30).getFxImage());
            ((FlameAround) flameAroundList.get(1)).setSprite(Sprite.movingSprite(Sprite.explosion_vertical_top_last,
                    Sprite.explosion_vertical_top_last1,
                    Sprite.explosion_vertical_top_last2,
                    indexOfSprite, 30).getFxImage());
            ((FlameAround) flameAroundList.get(2)).setSprite(Sprite.movingSprite(Sprite.explosion_horizontal_left_last,
                    Sprite.explosion_horizontal_left_last1,
                    Sprite.explosion_horizontal_left_last2,
                    indexOfSprite, 30).getFxImage());
            ((FlameAround) flameAroundList.get(3)).setSprite(Sprite.movingSprite(Sprite.explosion_horizontal_right_last,
                    Sprite.explosion_horizontal_right_last1,
                    Sprite.explosion_horizontal_right_last2,
                    indexOfSprite, 30).getFxImage());
            ((FlameAround) flameAroundList.get(4)).setSprite(Sprite.movingSprite(Sprite.explosion_horizontal,
                    Sprite.explosion_horizontal1,
                    Sprite.explosion_horizontal2,
                    indexOfSprite, 30).getFxImage());
            ((FlameAround) flameAroundList.get(5)).setSprite(Sprite.movingSprite(Sprite.explosion_vertical,
                    Sprite.explosion_vertical1,
                    Sprite.explosion_vertical2,
                    indexOfSprite, 30).getFxImage());
            if (indexOfSprite == 15) {
                bombStatus = BombStatus.DISAPPEAR;
            }
                flameAroundList.forEach(g -> g.render(gc));
        }
    }

}
