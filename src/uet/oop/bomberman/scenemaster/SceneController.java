package uet.oop.bomberman.scenemaster;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.Level;
import uet.oop.bomberman.audiomaster.AudioController;

public abstract class SceneController {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 400;
    Stage stage;
    GameController gameController;

    /**
     * Set game controller.
     */
    public void setGameController(GameController gameController) {
        this.gameController = gameController;
        stage = gameController.getStage();
    }

    /**
     * Press button.
     */
    @FXML
    public void pressButton() {
        GameController.audioController.playParallel(AudioController.AudioName.CLICK_BUTTON, 1);
    }

    /**
     * Release button.
     */
    @FXML
    public void releaseButton() {

    }

    /**
     * Enter button.
     */
    @FXML
    public void enterButton(MouseEvent event) {
        Effect shadow = new Glow();
        ((Button) event.getSource()).setEffect(shadow);
        ((Button) event.getSource()).getGraphic().setScaleX(1.05);
        ((Button) event.getSource()).getGraphic().setScaleY(1.05);
    }

    /**
     * Exit button.
     */
    @FXML
    public void exitButton(MouseEvent event) {
        ((Button) event.getSource()).setEffect(null);
        ((Button) event.getSource()).getGraphic().setScaleX(1 / 1.05);
        ((Button) event.getSource()).getGraphic().setScaleY(1 / 1.05);
    }
}
