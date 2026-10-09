package com.bomberman.gui;

import com.bomberman.io.Window;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import org.joml.Vector2f;

import java.util.Random;

/**
 * Intro gry
 */
public class Intro {
    private Shader shader;
    private Camera camera;
    private TileSheet sheet;
    private Shader shader_font;
    private Camera camera_font;
    private TileSheet sheet_font;
    private DrawString black;
    private DrawString loading;
    private DrawString pasek;
    private DrawString garbage;
    private String garbage_content;
    private Random generator;

    int x;
    int time = 0;


    public Intro(Window window)   {
        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        sheet = new TileSheet("gui.png", 3);
        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);

        black = new DrawString(new Vector2f(0, 0), new Vector2f(700, 400));
        loading = new DrawString(new Vector2f(0 - 6* 48, 200), new Vector2f(48, 48));
        garbage_content = new String();
        generator = new Random();

    }

    public boolean render(){
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");
        loading.render(camera_font, sheet_font, shader_font, "LOADING");

        pasek = new DrawString(new Vector2f(0, -100), new Vector2f(time, 12));
        pasek.render(camera_font, sheet_font, shader_font, "WHITE");

        garbage = new DrawString(new Vector2f(0 - (garbage_content.length()-1)*12, -150), new Vector2f(12, 12));
        garbage.render(camera_font, sheet_font, shader_font, garbage_content);

        if (time < 100) garbage_content = "LADOWANIE TEKSTUR";
        if (time > 100 && time < 200) garbage_content = "LADOWANIE MODELI";
        if (time > 200 && time < 300) garbage_content = "LADOWANIE EASTER EGGOW";
        if (time > 300 && time < 400) garbage_content = "SCIAGANIE TROJANOW";
        if (time > 400 && time < 500) garbage_content = "WYSYLANIE HASEL ZAPISANYCH W PRZEGLADARCE";
        if (time > 600) garbage_content = "PRZERWA NA SZLUGA";



    if (generator.nextBoolean()) {
        if (generator.nextBoolean()) time += 1;
        else time += 10;
    }
    else time +=0;

    if (time >= 650) return true;
    else return false;

    }
}
