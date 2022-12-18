package uet.oop.bomberman;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.*;

public class BombermanGame extends Application {
    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }
    @Override
    public void start(Stage stage) {
        GameController gameController = new GameController(stage);

        gameController.run();
    }

}
