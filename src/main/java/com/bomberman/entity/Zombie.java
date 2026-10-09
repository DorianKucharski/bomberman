package com.bomberman.entity;

import com.bomberman.io.Window;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.world.World;
import org.joml.Vector2f;

import static com.bomberman.entity.Player.ANIM_WALK_LEFT;
import static com.bomberman.entity.Player.ANIM_WALK_RIGHT;

/**
 * Klasa jednostki - Zombie
 */
public class Zombie extends Enemy{
    /**
     * Konstruktor jednostki
     * @param transform polozenie jednostki
     */
    public Zombie (Transform transform) {
        super(transform);
        setAnimation(ANIM_IDLE, new Animation(1, 1, "zombie/idle"));
        setAnimation(ANIM_WALK_RIGHT, new Animation(1, 1, "zombie/walking_right"));
        setAnimation(ANIM_WALK_LEFT, new Animation(1, 1, "zombie/walking_left"));
        type = Enemy.Type.ZOMBIE;
        speed = 2;
    }

    /**
     * Aktualiozacja stanu jednostki, opdowiada za poruszanie i animacje
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


        if (movement.x > 0) useAnimation(ANIM_WALK_RIGHT);
        if (movement.x < 0) useAnimation(ANIM_WALK_LEFT);

        else useAnimation(ANIM_IDLE);


    }

    /**
     * Kopiowanie obiektu
     * @return kopia obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Zombie ZombieCopy = new Zombie(transformCopy);
        return ZombieCopy;
    }
}
