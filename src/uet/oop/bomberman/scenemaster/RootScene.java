package uet.oop.bomberman.scenemaster;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import uet.oop.bomberman.BombermanGame;
import uet.oop.bomberman.graphics.Sprite;

public abstract class RootScene {
    protected static final int SCREEN_WIDTH_PIXEL = 700;
    protected static final int SCREEN_HEIGHT_PIXEL = 400;
    Canvas canvas;
    Canvas canvas2;
    GraphicsContext gc;
    VBox root;
    Scene scene;
    public RootScene() {
        canvas = new Canvas(SCREEN_WIDTH_PIXEL, SCREEN_HEIGHT_PIXEL-30);
        gc = canvas.getGraphicsContext2D();
        canvas2 = new Canvas(SCREEN_WIDTH_PIXEL, 30);
        root = new VBox();
        root.getChildren().add(canvas2);
        root.getChildren().add(canvas);
        scene = new Scene(root);
    }

    public Scene getScene() {
        return scene;
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public GraphicsContext getGc() {
        return gc;
    }
}
