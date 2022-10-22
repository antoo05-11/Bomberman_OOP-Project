package uet.oop.bomberman;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.enemies.Balloom;
import uet.oop.bomberman.entities.movingobject.enemies.Enemy;
import uet.oop.bomberman.entities.movingobject.enemies.Oneal;
import uet.oop.bomberman.entities.stillobject.item.BombItem;
import uet.oop.bomberman.entities.stillobject.item.FlameItem;
import uet.oop.bomberman.entities.stillobject.item.SpeedItem;
import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Grass;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.graph_mapmaster.Graph;
import uet.oop.bomberman.graph_mapmaster.Vertice;
import uet.oop.bomberman.graphics.Sprite;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static uet.oop.bomberman.BombermanGame.HEIGHT;
import static uet.oop.bomberman.BombermanGame.WIDTH;

public class Map {
    private int widthTile;
    private int heightTile;

    public int getHeightTile() {
        return heightTile;
    }

    public int getWidthTile() {
        return widthTile;
    }

    public int getWidthPixel() {
        return widthTile * Sprite.SCALED_SIZE;
    }

    public int getHeightPixel() {
        return heightTile * Sprite.SCALED_SIZE;
    }

    private final List<List<Entity>> mapInfo = new ArrayList<>(); //Can be changed.
    int LEVEL;
    private Graph graph;
    protected int[][] listItem = new int[HEIGHT][WIDTH];

    public Map(int LEVEL) {
        this.LEVEL = LEVEL;
        readMapFromFile();
        convertMapToGraph();
    }

    public Graph getGraph() {
        return graph;
    }

    /**
     * Read map, add still objects to mapInfo such as wall, brick, grass.
     * Bomber and other enemies are added to entities list in GameController class.
     */
    public void readMapFromFile() {
        File file = new File("res/levels/Level" + (LEVEL + 1) + ".txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("No file exist");
        }
        String rowString = ""; //Save info of a row into string.
        assert scanner != null;
        rowString = scanner.nextLine(); //Read first line in Level.txt.

        String[] specs = rowString.split(" "); //Line 1 splits: LEVEL, WIDTH, HEIGHT.
        widthTile = Integer.parseInt(specs[2]);
        heightTile = Integer.parseInt(specs[1]);

        for (int i = 0; i < heightTile; i++) {
            rowString = scanner.nextLine();
            mapInfo.add(new ArrayList<>());
            for (int j = 0; j < widthTile; j++) {
                switch (rowString.charAt(j)) {
                    case 'p':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Entity bomberman = new Bomber(j, i, new CollisionManager(this, Bomber.WIDTH, Bomber.HEIGHT));
                        GameController.entities.get(LEVEL).add(bomberman);
                        break;
                    case '#':
                        mapInfo.get(i).add(new Wall(j, i, Sprite.wall.getFxImage()));
                        break;
                    case '*':
                        mapInfo.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        break;
                    case '1':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy ballom = new Balloom(j, i, Sprite.balloom_left1.getFxImage(), new CollisionManager(this, Balloom.WIDTH, Balloom.HEIGHT));
                        GameController.entities.get(LEVEL).add(ballom);
                        break;
                    case '2':
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        Enemy oneal = new Oneal(j, i, Sprite.oneal_right1.getFxImage(), new CollisionManager(this, Oneal.WIDTH, Oneal.HEIGHT), GameController.entities.get(LEVEL).get(0));
                        GameController.entities.get(LEVEL).add(oneal);
                        break;
                    case 'b':
                        mapInfo.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        listItem[i][j] = BombItem.code;
                        break;
                    case 'f':
                        mapInfo.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        listItem[i][j] = FlameItem.code;
                        break;
                    case 's':
                        mapInfo.get(i).add(new Brick(j, i, Sprite.brick.getFxImage()));
                        listItem[i][j] = SpeedItem.code;
                        break;
                    default:
                        mapInfo.get(i).add(new Grass(j, i, Sprite.grass.getFxImage()));
                        break;
                }
            }
        }
        scanner.close();
    }

    /**
     * Render still
     */
    public void mapRender(GraphicsContext gc) {
        for (int i = 0; i < mapInfo.size(); i++) {
            for (int j = 0; j < mapInfo.get(i).size(); j++) {
                mapInfo.get(i).get(j).render(gc);
            }
        }
    }

    public Entity getEntityAt(int x, int y) {
        return mapInfo.get(y / Sprite.SCALED_SIZE).get(x / Sprite.SCALED_SIZE);
    }

    public int getItem(int xPos, int yPos) {
        return listItem[yPos][xPos];
    }

    /**
     * Read map again and refresh entities list in GameController class.
     */
    public void reset() {
        GameController.entities.get(LEVEL).clear();
        GameController.bombsList.clear();
        GameController.itemsList.clear();
        mapInfo.clear();
        readMapFromFile();
        convertMapToGraph();
    }

    /**
     * Get a random item and replace current position by Grass.
     */
    public Entity randomItem(int rowPos, int columnPos) {
        Entity newItem = null;
        Random random = new Random();
        int rand = random.nextInt(3);
        switch (rand) {
            case 0:
                newItem = new SpeedItem(columnPos, rowPos, Sprite.powerup_speed.getFxImage());
                break;
            case 1:
                newItem = new FlameItem(columnPos, rowPos, Sprite.powerup_flames.getFxImage());
                break;
            case 2:
                newItem = new BombItem(columnPos, rowPos, Sprite.powerup_bombs.getFxImage());
                break;
        }
        replace(rowPos, columnPos, new Grass(columnPos, rowPos, Sprite.grass.getFxImage()));
        return newItem;
    }

    /**
     * Replace any entity by other entity in mapInfo.
     */
    public void replace(int rowPos, int columnPos, Entity newItem) {
        mapInfo.get(rowPos).set(columnPos, newItem);
    }

    /**
     * Read from mapInfo to graph.
     * Add all cells on map and add adj vertices into Graph.
     */
    public void convertMapToGraph() {
        List<Vertice> verticesList = new ArrayList<>();
        for (int i = 0; i < mapInfo.size(); i++) {
            for (int j = 0; j < mapInfo.get(i).size(); j++) {
                verticesList.add(new Vertice(j, i));
            }
        }
        graph = new Graph(verticesList);
        for (int i = 0; i < verticesList.size() - 1; i++) {
            for (int j = i + 1; j < verticesList.size(); j++) {
                boolean isAdj = false;
                int x1 = verticesList.get(i).getxTilePos();
                int x2 = verticesList.get(j).getxTilePos();
                int y1 = verticesList.get(i).getyTilePos();
                int y2 = verticesList.get(j).getyTilePos();
                if (mapInfo.get(y1).get(x1) instanceof Grass
                        && mapInfo.get(y2).get(x2) instanceof Grass) {
                    if (x1 == x2) {
                        if (y1 == y2 + 1) isAdj = true;
                        else if (y1 == y2 - 1) isAdj = true;
                    } else if (y1 == y2) {
                        if (x1 == x2 + 1) isAdj = true;
                        else if (x1 == x2 - 1) isAdj = true;
                    }
                }
                if (isAdj) graph.addAdjVertice(i, j);
            }
        }
    }

}
