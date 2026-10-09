package com.bomberman.gui;

import com.bomberman.game.Main;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Input;
import com.bomberman.io.Window;
import org.joml.Vector2f;

/**
 * Gorny pasek wyswietlajacy ilosc zyc i punktow
 */
public class Bar {
    private Shader shader;
    private Camera camera;
    private TileSheet sheet;

    private Shader shader_font;
    private Camera camera_font;
    private TileSheet sheet_font;


    private Button bar;
    private DrawString head;
    private DrawString score;


    public Bar(Window window)   {
        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        sheet = new TileSheet("gui.png", 9);
        bar = new Button(new Vector2f(0, 326), new Vector2f(640, 34));




        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);
        head = new DrawString(new Vector2f(-600, 326), new Vector2f(24, 24));
        score = new DrawString(new Vector2f(400, 326), new Vector2f(24, 24));

}

    public void resizeCamera(Window window) {
        camera.setProjection(window.getWidth(), window.getHeight());
    }

    public void update(Input input) {

    }

    public void render() {
        shader.bind();
        bar.render(camera, sheet, shader);

        for (int i = 0; i < Main.getLives(); i++){
            head.changePosition(-600 + i *60, 326);

            head.render(camera_font, sheet_font, shader_font, "HEAD");
        }


        score.render(camera_font, sheet_font, shader_font, Integer.toString(Main.getScore()));


    }
}
