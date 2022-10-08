package uet.oop.bomberman.scenemaster;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.Entity;

import java.util.ArrayList;
import java.util.List;

public class PlayScene extends RootScene {
    public PlayScene() {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), true);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), false);
            }
        });
    }


}
