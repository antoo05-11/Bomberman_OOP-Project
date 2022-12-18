package uet.oop.bomberman;

import javafx.application.Application;
import javafx.stage.Stage;

public class BombermanGame extends Application {
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) {
        GameController gameController = GameController.getGameController(stage);
        gameController.run();
    }

}
