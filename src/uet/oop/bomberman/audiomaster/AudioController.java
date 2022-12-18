package uet.oop.bomberman.audiomaster;

import uet.oop.bomberman.GameController;

public class AudioController {
    private boolean isMuted = false;
    public enum AudioName {
        LOBBY,
        PLAYING,
        EXPLODING,
        EAT_ITEM,
        WIN_ALL,
        WIN_ONE,
        START_STAGE,
        CLICK_BUTTON
    }

    Audio[] audiosList;

    public AudioController() {
        audiosList = new Audio[20];
        audiosList[AudioName.LOBBY.ordinal()] = new Audio("/audio/lobby.wav");
        audiosList[AudioName.PLAYING.ordinal()] = new Audio("/audio/playing.wav");
        audiosList[AudioName.EXPLODING.ordinal()] = new Audio("/audio/exploding.wav");
        audiosList[AudioName.EAT_ITEM.ordinal()] = new Audio("/audio/eatItem.wav");
        audiosList[AudioName.CLICK_BUTTON.ordinal()] = new Audio("/audio/clickButton.wav");
        audiosList[AudioName.START_STAGE.ordinal()] = new Audio("/audio/startStage.wav");
        audiosList[AudioName.WIN_ONE.ordinal()] = new Audio("/audio/winOne.wav");
        audiosList[AudioName.WIN_ALL.ordinal()] = new Audio("/audio/winAll.wav");
    }

    /**
     * Control the background music for the game in each game status.
     */
    public void run() {
        if (isMuted) {
            audiosList[AudioName.LOBBY.ordinal()].stop();
            audiosList[AudioName.PLAYING.ordinal()].stop();
            audiosList[AudioName.START_STAGE.ordinal()].stop();
            audiosList[AudioName.WIN_ALL.ordinal()].stop();
        } else {
            if (GameController.gameStatus == GameController.GameStatus.GAME_LOBBY) {
                audiosList[AudioName.PLAYING.ordinal()].stop();
                audiosList[AudioName.WIN_ALL.ordinal()].stop();
                audiosList[AudioName.LOBBY.ordinal()].play(-1);
            }
            if (GameController.gameStatus == GameController.GameStatus.GAME_START) {
                audiosList[AudioName.WIN_ONE.ordinal()].stop();
                audiosList[AudioName.LOBBY.ordinal()].stop();
                audiosList[AudioName.START_STAGE.ordinal()].play(-1);
            }
            if (GameController.gameStatus == GameController.GameStatus.GAME_PLAYING) {
                audiosList[AudioName.START_STAGE.ordinal()].stop();
                audiosList[AudioName.PLAYING.ordinal()].play(-1);
            }
            if (GameController.gameStatus == GameController.GameStatus.WIN_ONE) {
                audiosList[AudioName.PLAYING.ordinal()].stop();
                audiosList[AudioName.WIN_ONE.ordinal()].play(-1);
            }
            if (GameController.gameStatus == GameController.GameStatus.WIN_ALL) {
                audiosList[AudioName.WIN_ONE.ordinal()].stop();
                audiosList[AudioName.WIN_ALL.ordinal()].play(-1);
            }
        }
    }


    public void playParallel(AudioName audioName, int time) {
        if (!isMuted && audioName != AudioName.CLICK_BUTTON) {
            Audio audio = audiosList[audioName.ordinal()].cloneAudio();
            audio.play(time);
        } else if (audioName == AudioName.CLICK_BUTTON) {
            Audio audio = audiosList[audioName.ordinal()].cloneAudio();
            audio.play(time);
        }
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean isMuted) {
        this.isMuted = isMuted;
        if (isMuted) {
            for (Audio i : audiosList) {
                if (i != null)
                    i.stop();
            }
        }
    }
}
