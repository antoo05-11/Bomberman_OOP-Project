package uet.oop.bomberman.map_graph;

import javafx.scene.canvas.GraphicsContext;
import uet.oop.bomberman.Level;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.movingobject.Bomber;
import uet.oop.bomberman.entities.movingobject.MovingObject;
import uet.oop.bomberman.entities.movingobject.enemies.*;
import uet.oop.bomberman.entities.stillobject.Brick;
import uet.oop.bomberman.entities.stillobject.Grass;
import uet.oop.bomberman.entities.stillobject.Portal;
import uet.oop.bomberman.entities.stillobject.Wall;
import uet.oop.bomberman.entities.stillobject.bomb.Bomb;
import uet.oop.bomberman.entities.stillobject.item.BombItem;
import uet.oop.bomberman.entities.stillobject.item.FlameItem;
import uet.oop.bomberman.entities.stillobject.item.SpeedItem;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.scenemaster.SceneController;

import java.io.*;
import java.util.*;



public class Map {
    private int widthTile;
    private int heightTile;
    private Level currentLevel;
    private List<Entity> movingEntitiesList;
    private Entity[][] mapInfo;
    protected Entity[][] grassList;
    protected Entity[][] bombList;
    protected List<Entity> bombArrayList;
    protected Entity[][] itemAndPortalList;

    public Map(Level currentLevel) {
        this.currentLevel = currentLevel;
        readMapFromFile();
    }

    public int getWidth_Pixel() {
        return widthTile * Sprite.SCALED_SIZE;
    }

    public int getHeight_Pixel() {
        return heightTile * Sprite.SCALED_SIZE;
    }

