package uet.oop.bomberman.audiomaster;

public class AudioController {
    private boolean isMuted = false; //If isMuted is true, controller will stop all playing music.

    public enum AudioName {
        LOBBY,
        PLAYING,
        EXPLODING,
        EAT_ITEM,
        KILL_ENEMY,
        LOSE,
        WIN,
        CHOOSE,
        DIE,
        CLICK_BUTTON
    }

    Audio[] audiosList;

    public AudioController() {
        audiosList = new Audio[10];
        audiosList[AudioName.LOBBY.ordinal()] = new Audio("res/audio/Title Screen.wav");
        audiosList[AudioName.PLAYING.ordinal()] = new Audio("res/audio/playing.wav");
        audiosList[AudioName.EXPLODING.ordinal()] = new Audio("res/audio/exploding.wav");
        audiosList[AudioName.EAT_ITEM.ordinal()] = new Audio("res/audio/eatItem.wav");
        audiosList[AudioName.CLICK_BUTTON.ordinal()] = new Audio("res/audio/clickButton.wav");
    }

    public void playParallel(AudioName audioName, int time) {
        if (!isMuted) {
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
                    }
                }
            }
            audiosList[audioName.ordinal()].play(time);
        }
    }

}
