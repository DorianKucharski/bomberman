package com.bomberman.entity;

import com.bomberman.render.Animation;

/**
 * Klasa jednostki - Boss
 */
public class Boss extends Enemy {
    /**
     * Konstruktor jednostki - Boss
     * @param transform polozenie jednostki
     */
    public Boss (Transform transform) {
        super(transform);
        setAnimation(ANIM_IDLE, new Animation(1, 1, "boss/idle"));
        setAnimation(ANIM_WALK, new Animation(2, 5, "boss/walking"));
        type = Type.BOSS;
        speed = 10;
    }

    /**
     * Metoda kopiujaca obiekt
     * @return zwraca kopie obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Boss bossCopy = new Boss(transformCopy);
        return bossCopy;
    }
}
