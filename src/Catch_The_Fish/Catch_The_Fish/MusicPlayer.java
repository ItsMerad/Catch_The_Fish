package Catch_The_Fish;
// MusicPlayer.java
import java.net.URL;
import javax.sound.sampled.*;

public class MusicPlayer {
    private Clip clip;
    private boolean playing = false;

    public void play(String path, boolean loop) {
        try {
            URL url = getClass().getResource(path);
            if (url == null) {
                System.out.println("Müzik dosyası bulunamadı: " + path);
                return;
            }
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            playing = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (clip != null && playing) {
            clip.stop();
            clip.close();
            playing = false;
        }
    }

    public boolean isPlaying() {
        return playing;
    }
}