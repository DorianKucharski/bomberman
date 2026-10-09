package com.bomberman.entity;

import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.io.Window;
import com.bomberman.world.World;
import org.joml.Vector3f;
import java.util.Random;

/**
 * Klasa jednostki - Krew
 */
public class Blood extends Entity {
    /**
     * Id animacji
     */
    private static final int ANIM_1 = 0;
    /**
     * Ilosc animacji
     */
    private static final int ANIM_SIZE = 1;
    /**
     * Generator losowy
     */
    private Random random = new Random();

    /**
     * Konstruktor obiektu krew
     * @param transform - okresla polozenie obiektu
     */
    public Blood (Transform transform) {
        super(ANIM_SIZE, transform);
        ID  = Id.BLOOD;
        setAnimation(ANIM_1, new Animation(1, 1, "blood/1"));
        bounding_box.solid = true;
        transform.scale = new Vector3f(random.nextFloat()*10, random.nextFloat()*10, 0);

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
        useAnimation(ANIM_1);
    }

    /**
     * Metoda kopiowania obiektu
     * @return zwraca kopie obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        transformCopy.scale.x = this.transform.scale.x;
        transformCopy.scale.y = this.transform.scale.y;
        Blood bloodCopy = new Blood(transformCopy);
        return bloodCopy;
    }



}
