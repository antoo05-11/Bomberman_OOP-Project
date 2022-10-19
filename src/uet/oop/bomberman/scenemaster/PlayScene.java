package uet.oop.bomberman.scenemaster;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import uet.oop.bomberman.GameController;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graph_mapmaster.Graph;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CancellationException;

public class PlayScene extends RootScene {
    private static final int WIDTH_PIXEL_STATUS_FRAME = SCREEN_WIDTH_PIXEL;
    private static final int HEIGHT_PIXEL_STATUS_FRAME = 30;
    private static final int WIDTH_PIXEL_PLAYING_FRAME = WIDTH_PIXEL_STATUS_FRAME;
    private static final int HEIGHT_PIXEL_PLAYING_FRAME = SCREEN_HEIGHT_PIXEL - HEIGHT_PIXEL_STATUS_FRAME;
    HBox layout; //StatusFrame is above PlayingFrame.
    Canvas statusFrame;
    Canvas playingFrame;
    GraphicsContext statusGc;
    GraphicsContext playingGc;
    public PlayScene() {
        statusFrame = new Canvas(WIDTH_PIXEL_STATUS_FRAME, HEIGHT_PIXEL_STATUS_FRAME);
        playingFrame = new Canvas(WIDTH_PIXEL_PLAYING_FRAME, HEIGHT_PIXEL_PLAYING_FRAME);
        playingGc = playingFrame.getGraphicsContext2D();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), true);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                GameController.entities.get(GameController.LEVEL).get(0).saveKeyEvent(event.getCode(), false);
            }
        });
    }
    public static int getWidthPixelPlayingFrame() {
        return WIDTH_PIXEL_PLAYING_FRAME;
    }
    public static int getHeightPixelPlayingFrame() {
        return HEIGHT_PIXEL_PLAYING_FRAME;
    }
    public GraphicsContext getPlayingGc() {
        return playingGc;
    }
}
