package uet.oop.bomberman;

import javafx.application.Application;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;

public class BombermanGame extends Application {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws LineUnavailableException {
        GameController gameController = new GameController(stage);
        gameController.run();
    }
}
