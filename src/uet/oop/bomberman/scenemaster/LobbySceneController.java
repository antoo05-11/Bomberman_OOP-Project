package uet.oop.bomberman.scenemaster;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import uet.oop.bomberman.GameController;

public class LobbySceneController {
    @FXML
    Text NAME_TEXT;

    @FXML
    public void changeText() {
        //NAME_TEXT.setText("445");
        GameController.gameStatus = GameController.GameStatus.GAME_PLAYING;
    }
}
