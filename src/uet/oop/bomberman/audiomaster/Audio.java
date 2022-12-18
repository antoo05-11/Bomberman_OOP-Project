package uet.oop.bomberman.audiomaster;

import uet.oop.bomberman.BombermanGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.InputStream;

public class Audio {
    private Clip clip;
    private boolean isPlaying = false;
    private String source;

    public Audio(String source) {
        this.source = source;
        try {

            AudioInputStream audioInput = AudioSystem.getAudioInputStream(getClass().getResource(source));
            clip = AudioSystem.getClip();
            clip.open(audioInput);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @time = -1 if loop with infinite times.
     */
    public void play(int time) {
        if (!isPlaying) {
            clip.loop((time > 0) ? time - 1 : -1);
        }
        isPlaying = true;
    }

    public void stop() {
        if (isPlaying) {
            clip.stop();
        }
        isPlaying = false;
    }

    /**
     * clone current audio to play parallel same audio.
     *
     * @return clone current audio.
     */
    public Audio cloneAudio() {
        return new Audio(source);
    }
}
