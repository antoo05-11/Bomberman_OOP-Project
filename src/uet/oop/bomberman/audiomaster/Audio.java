package uet.oop.bomberman.audiomaster;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio {
    private AudioInputStream audioInput;
    private Clip clip;
    private boolean isPlaying = false;
    private String audioSrc;
    public Audio(String audioSrc) {
        File audioFile = new File(audioSrc);
        this.audioSrc = audioSrc;
        try {
            audioInput = AudioSystem.getAudioInputStream(audioFile);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            clip = AudioSystem.getClip();
            clip.open(audioInput);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    /**
     * @time = -1 if loop with infinite times.
     */
    public void play(int time) {
        if(!isPlaying) {
            if(time > 0) clip.loop(time-1);
            else clip.loop(-1);
        }
        isPlaying = true;
    }

    public void stop() {
        if(isPlaying) {
            clip.stop();
        }
        isPlaying = false;
    }

    public Audio copyAudio() {
        Audio copy = new Audio(audioSrc);
        return copy;
    }
}
