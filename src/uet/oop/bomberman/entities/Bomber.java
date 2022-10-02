package uet.oop.bomberman.entities;

import com.sun.xml.internal.ws.policy.EffectiveAlternativeSelector;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.security.Key;

public class Bomber extends Entity {
    private KeyCode keyCode;
    boolean goLeft = false;
    boolean goRight = false;
    boolean goUp = false;
    boolean goDown = false;

    public Bomber(int x, int y, Image img) {
        super(x, y, img);
    }

    public void saveKeyEvent(KeyCode keyCode, boolean isPress) {
        if (keyCode.isArrowKey()) {
            if (isPress)
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
            }
        }
    }

    @Override
    public void update() {
        if(goRight) x++;
        if(goLeft) x--;
        if(goUp) y--;
        if(goDown) y++;
    }
}
