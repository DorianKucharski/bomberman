package com.bomberman.gui;



import com.bomberman.game.Main;
import com.bomberman.game.Saves;
import com.bomberman.io.ScreenShots;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Input;
import com.bomberman.io.Window;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_F9;

/**
 * Menu wczytywania i zapisywania stanow gry
 */
public class SavesMenu {
    private Shader shader;
    private Camera camera;
    private TileSheet sheet;
    private Shader shader_font;
    private Camera camera_font;
    private TileSheet sheet_font;
    private Button slot1Button;
    private Button slot2Button;
    private Button slot3Button;
    private Button slot4Button;
    private Button exitButton;
    private DrawString slot1String;
    private DrawString slot2String;
    private DrawString slot3String;
    private DrawString slot4String;
    private DrawString exitString;

    private DrawString pic1;
    private DrawString pic2;
    private DrawString pic3;
    private DrawString pic4;

    private TileSheet pic1_sheet;
    private TileSheet pic2_sheet;
    private TileSheet pic3_sheet;
    private TileSheet pic4_sheet;

    private boolean menu = false;
    public int mode = 0;
    public final int SAVE = 1;
    public final int LOAD = 0;

    private Saves save1;
    private Saves save2;
    private Saves save3;
    private Saves save4;

    public SavesMenu(Window window)   {
        save1 = new Saves("save1");
        save2 = new Saves("save2");
        save3 = new Saves("save3");
        save4 = new Saves("save4");

        save1 = save1.loadFromFile();
        save2 = save2.loadFromFile();
        save3 = save3.loadFromFile();
        save4 = save4.loadFromFile();

        loadPictures();

        shader = new Shader("gui");
        camera = new Camera(window.getWidth(), window.getHeight());
        sheet = new TileSheet("gui.png", 9);


        slot1Button = new Button(new Vector2f(0, 96), new Vector2f(400, 48)){
            @Override
            public void output() {
                if (mode == 0) {
                    save1.load(Main.getWorld());
                    hide();
                    Main.pauza = false;
                }
                else{
                    save1.save(Main.getWorld());
                    save1.saveToFile();
                    reload();
                }
            }
        };
        slot2Button = new Button(new Vector2f(0, 0), new Vector2f(400, 48)){
            @Override
            public void output() {
                if (mode == 0) {
                    save2.load(Main.getWorld());
                    hide();
                    Main.pauza = false;
                }
                else{
                    save2.save(Main.getWorld());
                    save2.saveToFile();
                    reload();
                }
            }
        };

        slot3Button = new Button(new Vector2f(0, -96), new Vector2f(400, 48)){
            @Override
            public void output() {
                if (mode == 0) {
                    save3.load(Main.getWorld());
                    hide();
                    Main.pauza = false;
                }
                else{
                    save3.save(Main.getWorld());
                    save3.saveToFile();
                    reload();
                }
            }
        };
        slot4Button = new Button(new Vector2f(0, -192), new Vector2f(400, 48)){
            @Override
            public void output() {
                if (mode == 0) {
                    save4.load(Main.getWorld());
                    hide();
                    Main.pauza = false;
                }
                else{
                    save4.save(Main.getWorld());
                    save4.saveToFile();
                    reload();
                }
            }
        };
        exitButton = new Button(new Vector2f(0, -288), new Vector2f(240, 48)){
            @Override
            public void output() {
              //  Main.exit();
                hide();
            }
        };


        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);


