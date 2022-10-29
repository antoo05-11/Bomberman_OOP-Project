package uet.oop.bomberman.scenemaster;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import uet.oop.bomberman.GameController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LobbyController extends SceneController implements Initializable {
    private Scene playingScene;

    public void setPlayingScene(Scene playingScene) {
        this.playingScene = playingScene;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void clickPlayButton(MouseEvent event) {
        GameController.gameStatus = GameController.GameStatus.GAME_START;
        stage.setScene(playingScene);
    }

    @FXML
    public void clickQuitButton(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.WARNING,
                "You really want to exit :(",
                ButtonType.YES, ButtonType.CANCEL);
        alert.setHeaderText(null);
        alert.setTitle("EXIT Notification");
        alert.setGraphic((new ImageView("lobbyTexture/quitNotification.png")));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            System.exit(0);
        }
    }

    public void clickAudioButton(MouseEvent event) {
        gameController.audioController.setMuted(!gameController.audioController.isMuted());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic((new ImageView("lobbyTexture/audioNotification.png")));
        alert.setTitle("AUDIO Notification");
        alert.setHeaderText(null);
        alert.setContentText("Audio has been turn " + (gameController.audioController.isMuted() ? "off!" : "on!"));
        alert.showAndWait();
    }

    public void clickRankButton() {

    }


}
