package com.bomberman.entity;

import com.bomberman.game.Main;
import com.bomberman.io.Window;
import com.bomberman.world.World;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;

/**
 * Klasa jednostki - Cegla
 */
public class Brick extends Entity {
    /**
     * Animacja spoczynku
     */
    private static final int ANIM_IDLE = 0;
    /**
     * Ilosc animacji
     */
    private static final int ANIM_SIZE = 2;

    /**
     * Konstruktor obiektu cegla
     * @param transform polozenie obiektu
     */
    public Brick(Transform transform) {
        super(ANIM_SIZE, transform);
        ID = Id.SCIANA;     //
        setAnimation(ANIM_IDLE, new Animation(1, 1, "brick"));
        bounding_box.solid = true;
    }


    @Override
    public void update(float delta, Window window, Camera camera, World world) {
    }

    /**
     * Metoda uruchamiana w momencie niszczenia obiektu
     * Usuwa obiekt ze swiata
     * @param world swiat w ktorym znajduje sie obiekt
     */
    public void zniszcz(World world) {
        world.deleteEntity(this);
        Main.increaseScore(10);
    }

    /**
     * Metoda kopiujaca obiekt
     * @return zwraca kopie obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Brick brickCopy = new Brick(transformCopy);
        return brickCopy;
    }




}
