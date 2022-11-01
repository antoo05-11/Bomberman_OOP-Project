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
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.map_graph.Map;

import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static uet.oop.bomberman.GameController.GameStatus.*;
import static uet.oop.bomberman.GameController.LEVEL;

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
    @FXML
    VBox winOneImg;
    @FXML
    Text pointText;
    Timeline timeline;
    Timeline nextLevelTimeline;
    Timeline victoryTimeline;
    AtomicInteger timerCounter = new AtomicInteger(3);
    int curGamePoint = 0;
    @FXML
    VBox winAllBox;
    @FXML
    private HBox livesImg;

    /**
     * Initialize.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        URL playingURL = BombermanGame.class.getResource("/UI_fxml/PlayingScene.fxml");
        if (location.equals(playingURL)) {

            //Add playing canvas from gameController
            playingBox.getChildren().add(GameController.playingCanvas);

            levelText.setText("LEVEL " + LEVEL);
            pointText.setText(Integer.toString(curGamePoint));

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
                    GameController.gameStatus = GAME_UNPAUSE;
                }
            });

            // Click audio button.
            audioButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    GameController.audioController.setMuted(!GameController.audioController.isMuted());
                }
            });

            // Click quit button.
            quitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    GameController.gameStatus = GAME_PAUSE;
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Do you want to quit?", ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText(null);
                    Optional<ButtonType> buttonType = alert.showAndWait();
                    if (buttonType.get() == ButtonType.YES) {
                        GameController.gameStatus = GAME_LOSE;
                    } else GameController.gameStatus = GAME_UNPAUSE;
                }
            });

            // Set keyboard event for playing box.
            playingBox.getChildren().get(1).setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    ((Bomber) GameController.entities.get(LEVEL).get(0)).saveKeyEvent(event.getCode(), true);
                }
            });
            playingBox.getChildren().get(1).setOnKeyReleased(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    ((Bomber) GameController.entities.get(LEVEL).get(0)).saveKeyEvent(event.getCode(), false);
                }
            });
            playingBox.getChildren().get(1).setFocusTraversable(true);

            timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), 0)),
                    new KeyFrame(Duration.seconds(GameController.MAX_TIME), e -> {
                        timeline.stop();
                    }, new KeyValue(progressBar.progressProperty(), 1))
            );
            timeline.setCycleCount(1);

            nextLevelTimeline = new Timeline();
            nextLevelTimeline.getKeyFrames().addAll(new KeyFrame(Duration.seconds(0),
                    event -> {
                        timerCounter.decrementAndGet();
                        if (timerCounter.get() <= 0) {
                            nextLevelBox.setVisible(false);
                            playingBox.setDisable(false);
                            timeline.playFromStart();
                            GameController.gameStatus = GAME_PLAYING;
                            nextLevelTimeline.stop();
                        }
                    }), new KeyFrame(Duration.seconds(1)));
            nextLevelTimeline.setCycleCount(-1);

            victoryTimeline = new Timeline();
            victoryTimeline.getKeyFrames().addAll(new KeyFrame(Duration.seconds(0),
                    event -> {
                        timerCounter.decrementAndGet();
                        if (timerCounter.get() <= 0) {

                            if (LEVEL < GameController.MAX_LEVEL && GameController.gameStatus == WIN_ONE) {
                                LEVEL++;
                                GameController.gameStatus = GameController.GameStatus.GAME_START;
                            } else {
                                if (GameController.gameStatus != GameController.GameStatus.WIN_ALL)
                                    GameController.gameStatus = GameController.GameStatus.WIN_ALL;
                                else {
                                    GameController.gameStatus = GAME_LOBBY;
                                    stage.setScene(lobbyScene);
                                }
                            }
                            winOneImg.setVisible(false);
                            winAllBox.setVisible(false);
                            victoryTimeline.stop();
                        }
                    }), new KeyFrame(Duration.seconds(1)));
            victoryTimeline.setCycleCount(-1);
            winAllBox.setVisible(false);
        }
    }

    /**
     * Update status for all game events include timers, status bar.
     */
    public void updateStatus() {
        switch (GameController.gameStatus) {
            case GAME_START:
                if (nextLevelTimeline.getStatus() == Animation.Status.STOPPED) {
                    nextLevelBox.setVisible(true);
                    playingBox.setDisable(true);
                    stageText.setText("STAGE " + (LEVEL + 1));
                    timerCounter.set(3);
                    nextLevelTimeline.playFromStart();
                }
                break;
            case GAME_PLAYING:
                if (curGamePoint < gameController.getGamePoint()) {
                    pointText.setText(Integer.toString(++curGamePoint));
                } else if (curGamePoint > gameController.getGamePoint()) {
                    pointText.setText(Integer.toString(--curGamePoint));
                }
                if (timeline.getStatus() == Timeline.Status.STOPPED) {
                    GameController.gameStatus = GAME_LOSE;
                } else if (timeline.getStatus() == Timeline.Status.RUNNING) {
                    // Update display.
                    levelText.setText("STAGE " + (LEVEL + 1));
                    maxBombs.setText(Integer.toString(Map.MAX_BOMB));
                    muteLine.setVisible(GameController.audioController.isMuted());
                    for (int i = 0; i < gameController.getNumOfLives(); i++)
                        livesImg.getChildren().get(i).setVisible(true);
                    for (int i = gameController.getNumOfLives(); i < Bomber.MAX_LIVES; i++) {
                        livesImg.getChildren().get(i).setVisible(false);
                    }

                    // Update logic game.
                    GameController.entities.get(LEVEL).forEach(Entity::update);
                    gameController.updateMapCamera();
                    gameController.updateEntitiesList();

                }
                break;
            case GAME_PAUSE:
                timeline.pause();
                break;
            case GAME_UNPAUSE:
                timeline.play();
                GameController.gameStatus = GAME_PLAYING;
                break;
            case LOAD_CURRENT_LEVEL:
                // Game display.
                timeline.playFromStart();

                // Logic game
                gameController.resetCurrentLevel();
                GameController.gameStatus = GAME_PLAYING;
                break;
            case WIN_ONE:
                if (curGamePoint < gameController.getGamePoint()) {
                    pointText.setText(Integer.toString(++curGamePoint));
                } else if (curGamePoint > gameController.getGamePoint()) {
                    pointText.setText(Integer.toString(--curGamePoint));
                }
                if (victoryTimeline.getStatus() != Animation.Status.RUNNING) {
                    timerCounter.set(7);
                    victoryTimeline.playFromStart();
                    gameController.plusPoint((int) ((1 - progressBar.getProgress()) * (LEVEL + 1) * 100));
                    timeline.stop();
                    winOneImg.setVisible(true);
                    gameController.resetLevelPoint();
                }
                break;
            case WIN_ALL:
                if (victoryTimeline.getStatus() != Animation.Status.RUNNING) {
                    updateRankingTable();
                    timerCounter.set(7);
                    victoryTimeline.playFromStart();
                    winAllBox.setVisible(true);
                    gameController.resetAllLevel();
                }
                break;
            case GAME_LOSE:
                updateRankingTable();
                gameController.resetAllLevel();
                curGamePoint = 0;
                stage.setScene(lobbyScene);
                GameController.gameStatus = GAME_LOBBY;
                timeline.stop();
                break;
        }
    }

    /**
     * Update ranking table.
     */
    public void updateRankingTable() {
        File file = new File("res/lobbyTexture/ranking.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            fileWriter.write("\n" + gameController.getUsername() + "\n" + gameController.getGamePoint() + " " + formatter.format(date));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set lobby scene.
     */
    public void setLobbyScene(Scene lobbyScene) {
        this.lobbyScene = lobbyScene;
    }
}
