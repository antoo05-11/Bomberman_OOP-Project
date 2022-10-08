package uet.oop.bomberman.scenemaster;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class LobbyScene extends RootScene{
    Button playButton = new Button("PLAY");
    StackPane layout = new StackPane(playButton);
    public LobbyScene() {
        scene = new Scene(layout, BombermanGame.WIDTH * Sprite.SCALED_SIZE, BombermanGame.HEIGHT * Sprite.SCALED_SIZE);
        playButton.setOnAction(event -> {
            GameController.gameStatus = GameController.GameStatus.GAME_PLAYING;
        });
    }
}
