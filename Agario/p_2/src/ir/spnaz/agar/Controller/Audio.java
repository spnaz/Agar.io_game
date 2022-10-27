package ir.spnaz.agar.Controller;


import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Audio {
    public static Clip clip;
    public static boolean playing = true;
    public static void play() {

        InputStream resourceAsStream = Audio.class.getResourceAsStream("/ir/spnaz/agar/View/Resources/music.wav");

        clip = null;
        AudioInputStream inputStream = null;
        try {
            inputStream = AudioSystem.getAudioInputStream(resourceAsStream);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DataLine.Info info = new DataLine.Info(Clip.class, inputStream.getFormat());
        try {

            clip = (Clip)AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        try {
            clip.open(inputStream);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    public static void start() {
        clip.start();
    }

    public static void stop(){
        clip.stop();
    }

    public static void toggleStatus() {
        if (playing)
            stop();
        else
            start();
        playing = !playing;
    }
}
