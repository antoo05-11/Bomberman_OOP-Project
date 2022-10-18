package uet.oop.bomberman.audiomaster;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AudioController {
    public enum AudioName {
        LOBBY,
        PLAYING,
        EXPLODING,
        EAT_ITEM,
        KILL_ENEMY,
        LOSE,
        WIN,
        CHOOSE,
        DIE
    }

    Audio[] audiosList;

    public AudioController() {
        audiosList = new Audio[5];
        audiosList[AudioName.LOBBY.ordinal()] = new Audio("res/audio/Title Screen.wav");
        audiosList[AudioName.PLAYING.ordinal()] = new Audio("res/audio/playing.wav");
        audiosList[AudioName.EXPLODING.ordinal()] = new Audio("res/audio/exploding.wav");
        audiosList[AudioName.EAT_ITEM.ordinal()] = new Audio("res/audio/eatItem.wav");
    }

    public void playParallel(AudioName audioName, int time) {
        Audio audio = audiosList[audioName.ordinal()].copyAudio();
        audio.play(time);
    }


    public void playAlone(AudioName audioName, int time) {
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
