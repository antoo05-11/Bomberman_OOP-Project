package uet.oop.bomberman.scenemaster;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static uet.oop.bomberman.GameController.GameStatus.GAME_PAUSE;
import static uet.oop.bomberman.GameController.GameStatus.GAME_PLAYING;
public class PlayingController implements SceneController, Initializable {
    @FXML
    private Text levelText;
    @FXML
    private ProgressBar progressBar;
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
    private Scene scene;
    private Stage stage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL playingURL;
        playingURL = BombermanGame.class.getResource("/UI_fxml/PlayingScene.fxml");
        if (location.equals(playingURL)) {
            // Set level text.
            levelText.setText("LEVEL " + (GameController.LEVEL + 1));

            // Set status of mute line for audio button.
            muteLine.setVisible(GameController.audioController.isMuted());

            // Set status for progress bar.
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                    new KeyFrame(Duration.minutes(1), e -> {
                        //time out
                    }, new KeyValue(progressBar.progressProperty(), 1))
            );
            timeline.play();

            //Click pause button
            pauseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameController.gameStatus = GAME_PAUSE;
                    root.getChildren().get(0).setEffect(new BoxBlur());
                    continueButton.setVisible(true);
                    continueButton.setDisable(false);
                }
            });
            //Click continue button.
            continueButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    root.getChildren().get(0).setEffect(null);
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
            URL url = BombermanGame.class.getResource("/UI_fxml/LobbyScene.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            stage = (Stage) (((Node) (event.getSource())).getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        }
        else GameController.gameStatus = GAME_PLAYING;
    }
}
