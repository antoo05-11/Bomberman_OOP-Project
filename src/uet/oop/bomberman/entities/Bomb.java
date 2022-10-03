package uet.oop.bomberman.entities;

import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import uet.oop.bomberman.graphics.Sprite;

import javax.swing.*;
import java.io.NotActiveException;
import java.util.Timer;
import java.util.TimerTask;

public class Bomb extends Entity {
    private enum BombStatus {
        NotExplodedYet,
        EXPLODED,
        EXIT
    }
    int waitForExplodingTime = 3;
    int indexOfSprite = 0;
    protected BombStatus bombStatus;
    TimerTask task = new TimerTask()
    {
        int i = 0;
        @Override
        public void run()
        {
            i++;
            if(i % waitForExplodingTime == 0)
                System.out.println("Timer action!");
            else
                System.out.println("Time left:" + (waitForExplodingTime - (i %waitForExplodingTime)) );
        }
    };

    Timer timer = new Timer();



    public Bomb(int x, int y, Image img) {
        super(x, y, img);
        timer.schedule(task, 0, 1000);
        bombStatus = BombStatus.NotExplodedYet;
    }
    private void setSprite(Image img) {
        this.img = img;
    }
    @Override
    public void update() {
        if(bombStatus == BombStatus.NotExplodedYet) {

            indexOfSprite ++;
            if(indexOfSprite > 2) indexOfSprite = 0;
            if(indexOfSprite == 0) setSprite(Sprite.bomb.getFxImage());
            else if(indexOfSprite == 1) setSprite(Sprite.bomb_1.getFxImage());
            else setSprite(Sprite.bomb_2.getFxImage());
        }
    }
}
