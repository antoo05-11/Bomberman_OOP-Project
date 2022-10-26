package uet.oop.bomberman.scenemaster;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import uet.oop.bomberman.GameController;

public abstract class SceneController {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 400;
    Stage stage;

    GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        stage = gameController.getStage();
    }
}
