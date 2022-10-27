package uet.oop.bomberman.scenemaster;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.movingobject.Bomber;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static uet.oop.bomberman.GameController.GameStatus.GAME_PAUSE;
import static uet.oop.bomberman.GameController.GameStatus.GAME_PLAYING;

public class PlayingController extends SceneController implements Initializable {
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
    private Scene lobbyScene;
    @FXML
    private VBox nextLevelBox;
    @FXML
    private Text stageText;
    @FXML
    VBox playingBox;
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
            playingBox.getChildren().add(gameController.playingCanvas);

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
            playingBox.getChildren().get(1).setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    ((Bomber)GameController.entities.get(GameController.LEVEL).get(0)).saveKeyEvent(event.getCode(), true);
                }
            });
            playingBox.getChildren().get(1).setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    ((Bomber)GameController.entities.get(GameController.LEVEL).get(0)).saveKeyEvent(event.getCode(), false);
                }
            });
            playingBox.getChildren().get(1).setFocusTraversable(true);
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

    /**
     * Start timer for game playing.
     */
    private void setUpTimeline() {
        if (timeline != null) timeline.stop();
        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(180), e -> {
                    timeline.pause();
                }, new KeyValue(progressBar.progressProperty(), 1))
        );
        timeline.setCycleCount(1);
    }

    /**
     * Update status for all game events include timers, status bar.
     */
    public void updateStatus() {
        switch (gameController.gameStatus) {
            case GAME_START:
                setUpTimeline();
                nextLevelBox.setVisible(true);
                playingBox.setDisable(true);
                stageText.setText("STAGE " + (GameController.LEVEL + 1)); //Appear when start new level.

                AtomicInteger timerCounter = new AtomicInteger(2);
                Timeline nextLevelTimeline = new Timeline();
                nextLevelTimeline.stop();
                KeyFrame kf = new KeyFrame(Duration.seconds(0),
                        event -> {
                            timerCounter.decrementAndGet();
                            if (timerCounter.get() <= 0) {
                                nextLevelBox.setVisible(false);
                                playingBox.setDisable(false);
                                timeline.play();
                                nextLevelTimeline.stop();
                            }
                        });
                nextLevelTimeline.getKeyFrames().addAll(kf, new KeyFrame(Duration.seconds(1)));
                nextLevelTimeline.setCycleCount(Animation.INDEFINITE);
                nextLevelTimeline.play();
                break;
            case GAME_PLAYING:
                if (timeline.getStatus() == Timeline.Status.PAUSED) {
                    GameController.gameStatus = GameController.GameStatus.GAME_LOSE;
                } else if (timeline.getStatus() == Timeline.Status.RUNNING) {

                    levelText.setText("STAGE " + (GameController.LEVEL + 1)); //Appear in status bar.
                    maxBombs.setText(Integer.toString(gameController.getMaxBombs()));
                    for (int i = 0; i < gameController.getNumOfLives(); i++)
                        livesImg.getChildren().get(i).setVisible(true);
                    for (int i = gameController.getNumOfLives(); i < 3; i++) {
                        livesImg.getChildren().get(i).setVisible(false);
                    }
                }
                break;
            case LOAD_CURRENT_LEVEL:
                setUpTimeline();
                timeline.play();
                playingBox.setDisable(false);
                break;
        }
    }

    public void setLobbyScene(Scene lobbyScene) {
        this.lobbyScene = lobbyScene;
    }
}
