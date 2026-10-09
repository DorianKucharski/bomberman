package com.bomberman.entity;

import com.bomberman.io.Window;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.world.World;
import org.joml.Vector2f;

import static com.bomberman.entity.Player.ANIM_WALK_LEFT;
import static com.bomberman.entity.Player.ANIM_WALK_RIGHT;

/**
 * Klasa jednostki - Gumba
 */
public class Gumba extends Enemy {
    /**
     * Ilosc zyc
     */
    int lives = 2;
    /**
     * Czas wlaczenia GodMode
     */
    public long timeGodMode;

    /**
     * Konstruktor jednostki klasy Gumba
     * @param transform polozenie jednostki
     */
    public Gumba(Transform transform) {
        super(transform);
        setAnimation(0, new Animation(1, 1, "gumba/idle"));
        setAnimation(1, new Animation(2, 5, "gumba/walking"));
        setAnimation(2, new Animation(1, 1, "gumba/nearly_dead_idle"));
        setAnimation(3, new Animation(2, 5, "gumba/nearly_dead_walking"));
        type = Type.GUMBA;
        speed = 2;

    }

    /**
     * Aktualizacja stanu przeciwnika. Odpowiada za poruszanie, model sztucznej inteligencji i ustawienie odpowiedniej
     * animacji.
     * @param delta roznica czasu pomiedzy klatkami animacji
     * @param window okno gry
     * @param camera kamera ktora sledzi jednostke
     * @param world swiat w ktorym istnieje jednostka
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        Vector2f movement = new Vector2f();

        if (czySiePorusza()) {
            zmianaKierunku();
        }
        else odbijanieOdScian();

        movement.add(x * speed * delta, y * speed * delta);

        move(movement);

        if(lives == 2){
            if (movement.x != 0 || movement.y != 0) useAnimation(1);
            else useAnimation(0);
        }
        else{
            if (movement.x != 0 || movement.y != 0) useAnimation(3);
            else useAnimation(2);
        }

        godmode();

    }

    /**
     * Zniszczenie jednostki. Dekrementuje ilosc zyc, a jesli wynosi ona 0 usuwa obiekt.
     * @param world swiat w ktorym znajduje sie jednsotka
     */
    @Override
    public void zniszcz(World world) {

        timeGodMode = System.currentTimeMillis();
        godmode();
        lives--;
        if (lives == 0) {

            super.zniszcz(world);
        }
    }

    /**
     * Wlacza i wylacza GodMode w zaleznosci od pola timeGodMode.
     */
    public void godmode(){
        if (System.currentTimeMillis() < timeGodMode + 2000){
            this.ID = Id.GOD;
        }
        else{
            this.ID = Id.PRZECIWNIK;
        }
    }

    /**
     * Kopiuje obiekt
     * @return kopia obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Gumba gumbaCopy = new Gumba(transformCopy);
        return gumbaCopy;
    }
}
