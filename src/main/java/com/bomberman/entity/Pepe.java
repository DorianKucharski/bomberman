package com.bomberman.entity;

import com.bomberman.game.Main;
import com.bomberman.io.Window;
import com.bomberman.render.*;
import com.bomberman.world.World;
import org.joml.Vector2f;

import java.util.Random;

/**
 * Klasa jednostki - Pepe
 */
public class Pepe extends Enemy{

    /**
     * Czas depresji
     */
    private long timeOfAnxiety;

    /**
     * Konstruktor jednostki Pepe
     * @param transform polozenie jednostki
     */
    public Pepe (Transform transform) {
        super(transform);
        setAnimation(ANIM_IDLE, new Animation(1, 1, "pepe/idle"));
        setAnimation(ANIM_WALK, new Animation(2, 5, "pepe/walking"));
        type = Enemy.Type.PEPE;
        speed = 5;
        timeOfAnxiety = System.currentTimeMillis();


    }

    /**
     * Kopiuje jednostke
     * @return kopia jednostki
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Pepe pepeCopy = new Pepe(transformCopy);
        return pepeCopy;
    }

    /**
     * Aktualizacja stanu jednostki, odpowiada za poruszanie jednostki i ustawienie animacji.
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

        if (timeOfAnxiety + 5000 < System.currentTimeMillis()){
            if (timeOfAnxiety + 6000 < System.currentTimeMillis()){
                timeOfAnxiety = System.currentTimeMillis();
            }
            move(movement);

        }

        if (movement.x != 0 || movement.y != 0)
            useAnimation(ANIM_WALK);
        else useAnimation(ANIM_IDLE);


    }
}
