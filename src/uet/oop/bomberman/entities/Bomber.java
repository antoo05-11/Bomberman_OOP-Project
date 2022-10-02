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
    int frame = 0;

    private static final int SPEED = 2;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }

    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (keyCode.isArrowKey()) {
            if (isPress) {
                switch (keyCode) {
                    case DOWN:
                        goDown = true;
                        break;
                    case LEFT:
                        goLeft = true;
                        break;
                    case RIGHT:
                        goRight = true;
                        break;
                    case UP:
                        goUp = true;
                        break;
                }
            } else {
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
            }
        }
    }

    public void moving() {
        if (goRight) {
            x += SPEED;
        }
        if (goLeft) {
            x -= SPEED;
        }
        if (goUp) {
            y -= SPEED;
        }
        if (goDown) {
            y += SPEED;
        }
    }

    @Override
    public void update() {
        moving();
    }
}