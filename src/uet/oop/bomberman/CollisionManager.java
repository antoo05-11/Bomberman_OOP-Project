package uet.oop.bomberman;

import uet.oop.bomberman.entities.Entity;

public class CollisionManager {
     private Map map;

    public static enum DIRECTION {
        UP, DOWN, LEFT, RIGHT, CENTER, STOP;
    }

     public CollisionManager(Map map){
         this.map = map;
     }

     /*public boolean collide(int x, int y, DIRECTION dir){
         Entity object1;
         Entity object2;
         switch (dir){
             case UP:
                 object1 = map.getPosition(x + FIX_WIDTH, y + FIX_HEIGHT);
                 object2 = map.getPosition(x + 24 - FIX_WIDTH, y + FIX_HEIGHT);
                 break;
             case DOWN:
                 object1 = map.getPosition(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                 object2 = map.getPosition(x + 24 - FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                 break;
             case LEFT:
                 object1 = map.getPosition(x + FIX_WIDTH, y + FIX_HEIGHT);
                 object2 = map.getPosition(x + FIX_WIDTH, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                 break;
             case RIGHT:
                 object1 = map.getPosition(x + 24, y + FIX_HEIGHT);
                 object2 = map.getPosition(x + 24, y + Sprite.SCALED_SIZE - FIX_HEIGHT);
                 break;
         }
     }*/
}
