package com.bomberman.gui;

import com.bomberman.assets.Assets;

import com.bomberman.game.Main;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Window;
import com.bomberman.scores.Score;
import com.bomberman.scores.Scores;
import org.joml.Matrix4f;
import org.joml.Vector2f;

/**
 * Tablica wynikow
 */
public class ScoreBoard {
    /**
     * Shader czcionki
     */
    private Shader shader_font;
    /**
     * Kamera czcionki
     */
    private Camera camera_font;
    /**
     * Siatka tekstur czcionki
     */
    private TileSheet sheet_font;
    /**
     * Czarne tlo
     */
    private DrawString black;
    /**
     * Napis
     */
    private DrawString string1;
    /**
     * Napis
     */
    private DrawString string2;
    /**
     * Nazwa gracza
     */
    private String name = new String();
    /**
     * Wskaznik na okno
     */
    private Window window;
    /**
     * Obiekt przechowujacy wyniki
     */
    private Scores scores = new Scores();
    /**
     * Miejsce zajete przez gracza
     */
    private int miejsce = -1;
    /**
     * Czy skonczono wpisywanie imienia
     */
    private boolean done = false;
    /**
     * Czy zaladowano wyniki
     */
    private boolean loaded = false;
    /**
     * Czy zapisano wyniki
     */
    private boolean saved = false;


    /**
     * Konstruktor tablicy wynikow
     * @param window wskaznik na okno
     */
    public ScoreBoard(Window window)   {
        this.window = window;
        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);

        black = new DrawString(new Vector2f(0, 0), new Vector2f(700, 400));
        string1 = new DrawString(new Vector2f(-300, 150), new Vector2f(12, 12));
        string2 = new DrawString(new Vector2f(-100, 80), new Vector2f(12, 12));
    }

    /**
     * Glowna metoda renderujaca tablice wynikow.
     * Przechodzi przez poszczegolne ekrany tablicy wynikow. Wczytuje wyniki i zapisuje.
     * @return true po zakonczeniu renderowania, false jesli trwa jeszcze renderowanie
     */
    public boolean render() {
        if (!loaded){
            scores = Scores.load();
            loaded = true;
        }
        if (!done) renderScore();
        else {

            if (!saved){
                miejsce = scores.add(Main.getScore(), name.substring(0, name.length() - 1));
                scores.save();
                saved = true;
            }
            rednerTable();

            return true;
        }
        return false;
    }

    /**
     * Renderowanie ekranu z uszyskanym wynikiem i wpisywaniem imienia
     */
    public void renderScore(){

        shader_font.bind();
            black.render(camera_font, sheet_font, shader_font, "BLACK");

            int size = ("UZYSKALES: " + Main.getScore() + " PUNKTOW").length();
            string1 = new DrawString(new Vector2f(0 - size * 23, 300), new Vector2f(24, 24));
            string1.render(camera_font, sheet_font, shader_font, "UZYSKALES: " + Main.getScore() + " PUNKTOW");

            if (scores.record(Main.getScore())) {
                string1 = new DrawString(new Vector2f(0 - 33 * 12, 200), new Vector2f(12, 12));
                string1.render(camera_font, sheet_font, shader_font, "ZALAPALES SIE DO TABLICY WYNIKOW");

                string1 = new DrawString(new Vector2f(0 - 15 * 12, 100), new Vector2f(12, 12));
                string1.render(camera_font, sheet_font, shader_font, "WPISZ SWOJE IMIE");

                string1 = new DrawString(new Vector2f(0 - name.length() * 24, 0), new Vector2f(24, 24));
                string1.render(camera_font, sheet_font, shader_font, name);

                int i = window.getInput().returnkey();
                if (i != 0) {
                    if (i == 259) name = name.substring(0, name.length() - 1);
                    if ((i >= 65 && i <= 90) || i == 32 || i == 257) name += (char) i;
                    if (i == 257) done = true;
                }
            } else {
                string1 = new DrawString(new Vector2f(0 - 35 *12, 100), new Vector2f(12, 12));
                string1.render(camera_font, sheet_font, shader_font, "NIE ZALAPALES SIE DO TABLICY WYNIKOW");
                if (window.getInput().returnkey() == 254) done = true;
            }
    }

    /**
     * Renderowanie tablicy wynikow
     */
    public void rednerTable(){

        if (!scores.areLoaded()){
            scores = Scores.load();
        }
        shader_font.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");



        string1 = new DrawString(new Vector2f(-5*84, 300), new Vector2f(84, 36));
        string1.render(camera_font, sheet_font,shader_font, "WYNIKI");

        for (int i = 0; i < scores.size(); i++){
            int size = (scores.getPoints(i) + "  " + scores.getName(i)).length();
            if ( i == miejsce) string2 = new DrawString(new Vector2f(0-size*24, 200- i *50), new Vector2f(24, 24));
            else string2 = new DrawString(new Vector2f(0-size*12, 200 - i *50), new Vector2f(12, 12));
            string2.render(camera_font, sheet_font, shader_font, scores.getPoints(i) + "  " + scores.getName(i));
        }
    }

    /**
     * Ustawianie warunkow poczatkowych, resetowanie zmiennych.
     */
    public void clearName(){
        name = new String();
        miejsce = -1;
        done = false;
        loaded = false;
        saved = false;
    }



}