    /**
     * Read map, add still objects to mapInfo such as wall, brick, grass.
     * Bomber and other enemies are added to entities list in GameController class.
     */
    public void readMapFromFile() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(getClass().getResource("/levels/Level" + (currentLevel.getLevelCode().get() + 1) + ".txt").openStream());
//            InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResource("/levels/Level" + (currentLevel.getLevelCode().get() + 1) + ".txt").openStream())
//            BufferedReader br = new BufferedReader(inputStreamReader);
        } catch (FileNotFoundException e) {
            System.out.println("No file exist");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        String rowString; //Save info of a row into string.
        assert scanner != null;
        rowString = scanner.nextLine(); //Read first line in Level.txt.

        StringTokenizer specs = new StringTokenizer(rowString, " "); //Line 1 splits: LEVEL, WIDTH, HEIGHT.
        specs.nextToken();
        heightTile = Integer.parseInt(specs.nextToken());
        widthTile = Integer.parseInt(specs.nextToken());


        mapInfo = new Entity[heightTile][widthTile];
        grassList = new Entity[heightTile][widthTile];
        bombList = new Entity[heightTile][widthTile];
        itemAndPortalList = new Entity[heightTile][widthTile];
        bombArrayList = new LinkedList<>();
        movingEntitiesList = new ArrayList<>();

        for (int i = 0; i < heightTile; i++) {
            rowString = scanner.nextLine();
            for (int j = 0; j < widthTile; j++) {
                grassList[i][j] = new Grass(j, i);
                switch (rowString.charAt(j)) {
                    case 'p':
                        Entity bomberman = new Bomber(j, i, new CollisionManager(this, Bomber.WIDTH, Bomber.HEIGHT));
                        movingEntitiesList.add(bomberman);
                        break;
                    case '#':
                        mapInfo[i][j] = new Wall(j, i);
                        break;
                    case '*':
                        mapInfo[i][j] = new Brick(j, i, this);
                        break;
                    case 'x':
                        mapInfo[i][j] = new Brick(j, i, this);
                        itemAndPortalList[i][j] = new Portal(j, i, this);
                        break;
                    case '1':
                        Enemy balloom = new Balloom(j, i, new CollisionManager(this, Balloom.WIDTH, Balloom.HEIGHT));
                        movingEntitiesList.add(balloom);
                        break;
                    case '2':
                        Enemy oneal = new Oneal(j, i, new CollisionManager(this, Oneal.WIDTH, Oneal.HEIGHT));
                        movingEntitiesList.add(oneal);
                        break;
                    case '3':
                        Enemy doll = new Doll(j, i, new CollisionManager(this, Doll.WIDTH, Doll.HEIGHT));
                        movingEntitiesList.add(doll);
                        break;
                    case '4':
                        Enemy kondoria = new Kondoria(j, i, new CollisionManager(this, Kondoria.WIDTH, Kondoria.HEIGHT));
                        movingEntitiesList.add(kondoria);
                        break;
                    case 'b':
                        mapInfo[i][j] = new Brick(j, i, this);
                        itemAndPortalList[i][j] = new BombItem(j, i, this);
                        break;
                    case 'f':
                        mapInfo[i][j] = new Brick(j, i, this);
                        itemAndPortalList[i][j] = new FlameItem(j, i, this);
                        break;
                    case 's':
                        mapInfo[i][j] = new Brick(j, i, this);
                        itemAndPortalList[i][j] = new SpeedItem(j, i, this);
                        break;
                    default:
                        break;
                }
            }
        }
        scanner.close();
    }

    /**
     * Render all list of map.
     */
    public void render(GraphicsContext gc) {
        for (int i = 0; i < heightTile; i++) {
            for (int j = 0; j < widthTile; j++) {
                grassList[i][j].render(gc);
            }
        }
        for (int i = 0; i < heightTile; i++) {
            for (int j = 0; j < widthTile; j++) {
                if (itemAndPortalList[i][j] != null) {
                    itemAndPortalList[i][j].render(gc);
                }
            }
        }
        for (int i = 0; i < heightTile; i++) {
            for (int j = 0; j < widthTile; j++) {
                if (mapInfo[i][j] != null) {
                    mapInfo[i][j].render(gc);
                }
            }
        }
        for (Entity i : bombArrayList) i.render(gc);
        for (Entity i : movingEntitiesList) i.render(gc);
    }

    /**
     * Get entity in map.
     *
     * @param x int
     * @param y int
     * @return Entity
     */
    public Entity getEntityAt(int x, int y) {
        Entity entity = mapInfo[y / Sprite.SCALED_SIZE][x / Sprite.SCALED_SIZE];

        if (bombList[y / Sprite.SCALED_SIZE][x / Sprite.SCALED_SIZE] != null)
            entity = bombList[y / Sprite.SCALED_SIZE][x / Sprite.SCALED_SIZE];

        return entity;
    }

    /**
     * Get item in map.
     *
     * @return itemAndPortalList
     */
    public Entity getItem(int xTile, int yTile) {
        return itemAndPortalList[yTile][xTile];
    }


    /**
     * Replace any entity by other entity in mapInfo.
     */
    public void removeInMapInfo(int xTile, int yTile) {
        mapInfo[yTile][xTile] = null;
    }

    public void removeItem(int xTile, int yTile) {
        itemAndPortalList[yTile][xTile] = null;
    }


    /**
     * Read from mapInfo to graph.
     * Add all cells on map and add adj vertices into Graph.
     * This method need be called after any change of any still element like brick, bomb.
     */
    public Graph convertMapToGraph(Vertice onealVertice) {
        //TODO: read from mapinfo to graph

        List<Vertice> verticesList = new ArrayList<>();

        //Add bomber pos and oneal pos as first and second vertice.
        Vertice bomberVertice = new Vertice((movingEntitiesList.get(0).getX() + Bomber.WIDTH / 2) / Sprite.SCALED_SIZE,
                (movingEntitiesList.get(0).getY() + Bomber.HEIGHT / 2) / Sprite.SCALED_SIZE);
        verticesList.add(bomberVertice);
        verticesList.add(onealVertice);

        //Then add all vertice from mapInfo list.
        for (int i = 1; i < mapInfo.length - 1; i++) {
            for (int j = 1; j < mapInfo[i].length - 1; j++) {
                if (getEntityAt(j * Sprite.SCALED_SIZE, i * Sprite.SCALED_SIZE) == null) {
                    Vertice vertice = new Vertice(j, i);
                    if (vertice.isAVerticeInGraph(this) && !vertice.equals(bomberVertice) && !vertice.equals(onealVertice)) {
                        verticesList.add(vertice);
                    }
                }
            }
        }
        Graph graph = new Graph(verticesList.size(), verticesList);
        graph.completeBuildingGraph(this);

        return graph;
    }



    /**
     * Set bomb for bomber.
     */
    public void setBomb(int xPixel, int yPixel) {
        if (bombArrayList.size() < ((Bomber)movingEntitiesList.get(0)).getMaxBomb()) {
            int xTile = xPixel / Sprite.SCALED_SIZE;
            int yTile = yPixel / Sprite.SCALED_SIZE;
            if (bombList[yTile][xTile] == null) {
                bombList[yTile][xTile] = new Bomb(xTile, yTile, this);
                bombArrayList.add(bombList[yTile][xTile]);
            }
        }
    }

    /**
     * Update bombArrayList: If any bomb disappear, remove it from bomb linked list.
     */
    public void updateBombArrayList() {
        bombArrayList.forEach(Entity::update);
        if (!bombArrayList.isEmpty())
            if (((Bomb) bombArrayList.get(0)).getBombStatus() == Bomb.BombStatus.DISAPPEAR) {
                bombList[bombArrayList.get(0).getY() / Sprite.SCALED_SIZE][bombArrayList.get(0).getX() / Sprite.SCALED_SIZE] = null;
                bombArrayList.remove(0);
            }
    }

    /**
     * Update entities list (moving entities).
     */
    public void updateMovingEntitiesList() {
        for (int i = movingEntitiesList.size() - 1; i >= 0; i--) {
            //Remove enemies died by bomb out of list.
            if (movingEntitiesList.get(i) instanceof Enemy) {
                if (((MovingObject) movingEntitiesList.get(i)).getObjectStatus() == MovingObject.MovingObjectStatus.DEAD) {
                    currentLevel.plusPoint(((Enemy) movingEntitiesList.get(i)).getRewardPoint());
                    movingEntitiesList.remove(i);
                }
            }
        }
    }

    /**
     * dx_gc and dy_gc is displacement of graphic context gc.
     * These values are set along with the position of Bomber.
     */
    public static int dx_gc = 0, dy_gc = 0;

    /**
     * If xPixel of bomber < SCREEN_WIDTH/2, gc render (xPixel,yPixel) as usual.
     * If SCREEN_WIDTH/2 < xPixel of bomber < MAP_WIDTH - SCREEN_WIDTH/2, gc decrease position of image by xPixel-SCREEN_WIDTH/2.
     * If xPixel of bomber > MAP_WIDTH - SCREEN_WIDTH/2, gc  decrease position of image by MAP_WIDTH - SCREEN_WIDTH.
     * All operations do same for y_pos rendering of gc.
     */
    public void setUpMapCamera() {
        int bomber_x = movingEntitiesList.get(0).getX();
        int bomber_y = movingEntitiesList.get(0).getY();
        if (bomber_y < (SceneController.SCREEN_HEIGHT - 30) / 2) {
            dy_gc = 0;
        } else if (bomber_y < getHeight_Pixel() - (SceneController.SCREEN_HEIGHT - 30) / 2) {
            dy_gc = bomber_y - (SceneController.SCREEN_HEIGHT - 30) / 2;
        } else {
            dy_gc = getHeight_Pixel() - (SceneController.SCREEN_HEIGHT - 30);
        }
        if (bomber_x < SceneController.SCREEN_WIDTH / 2) {
            dx_gc = 0;
        } else if (bomber_x < getWidth_Pixel() - SceneController.SCREEN_WIDTH / 2) {
            dx_gc = bomber_x - SceneController.SCREEN_WIDTH / 2;
        } else {
            dx_gc = getWidth_Pixel() - SceneController.SCREEN_WIDTH;
        }

    }

    public int getBomberNumOfLives() {
        return ((Bomber) movingEntitiesList.get(0)).getNumOfLives();
    }

    public void setBomberNumOfLives(int num) {
        ((Bomber) movingEntitiesList.get(0)).setNumOfLives(num);
    }

    public List<Entity> getMovingEntitiesList() {
        return movingEntitiesList;
    }
}
