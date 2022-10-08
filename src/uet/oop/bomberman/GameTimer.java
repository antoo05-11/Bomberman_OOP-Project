package uet.oop.bomberman;

import javafx.animation.AnimationTimer;

public class GameTimer extends AnimationTimer {
    private boolean running = false;

    @Override
    public void handle(long now) {

    }

    @Override
    public void start() {
        super.start();
        running = true;
    }

    @Override
    public void stop() {
        super.stop();
        running = false;
    }
    boolean isRunning() {
        return running;
    }
}
