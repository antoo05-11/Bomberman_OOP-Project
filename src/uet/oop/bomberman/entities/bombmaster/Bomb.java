package uet.oop.bomberman.entities.bombmaster;

import javafx.scene.image.Image;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.Sprite;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity {
    private enum BombStatus {
        NotExplodedYet,
        EXPLODED,
        DISAPPEAR
    }
    int waitForExplodingTime = 3;
    int indexOfSprite = 0;
    protected BombStatus bombStatus;
    protected List<Entity> flameAroundList = new ArrayList<>();
    Timer timer = new Timer(); //timer for counting 3 seconds before exploding
    TimerTask task = new TimerTask()
    {
        int i = 0;
        @Override
        public void run()
        {
            i++;
            System.out.println(i + " " + waitForExplodingTime);
            if((waitForExplodingTime - i)  <= 0) {
                bombStatus = BombStatus.EXPLODED;
                task.cancel();
                timer.cancel();
            };
        }
    };
    public Bomb(int x, int y, Image img) {
        super(x, y, img);
        timer.schedule(task, 0, 1000);
        bombStatus = BombStatus.NotExplodedYet;
        Entity flameHorizonTop = new FlameAround(x,y, Sprite.explosion_vertical_top_last.getFxImage());
        flameAroundList.add(flameHorizonTop);

    }
    private void setSprite(Image img) {
        this.img = img;
    }
    @Override
    public void update() {
        if(bombStatus == BombStatus.NotExplodedYet) {
            indexOfSprite ++;
            setSprite(Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, indexOfSprite, 30).getFxImage());
        }
        if(bombStatus == BombStatus.EXPLODED) {

        }
        if(bombStatus == BombStatus.DISAPPEAR) {

        }
    }
}
