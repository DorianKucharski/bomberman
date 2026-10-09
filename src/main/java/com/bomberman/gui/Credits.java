package com.bomberman.gui;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Window;
import org.joml.Vector2f;


import java.util.ArrayList;
import java.util.List;

/**
 * Autorzy gry
 */
public class Credits {

    private Shader shader_font;
    private Camera camera_font;
    private TileSheet sheet_font;

    private DrawString black;
    private List<DrawString> strings1 = new ArrayList<>();
    private List<DrawString> strings2 = new ArrayList<>();
    private List<DrawString> strings3 = new ArrayList<>();
    private List<String> credits = new ArrayList<>();
    private String author1 = "DORIAN KUCHARSKI";
    private String author2 = "MACIEJ KOZIEL";


    /**
     * Konstruktor napisow
     * @param window wskaznik na okno
     */
    public Credits(Window window)   {

        shader_font = new Shader("gui");
        camera_font = new Camera(window.getWidth(), window.getHeight());
        sheet_font = new TileSheet("font.png", 18);
        black = new DrawString(new Vector2f(0, 0), new Vector2f(700, 400));
        createStrings();
    }

    /**
     * Utworzenie tablic napisow
     */
    public void createStrings(){
        credits.add("SCENARIUSZ");
        credits.add("REZYSERIA");
        credits.add("DIALOGI");
        credits.add("MONTAZ");
        credits.add("PRODUKCJA");
        credits.add("PROGRAMISCI ROZGRYWKI");
        credits.add("PROGRAMISCI SILNIKA");
        credits.add("PROGRAMISCI QUESTOW");
        credits.add("PROGRAMISCI INTERFEJSU");
        credits.add("RYSUNKI KONCEPCYJNE");
        credits.add("ARTYSCI POSTACI");
        credits.add("ARTYSCI LOKACJI");
        credits.add("PROJEKTANCI LOKACJI ");
        credits.add("ARTYSCI OSWIETLENIA");
        credits.add("SEKWENCJE FILMOWE");
        credits.add("ANNIMACJE POSTACI");
        credits.add("ARTYSCI TECHNICZNI");
        credits.add("EFEKTY WIZUALNE");
        credits.add("OPRAWA DZWIEKOWA");
        credits.add("LOKALIZACJA");
        credits.add("ANGIELSKA WERSJA JEZYKOWA");
        credits.add("KONTROLA JAKOSCI ROZGRYWKI");
        credits.add("KONTROLA JAKOSCI GRY");
        credits.add("KONTROLA JAKOSCI OSWIETLENIA");
        credits.add("KONTROLA JAKOSCI DZWIEKU");
        credits.add("MUZYKA");
        credits.add("UDZWIEKOWIENIE");

        for (int i = 0; i < credits.size(); i++)
        {
            strings1.add(new DrawString(new Vector2f(0 - credits.get(i).length()*24, -200 - i*200), new Vector2f(24, 24)));
            strings2.add(new DrawString(new Vector2f(0 - author1.length()*12, -250 - i*200), new Vector2f(12, 12)));
            strings3.add(new DrawString(new Vector2f(0 - author2.length()*12, -280 - i*200), new Vector2f(12, 12)));
        }
        start();

    }

    /**
     * Ustawienie poczatkowego ustawienia napisow
     */
    public void start(){
        for (int i = 0; i < credits.size(); i++)
        {
            strings1.get(i).changePosition(0 - credits.get(i).length()*24,-200 - i*200);
            strings2.get(i).changePosition(0 - author1.length()*12,-250 - i*200);
            strings3.get(i).changePosition(0 - author1.length()*12,-280 - i*200);
        }
    }


    /**
     * Przesuwanie napisow
     */
    public void update(){
        for (int i = 0; i < credits.size(); i++){

            strings1.get(i).move(0, 2);
            strings2.get(i).move(0, 2);
            strings3.get(i).move(0, 2);
        }
    }

    /**
     * Renderowanie napisow
     */
    public void render() {
        shader_font.bind();
        black.render(camera_font, sheet_font, shader_font, "BLACK");

        for (int i = 0; i < credits.size(); i++){
            strings1.get(i).render(camera_font, sheet_font, shader_font, credits.get(i));
            strings2.get(i).render(camera_font, sheet_font, shader_font, author1);
            if (i != 25) strings3.get(i).render(camera_font, sheet_font, shader_font, author2);
        }



    }
}
