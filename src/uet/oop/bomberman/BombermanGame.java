package uet.oop.bomberman;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BombermanGame extends Application {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        GameController gameController = new GameController(stage);
        gameController.run();
    }
}
