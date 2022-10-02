package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Grass;
import uet.oop.bomberman.entities.Wall;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static uet.oop.bomberman.BombermanGame.*;

public class Map {
    private static List<Entity> stillObjects = new ArrayList<>();

    public static void createMap() throws IOException {

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

    public static void mapRender(GraphicsContext gc) {
        stillObjects.forEach(g -> g.render(gc));
    }
}
