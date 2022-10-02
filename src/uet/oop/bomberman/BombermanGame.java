package uet.oop.bomberman;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import uet.oop.bomberman.entities.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;

public class BombermanGame extends Application {

    public static final int WIDTH = 31;
    public static final int HEIGHT = 13;

    public static int LEVEL = 1;

    private GraphicsContext gc;
    private Canvas canvas;
    private List<Entity> entities = new ArrayList<>();
    private List<Entity> stillObjects = new ArrayList<>();


    public static void main(String[] args) {
        Application.launch(BombermanGame.class);
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Tao Canvas
        canvas = new Canvas(Sprite.SCALED_SIZE * WIDTH, Sprite.SCALED_SIZE * HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Tao root container
        Group root = new Group();
        root.getChildren().add(canvas);

        // Tao scene
        Scene scene = new Scene(root);

        // Them scene vao stage
        stage.setScene(scene);
        stage.show();

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                render();
                update();
            }
        };
        timer.start();

        createMap();

        Entity bomberman = new Bomber(1, 1, Sprite.player_right.getFxImage());
        entities.add(bomberman);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                bomberman.saveKeyEvent(event.getCode(), true);
            }
        });
        scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                bomberman.saveKeyEvent(event.getCode(), false);
            }
        });
    }


    public void createMap() throws IOException {

        File file = new File("res/levels/Level" + LEVEL + ".txt");
        Scanner scanner = new Scanner(file);

        scanner.nextLine();

        List<List<Character>> mapInfo = new ArrayList<>();
        List<Character> rowInfo;
        String rowString = "";
        for (int i = 0; i < HEIGHT; i++) {
            if (scanner.hasNext()) {
                rowString = scanner.nextLine();
                System.out.println(rowString);
            }

            for (int j = 0; j < WIDTH; j++) {
                Entity object;

                rowInfo = new ArrayList<>();
                rowInfo.add(rowString.charAt(j));

                if (rowString.charAt(j) == '#') {
                    object = new Wall(j, i, Sprite.wall.getFxImage());
                } else if (rowString.charAt(j) == '*') {
                    object = new Wall(j, i, Sprite.brick.getFxImage());
                } else if (rowString.charAt(j) == 'x') {
                    object = new Wall(j, i, Sprite.portal.getFxImage());
                } else {
                    object = new Grass(j, i, Sprite.grass.getFxImage());
                }

                mapInfo.add(rowInfo);

                stillObjects.add(object);
            }
        }

    }

    public void update() {
        entities.forEach(Entity::update);
    }

    public void render() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        stillObjects.forEach(g -> g.render(gc));
        entities.forEach(g -> g.render(gc));
    }
}
