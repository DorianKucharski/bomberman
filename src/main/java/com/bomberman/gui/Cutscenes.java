package com.bomberman.gui;

import com.bomberman.assets.Assets;

import com.bomberman.game.Main;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Window;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Przerywniki filmowe
 */
public class Cutscenes {
    private Shader shader;
    private Camera camera;
    private TileSheet sheet;
    private Shader shader_font;
    private Camera camera_font;
    private TileSheet sheet_font;
    private DrawString black;
    private DrawString string1;
    private DrawString string2;

    int x, y;
    int time;

    public static final int LOSE = 1;
    public static final int NEXTLEVEL = 2;
    public static final int WIN = 3;

    public Cutscenes(Window window)   {
        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        sheet = new TileSheet("gui.png", 3);
        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);

        black = new DrawString(new Vector2f(0, 0), new Vector2f(700, 400));
        string1 = new DrawString(new Vector2f(-300, 150), new Vector2f(12, 12));
        string2 = new DrawString(new Vector2f(-100, 80), new Vector2f(12, 12));
    }

    private void renderMuchomor(int x, int y){
        Matrix4f transform = new Matrix4f();
        transform.identity().translate(x, y, 0).scale(70, 70, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        sheet.bindTile(shader, 0, 1);
        Assets.getModel().render();
    }

    private void renderDyna(int x, int y){
        Matrix4f transform = new Matrix4f();
        transform.identity().translate(x, y, 0).scale(70, 70, 1);
        shader.setUniform("projection", camera.getProjection().mul(transform));
        sheet.bindTile(shader, 1, 1);
        Assets.getModel().render();
    }

    public void startCutscenes(){
        time = 0;
        x = 0;
        y = 0;
    }

    public boolean renderNextLevel1(){
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");
        renderMuchomor(200, 0);
        renderDyna(x, 0);


        if (time < 120) {
            x = time*6 - 700;
        }

        if (time > 120)
        {
            string1.render(camera_font, sheet_font, shader_font, "YOUR PRINCESS IS IN ANOTHER CASTLE");

            if (time > 150 && time < 200) {
                string2 = new DrawString(new Vector2f(-100, 80), new Vector2f(12, 12));
                string2.render(camera_font, sheet_font, shader_font, "F##K");
            }
            if (time > 200) {
                x = -(time-200)*6;

                if (x < -100 && x > -250) {
                    string2 = new DrawString(new Vector2f(-100, 80), new Vector2f(12, 12));
                    string2.render(camera_font, sheet_font, shader_font, "HOLY F###### F###");
                }

                if (x < -300 && x > -450) {
                    string2 = new DrawString(new Vector2f(-300, 80), new Vector2f(12, 12));
                    string2.render(camera_font, sheet_font, shader_font, "F###### M##########");
                }

                if (x < -500 && x > -650) {
                    string2 = new DrawString(new Vector2f(-500, 80), new Vector2f(12, 12));
                    string2.render(camera_font, sheet_font, shader_font, "S### MY F###### ##### YOUR F###### N#####");
                }
            }
        }
        return !(time < 300);
    }

    public boolean renderNextLevel2(){
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");
        renderMuchomor(200, 0);
        renderDyna(x, 0);


        if (time < 120) {
            x = time*6 - 700;
        }

        if (time > 120)
        {
            string1.render(camera_font, sheet_font, shader_font, "YOUR PRINCESS IS IN ANOTHER CASTLE");

            if (time > 200) {
                x = -(time-200)*6;

                if (x < -100 && x > -800) {
                    string2 = new DrawString(new Vector2f(x, 100), new Vector2f(12, 12));
                    string2.render(camera_font, sheet_font, shader_font, "ONE DAY YOU WILL WAKE UP ");
                    string2 = new DrawString(new Vector2f(x, 80), new Vector2f(12, 12));
                    string2.render(camera_font, sheet_font, shader_font, "DEAD IN EMPTY OUTER SPACE");
                }

            }
        }
        return !(time < 300);
    }

    public boolean renderNextLevel3(){
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");
        renderMuchomor(200, 0);
        renderDyna(x, 0);


        if (time < 50) {
            x = time*6 - 700;
        }

        if (time > 200) x -= time/20;
        return !(time < 300);
    }

    public boolean renderNextLevel4(){
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");
        renderMuchomor(200+y, 0);
        renderDyna(x, 0);


        if (time < 50) {
            x = time*6 - 700;
        }

        if (time > 100){
            string2 = new DrawString(new Vector2f(x, 100), new Vector2f(12, 12));
            string2.render(camera_font, sheet_font, shader_font, "IM GONNA F### YOU UP");
        }
        if (time > 150) {
            x += time/8;
            y += time/10;
        }



        return !(time < 300);
    }

    public boolean lose(){
        string2 = new DrawString(new Vector2f(0-9*36, 36), new Vector2f(36, 36));
        string2.render(camera_font, sheet_font, shader_font, "PRZEGRALES");
        return !(time < 300);
    }

    public boolean win(){
        string2 = new DrawString(new Vector2f(0-9*36, 36), new Vector2f(36, 36));
        string2.render(camera_font, sheet_font, shader_font, "WYGRALES");
        return !(time < 300);
    }

    public boolean render(int id) {
        shader.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");


            if (id == 1) {
                return lose();
            }
            if (id == 2) {
                if (Main.getLevel() == 2) return renderNextLevel1();
                if (Main.getLevel() == 3) return renderNextLevel2();
                if (Main.getLevel() == 4) return renderNextLevel3();
                if (Main.getLevel() == 5) return renderNextLevel4();
            }

             if (id == 3) {
                  return win();
            }

        return false;
    }

    public void update(){
        time++;
    }
}
