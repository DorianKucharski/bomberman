package com.bomberman.entity;

import com.bomberman.io.Audio;
import com.bomberman.io.Window;
import com.bomberman.world.World;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;

/**
 * Klasa jednostki - Bomba
 */
public class Bomb extends Entity {
    /**
     * Animacja spoczynku
     */
    private static final int ANIM_IDLE = 0;
    /**
     * Animacja wybuchu
     */
    private static final int ANIM_BOOM = 1;
    /**
     * Ilosc animacji
     */
    private static final int ANIM_SIZE = 2;
    /**
     * Czas utworzenia obiektu
     */
    public long time = 0;

    /**
     * Rozmiar plomieni
     */
    protected static int flames_size = 4;
    /**
     * Czy momba wybuchla
     */
    private boolean boom = false;

    /**
     * Konstruktor obiektu bomba
     * @param transform polozenie obiektu
     */
    public Bomb(Transform transform) {
        super(ANIM_SIZE, transform);
        ID = Id.BOMBA; // 1
        bounding_box.solid = true;

        if (time == 0) time = System.currentTimeMillis();
        setAnimation(ANIM_IDLE, new Animation(3, 2, "bomb/idle"));
        setAnimation(ANIM_BOOM, new Animation(4, 2, "bomb/boom"));
    }

    /**
     * Metoda przesuwania i renderowania obiektu
     * @param delta delta czasu odswiezaniem
     * @param window okno w ktorym uruchomiona jest gra
     * @param camera kamera ktora sledzi ruch gracza
     * @param world swiat w ktorym znajduje sie obiekt
     */
    @Override
    public void update(float delta, Window window, Camera camera, World world) {
        if (time + 2000 < System.currentTimeMillis()) {
            flames(world);
            useAnimation(ANIM_BOOM);
        }
        if (time + 4000 < System.currentTimeMillis()) world.deleteEntity(this);
    }

    /**
     * Metoda tworzaca jednostki klasy flames w momencie wybuchu bomby.
     * @param world swiat w ktorym tworzony jest obiekt
     */
    public void flames(World world){
            if (!boom) {
                Audio.playAudio("music/explosion.wav");
                //PRAWO
                for (int i = 0; i < flames_size; i++) {
                    Transform transform_flames1 = new Transform();
                    transform_flames1.pos.x = transform.pos.x + 2 + 2 * i;
                    transform_flames1.pos.y = transform.pos.y;
                    Flames flame1 = new Flames(transform_flames1);
                    flame1.kierunek = 2;
                    flame1.rzad = i;
                    world.addEntity(flame1);
                    if (flame1.collideWithTiles(world)) break;
                    if (flame1.isColiding(world)) break;
                }
                //LEWO
                for (int i = 0; i < flames_size; i++) {
                    Transform transform_flames2 = new Transform();
                    transform_flames2.pos.x = transform.pos.x - 2 - 2 * i;
                    transform_flames2.pos.y = transform.pos.y;
                    Flames flame2 = new Flames(transform_flames2);
                    flame2.kierunek = 4;
                    flame2.rzad = i;
                    world.addEntity(flame2);
                    if (flame2.collideWithTiles(world)) break;
                    if (flame2.isColiding(world)) break;
                }
                //GORA
                for (int i = 0; i < flames_size; i++) {
                    Transform transform_flames3 = new Transform();
                    transform_flames3.pos.x = transform.pos.x;
                    transform_flames3.pos.y = transform.pos.y + 2 + 2 * i;
                    Flames flame3 = new Flames(transform_flames3);
                    flame3.kierunek = 1;
                    flame3.rzad = i;
                    world.addEntity(flame3);
                    if (flame3.collideWithTiles(world)) break;
                    if (flame3.isColiding(world)) break;
                }
                //DOL
                for (int i = 0; i < flames_size; i++) {
                    Transform transform_flames4 = new Transform();
                    transform_flames4.pos.x = transform.pos.x;
                    transform_flames4.pos.y = transform.pos.y - 2 - 2 * i;
                    Flames flame4 = new Flames(transform_flames4);
                    flame4.kierunek = 3;
                    flame4.rzad = i;
                    world.addEntity(flame4);
                    if (flame4.collideWithTiles(world)) break;
                    if (flame4.isColiding(world)) break;
                }
                boom = true;
                time = System.currentTimeMillis() - 2100;
            }
    }

    /**
     * Metoda uruchamiana w momencie niszczenia obiektu
     * @param world swiat w ktorym istnieje obiekt
     */
    public void zniszcz(World world) {
        flames(world);
    }

    /**
     * Metoda kopiujaca obiekt
     * @return zwraca kopie obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Bomb bombCopy = new Bomb(transformCopy);
        bombCopy.time = time;
        return bombCopy;
    }
}
