package com.bomberman.io;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import javax.sound.sampled.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBVorbis;
import org.lwjgl.system.libc.LibCStdlib;

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
     * Dekoduje plik OGG Vorbis (stb_vorbis z LWJGL) i laduje go do obiektu Clip
     * @param filePath sciezka pliku .ogg
     * @return gotowy do odtworzenia Clip
     * @throws Exception gdy nie uda sie zdekodowac pliku lub otworzyc linii audio
     */
    private static Clip loadOgg(String filePath) throws Exception {
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        IntBuffer sampleRate = BufferUtils.createIntBuffer(1);
        ShortBuffer pcm = STBVorbis.stb_vorbis_decode_filename(filePath, channels, sampleRate);
        if (pcm == null) throw new Exception("Nie udalo sie zdekodowac " + filePath);
        byte[] data = new byte[pcm.remaining() * 2];
        for (int i = 0, n = pcm.remaining(); i < n; i++) {
            short sample = pcm.get(i);
            data[2 * i] = (byte) sample;
            data[2 * i + 1] = (byte) (sample >> 8);
        }
        LibCStdlib.free(pcm);
        AudioFormat format = new AudioFormat(sampleRate.get(0), 16, channels.get(0), true, false);
        Clip clip = AudioSystem.getClip();
        clip.open(format, data, 0, data.length);
        return clip;
    }

    /**
     * Odtwarza muzyke
     */
    public static void playMusic(){
        try
        {
            music = loadOgg("music/music1.ogg");
            music.start();
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
            music = loadOgg("music/music2.ogg");
            music.start();
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
