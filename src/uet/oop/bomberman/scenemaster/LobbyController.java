package uet.oop.bomberman.scenemaster;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.audiomaster.AudioController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class LobbyController extends SceneController implements Initializable {
    @FXML
    private Button playButton;
    @FXML
    private Button audioButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button rankButton;

    private Scene playingScene;

    public void setPlayingScene(Scene playingScene) {
        this.playingScene = playingScene;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void setEffect(MouseEvent event) {
        Effect shadow = new Glow();
        ((Button) event.getSource()).setEffect(shadow);
    }

    @FXML
    public void removeEffect(MouseEvent event) {
        ((Button) event.getSource()).setEffect(null);
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

    @FXML
    public void pressButton() {
        GameController.audioController.playParallel(AudioController.AudioName.CLICK_BUTTON, 1);
    }
    @FXML
    public void releaseButton() {

    }

    public void clickAudioButton(MouseEvent event) {
        GameController.audioController.setMuted(!GameController.audioController.isMuted());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic((new ImageView("lobbyTexture/audioNotification.png")));
        alert.setTitle("AUDIO Notification");
        alert.setHeaderText(null);
        alert.setContentText("Audio has been turn " + (GameController.audioController.isMuted() ? "off!" : "on!"));
        alert.showAndWait();
    }

    public void clickRankButton() {

    }


}
