package uet.oop.bomberman.entities;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import uet.oop.bomberman.graphics.Sprite;

import java.security.Key;

public class Bomber extends Entity {
    private KeyCode keyCode;
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;

    int indexOfSprite = 0;

    private static final int SPEED = 2;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }
    public void setSprite(Image img) {
        this.img = img;
    }
    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (keyCode.isArrowKey()) {
            if (isPress) {
                indexOfSprite++;
                if(indexOfSprite > 2) indexOfSprite = 0;
                switch (keyCode) {
                    case DOWN:
                        goDown = true;
                        if(indexOfSprite == 0) setSprite(Sprite.player_down.getFxImage());
                        else if(indexOfSprite == 1) setSprite(Sprite.player_down_1.getFxImage());
                        else if(indexOfSprite == 2) setSprite(Sprite.player_down_2.getFxImage());
                            break;
                    case LEFT:
                        goLeft = true;
                        if(indexOfSprite == 0) setSprite(Sprite.player_left.getFxImage());
                        else if(indexOfSprite == 1) setSprite(Sprite.player_left_1.getFxImage());
                        else if(indexOfSprite == 2) setSprite(Sprite.player_left_2.getFxImage());
                        break;
                    case RIGHT:
                        goRight = true;
                        if(indexOfSprite == 0) setSprite(Sprite.player_right.getFxImage());
                        else if(indexOfSprite == 1) setSprite(Sprite.player_right_1.getFxImage());
                        else if(indexOfSprite == 2) setSprite(Sprite.player_right_2.getFxImage());
                        break;
                    case UP:
                        goUp = true;
                        if(indexOfSprite == 0) setSprite(Sprite.player_up.getFxImage());
                        else if(indexOfSprite == 1) setSprite(Sprite.player_up_1.getFxImage());
                        else if(indexOfSprite == 2) setSprite(Sprite.player_up_2.getFxImage());
                        break;
                }
            }
            else {
                switch (keyCode) {
                    case DOWN:
                        goDown = false;
                        break;
                    case LEFT:
                        goLeft = false;
                        break;
                    case RIGHT:
                        goRight = false;
                        break;
                    case UP:
                        goUp = false;
                        break;
                }
                indexOfSprite = 0;
            }
        }
    }
    public void moving() {
        if (goRight) x += SPEED;
        if (goLeft) x -= SPEED;
        if (goUp) y -= SPEED;
        if (goDown) y += SPEED;
    }
    @Override
    public void update() {
        moving();
    }
}
