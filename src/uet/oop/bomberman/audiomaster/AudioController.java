package uet.oop.bomberman.audiomaster;

import uet.oop.bomberman.GameController;

public class AudioController {
    private boolean isMuted = false; //If isMuted is true, controller will stop all playing music.
    //private List<Audio> playingAudioList = new ArrayList<>();
    private GameController gameController;
    public enum AudioName {
        LOBBY,
        PLAYING,
        EXPLODING,
        EAT_ITEM,
        KILL_ENEMY,
        LOSE,
        WIN_ONE,
        CHOOSE,
        DIE,
        START_STAGE,
        CLICK_BUTTON
    }

    Audio[] audiosList;
    public void run() {
        if(isMuted) {
            audiosList[AudioName.LOBBY.ordinal()].stop();
            audiosList[AudioName.PLAYING.ordinal()].stop();
            audiosList[AudioName.START_STAGE.ordinal()].stop();
        }
        else {
            if(GameController.gameStatus == GameController.GameStatus.GAME_LOBBY) {
                audiosList[AudioName.PLAYING.ordinal()].stop();
                audiosList[AudioName.LOBBY.ordinal()].play(-1);
            }
            if(GameController.gameStatus == GameController.GameStatus.GAME_START) {
                audiosList[AudioName.WIN_ONE.ordinal()].stop();
                audiosList[AudioName.LOBBY.ordinal()].stop();
                audiosList[AudioName.START_STAGE.ordinal()].play(-1);
            }
            if(GameController.gameStatus == GameController.GameStatus.GAME_PLAYING) {
                audiosList[AudioName.START_STAGE.ordinal()].stop();
                audiosList[AudioName.PLAYING.ordinal()].play(-1);
            }
            if(GameController.gameStatus == GameController.GameStatus.WIN_ONE) {
                audiosList[AudioName.PLAYING.ordinal()].stop();
                audiosList[AudioName.WIN_ONE.ordinal()].play(-1);
            }
        }
    }
    public AudioController() {
        audiosList = new Audio[20];
        audiosList[AudioName.LOBBY.ordinal()] = new Audio("res/audio/Title Screen.wav");
        audiosList[AudioName.PLAYING.ordinal()] = new Audio("res/audio/playing.wav");
        audiosList[AudioName.EXPLODING.ordinal()] = new Audio("res/audio/exploding.wav");
        audiosList[AudioName.EAT_ITEM.ordinal()] = new Audio("res/audio/eatItem.wav");
        audiosList[AudioName.CLICK_BUTTON.ordinal()] = new Audio("res/audio/clickButton.wav");
        audiosList[AudioName.START_STAGE.ordinal()] = new Audio("res/audio/startStage.wav");
        audiosList[AudioName.WIN_ONE.ordinal()] = new Audio("res/audio/winOne.wav");
    }

    public void playParallel(AudioName audioName, int time) {
        if (!isMuted && audioName != AudioName.CLICK_BUTTON) {
            Audio audio = audiosList[audioName.ordinal()].copyAudio();
            audio.play(time);
        }
        else if(audioName == AudioName.CLICK_BUTTON) {
            Audio audio = audiosList[audioName.ordinal()].copyAudio();
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

    public void playAlone(AudioName audioName, int time) {
        if (!isMuted) {
            for (int i = 0; i < audiosList.length; i++) {
                if (i != audioName.ordinal()) {
                    if (audiosList[i] != null) {
                        audiosList[i].stop();
                        //playingAudioList.remove(audiosList[i]);
                    }
                }
            }
            audiosList[audioName.ordinal()].play(time);
            //if(time == -1) playingAudioList.add(audiosList[audioName.ordinal()]);
        }
    }

}