        exitString = new DrawString(new Vector2f(-6*24, -288), new Vector2f(24, 24));

    }

    public void reload(){
        save1 = save1.loadFromFile();
        save2 = save2.loadFromFile();
        save3 = save3.loadFromFile();
        save4 = save4.loadFromFile();

    }

    public void loadPictures(){
        if (save1.isLoaded()){
            pic1_sheet = new TileSheet("../../saves/save1.png", 2);

        }
        if (save2.isLoaded()){
            pic2_sheet = new TileSheet("../../saves/save2.png", 2);
        }
        if (save3.isLoaded()){
            pic3_sheet = new TileSheet("../../saves/save3.png", 2);
        }


        if (save4.isLoaded()){
            pic4_sheet = new TileSheet("../../saves/save4.png", 2);
        }



    }

    public void update(Input input) {
        slot1Button.update(input);
        slot2Button.update(input);
        slot3Button.update(input);
        slot4Button.update(input);
        exitButton.update(input);
        if (input.isKeyPressed(GLFW_KEY_ESCAPE)) hide();

    }

    public void show(){
        menu = true;
    }

    public void hide(){
        menu = false;
    }

    public boolean state(){
        return  menu;
    }


    public boolean render() {
        shader.bind();
        slot1Button.render(camera, sheet, shader);
        slot2Button.render(camera, sheet, shader);
        slot3Button.render(camera, sheet, shader);
        slot4Button.render(camera, sheet, shader);
        exitButton.render(camera, sheet, shader);

        if (save1.isLoaded()) {
            slot1String = new DrawString(new Vector2f(-380, 120), new Vector2f(10, 10));
            slot1String.render(camera_font, sheet_font, shader_font, "POZIOM: " + Integer.toString(save1.getLevel()));
            slot1String = new DrawString(new Vector2f(-380, 95), new Vector2f(10, 10));
            slot1String.render(camera_font, sheet_font, shader_font, "PUNKTY: " + Integer.toString(save1.getPoints()));
            slot1String = new DrawString(new Vector2f(-380, 70), new Vector2f(10, 10));
            slot1String.render(camera_font, sheet_font, shader_font, "ZYCIA: " + Integer.toString(save1.getLives()));

            LocalDateTime date = save1.getDate();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedDate = date.format(formatterDate);
            String formattedTime = date.format(formatterTime);


            slot1String = new DrawString(new Vector2f(20, 120), new Vector2f(10, 10));
            slot1String.render(camera_font, sheet_font, shader_font, formattedDate);
            slot1String = new DrawString(new Vector2f(20, 95), new Vector2f(10, 10));
            slot1String.render(camera_font, sheet_font, shader_font, formattedTime);


            DrawString test = new DrawString(new Vector2f(272, 95), new Vector2f(40, 40));
            test.render(camera_font, pic1_sheet, shader_font, "PICTURE");
        }
        else {
            slot1String = new DrawString(new Vector2f(-4*24, 96), new Vector2f(24, 24));
            slot1String.render(camera_font, sheet_font, shader_font, "WOLNE");
        }

        int i = - 95;
        if (save2.isLoaded()) {
            slot2String = new DrawString(new Vector2f(-380, 120 +i), new Vector2f(10, 10));
            slot2String.render(camera_font, sheet_font, shader_font, "POZIOM: " + Integer.toString(save2.getLevel()));
            slot2String = new DrawString(new Vector2f(-380, 95 +i), new Vector2f(10, 10));
            slot2String.render(camera_font, sheet_font, shader_font, "PUNKTY: " + Integer.toString(save2.getPoints()));
            slot2String = new DrawString(new Vector2f(-380, 70 +i), new Vector2f(10, 10));
            slot2String.render(camera_font, sheet_font, shader_font, "ZYCIA: " + Integer.toString(save2.getLives()));

            LocalDateTime date = save2.getDate();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedDate = date.format(formatterDate);
            String formattedTime = date.format(formatterTime);


            slot2String = new DrawString(new Vector2f(20, 120+i), new Vector2f(10, 10));
            slot2String.render(camera_font, sheet_font, shader_font, formattedDate);
            slot2String = new DrawString(new Vector2f(20, 95+i), new Vector2f(10, 10));
            slot2String.render(camera_font, sheet_font, shader_font, formattedTime);


            DrawString test = new DrawString(new Vector2f(272, 95+i), new Vector2f(40, 40));
            test.render(camera_font, pic2_sheet, shader_font, "PICTURE");
        }
        else {
            slot2String = new DrawString(new Vector2f(-4*24, 96+i), new Vector2f(24, 24));
            slot2String.render(camera_font, sheet_font, shader_font, "WOLNE");
        }

        i += -95;
        if (save3.isLoaded()) {
            slot3String = new DrawString(new Vector2f(-380, 120 +i), new Vector2f(10, 10));
            slot3String.render(camera_font, sheet_font, shader_font, "POZIOM: " + Integer.toString(save3.getLevel()));
            slot3String = new DrawString(new Vector2f(-380, 95 +i), new Vector2f(10, 10));
            slot3String.render(camera_font, sheet_font, shader_font, "PUNKTY: " + Integer.toString(save3.getPoints()));
            slot3String = new DrawString(new Vector2f(-380, 70 +i), new Vector2f(10, 10));
            slot3String.render(camera_font, sheet_font, shader_font, "ZYCIA: " + Integer.toString(save3.getLives()));

            LocalDateTime date = save3.getDate();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedDate = date.format(formatterDate);
            String formattedTime = date.format(formatterTime);


            slot3String = new DrawString(new Vector2f(20, 120+i), new Vector2f(10, 10));
            slot3String.render(camera_font, sheet_font, shader_font, formattedDate);
            slot3String = new DrawString(new Vector2f(20, 95+i), new Vector2f(10, 10));
            slot3String.render(camera_font, sheet_font, shader_font, formattedTime);


            DrawString test = new DrawString(new Vector2f(272, 95+i), new Vector2f(40, 40));
            test.render(camera_font, pic3_sheet, shader_font, "PICTURE");
        }
        else {
            slot3String = new DrawString(new Vector2f(-9*24, 96+i), new Vector2f(24, 24));
            slot3String.render(camera_font, sheet_font, shader_font, "WOLNY SLOT");
        }

        i += -95;
        if (save4.isLoaded()) {
            slot4String = new DrawString(new Vector2f(-380, 120 +i), new Vector2f(10, 10));
            slot4String.render(camera_font, sheet_font, shader_font, "POZIOM: " + Integer.toString(save4.getLevel()));
            slot4String = new DrawString(new Vector2f(-380, 95 +i), new Vector2f(10, 10));
            slot4String.render(camera_font, sheet_font, shader_font, "PUNKTY: " + Integer.toString(save4.getPoints()));
            slot4String = new DrawString(new Vector2f(-380, 70 +i), new Vector2f(10, 10));
            slot4String.render(camera_font, sheet_font, shader_font, "ZYCIA: " + Integer.toString(save4.getLives()));

            LocalDateTime date = save4.getDate();
            DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("HH:mm:ss");
            String formattedDate = date.format(formatterDate);
            String formattedTime = date.format(formatterTime);


            slot4String = new DrawString(new Vector2f(20, 120+i), new Vector2f(10, 10));
            slot4String.render(camera_font, sheet_font, shader_font, formattedDate);
            slot4String = new DrawString(new Vector2f(20, 95+i), new Vector2f(10, 10));
            slot4String.render(camera_font, sheet_font, shader_font, formattedTime);


            DrawString test = new DrawString(new Vector2f(272, 95+i), new Vector2f(40, 40));
            test.render(camera_font, pic4_sheet, shader_font, "PICTURE");
        }
        else {
            slot4String = new DrawString(new Vector2f(-9*24, 96+i), new Vector2f(24, 24));
            slot4String.render(camera_font, sheet_font, shader_font, "WOLNY SLOT");
        }


        exitString.render(camera_font, sheet_font, shader_font, "POWROT");

        return menu;


    }
}
