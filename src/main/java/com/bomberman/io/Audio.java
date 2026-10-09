package com.bomberman.io;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import javax.sound.sampled.*;

/**
 * Odtwarzanie muzyki
 */
public class Audio {

    /**
     * Przechowuje oddtwarzana muzyke
     */
    public static Clip music;
    /**
     * Glosnosc muzyki
     */
    public static float volume = 0.f;

    /**
     * Aktualnie odtwarzany fragment muzyki
     */
    private static int state;
    /**
     * Poczatek muzyki
     */
    public static final int BEGIN = 0;
    /**
     * Fragment odtwarzany w napisach koncowych
     */
    public static final int CREDITS = 146000000;
    /**
     * Glowny motyw muzyczny
     */
    public static final int THEME = 96000000;


    /**
     * Odtwarza dowolny dzwiek
     * @param filePath sciezka pliku
     */
    public static void playAudio(String filePath){
        try
        {
            File file = new File(filePath);
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            clip.start();
            stream.close();

        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Odtwarza muzyke
     */
    public static void playMusic(){
        try
        {
            File file = new File("music/music1.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            music = AudioSystem.getClip();
            music.open(stream);
            music.start();

            stream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    /**
     * Przewija muzyke do danego momentu
     * @param time moment w ms do ktorego przewinac
     */
    public static void jumpTo(int time){
        music.stop();
        music.setMicrosecondPosition(time);
        if (music.getMicrosecondLength()> 400000000)
            if (time != BEGIN) music.setMicrosecondPosition(212000000);
            else playMusic();
        state = time;
        music.start();


    }

    /**
     * Easter Egg
     */
    public static void bonus(){
        music.stop();
        try
        {
            File file = new File("music/music2.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            music = AudioSystem.getClip();
            music.open(stream);
            music.start();
            stream.close();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Metoda zapetlajaca muzyke
     */
    public static void replay(){

        if (music.getMicrosecondPosition() >= music.getMicrosecondLength() - 1000000){
            jumpTo(state);
        }

    }

    /**
     * Zmiana glosnosci
     */
    public static void volumeChange(){
        FloatControl gainControl = (FloatControl) music.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    /**
     * Przyciszanie muzyki
     */
    public static void volumeMinus(){
        if (volume > -80f) {
            volume += -1f;
            volumeChange();
        }
    }

    /**
     * Podglasnianie muzyki
     */
    public static void volumePlus(){
        if(volume < 0f)  {
            volume += +1f;
            volumeChange();
        }
    }

    /**
     * Zatrzymanie muzyki
     */
    public static void stopMusic(){
        music.stop();
    }




}
