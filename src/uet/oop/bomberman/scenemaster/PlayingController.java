package uet.oop.bomberman.scenemaster;

import javafx.animation.*;
import javafx.event.ActionEvent;
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
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.movingobject.Bomber;

import java.net.URL;
import java.sql.Time;
import java.util.Optional;
import java.util.ResourceBundle;

import static uet.oop.bomberman.GameController.GameStatus.GAME_PAUSE;
import static uet.oop.bomberman.GameController.GameStatus.GAME_PLAYING;

public class PlayingController extends SceneController implements Initializable {
    @FXML
    private Text levelText;
    @FXML
    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @FXML
    private Button audioButton;
    @FXML
    private Line muteLine;
    @FXML
    private Button pauseButton;
    @FXML
    private Button quitButton;
    @FXML
    private StackPane root;
    @FXML
    private Button continueButton;
    private Scene lobbyScene;
    private FadeTransition ft = new FadeTransition();
    @FXML
    private VBox nextLevelBox;
    @FXML
    private Text stageText;
    public VBox getNextLevelBox() {
        return nextLevelBox;
    }


    @FXML
    Text maxBombs;
    Timeline timeline;

    @FXML
    private HBox livesImg;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL playingURL = BombermanGame.class.getResource("/UI_fxml/PlayingScene.fxml");
        if (location.equals(playingURL)) {
            //Add playing canvas from gameController
            ((VBox) (((StackPane) root).getChildren().get(0))).getChildren().add(GameController.playingCanvas);

            levelText.setText("LEVEL " + gameController.LEVEL);

            // Set status of mute line for audio button.
            muteLine.setVisible(GameController.audioController.isMuted());

            //Click pause button
            pauseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameController.gameStatus = GAME_PAUSE;
                    root.getChildren().get(0).setEffect(new BoxBlur());
                    root.getChildren().get(0).setDisable(true);
                    continueButton.setVisible(true);
                    continueButton.setDisable(false);
                }
            });
            //Click continue button.
            continueButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    root.getChildren().get(0).setEffect(null);
                    root.getChildren().get(0).setDisable(false);
                    continueButton.setVisible(false);
                    continueButton.setDisable(true);
                    GameController.gameStatus = GAME_PLAYING;
                }
            });
            // Click audio button.
            audioButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameController.audioController.setMuted(!GameController.audioController.isMuted());
                    muteLine.setVisible(GameController.audioController.isMuted());
                }
            });
        }
    }

    @FXML
    public void clickQuitButton(MouseEvent event) {
        GameController.gameStatus = GAME_PAUSE;
        Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to quit?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get() == ButtonType.YES) {
            GameController.gameStatus = GameController.GameStatus.GAME_LOBBY;
            stage.setScene(lobbyScene);
        } else GameController.gameStatus = GAME_PLAYING;
    }

    public void updateStatus() {
        stageText.setText("STAGE " + (GameController.LEVEL + 1));
        levelText.setText("STAGE " + (GameController.LEVEL + 1));
        maxBombs.setText(Integer.toString(gameController.getMaxBombs()));
        for(int i = 0; i < gameController.getNumOfLives(); i++)
        livesImg.getChildren().get(i).setVisible(true);
        for(int i = gameController.getNumOfLives(); i < 3; i++) {
            livesImg.getChildren().get(i).setVisible(false);
        }
    }

    public void setLobbyScene(Scene lobbyScene) {
        this.lobbyScene = lobbyScene;
    }
}
