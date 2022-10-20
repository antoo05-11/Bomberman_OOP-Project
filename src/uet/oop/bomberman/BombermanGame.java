package uet.oop.bomberman;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class BombermanGame extends Application {
    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws LineUnavailableException {
        stage.setResizable(false);
        GameController gameController = new GameController(stage);
        gameController.run();
    }
}
