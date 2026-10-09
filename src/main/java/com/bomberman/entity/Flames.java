package com.bomberman.entity;

import com.bomberman.collision.AABB;
import com.bomberman.collision.Collision;
import com.bomberman.io.Window;
import com.bomberman.render.Animation;
import com.bomberman.world.World;
import org.joml.Vector2f;
import com.bomberman.render.Camera;

/**
 * Klasa jednostki - Plomien
 */
public class Flames extends Entity {
    /**
     * Animacja srodkowych plomieni skierowanych w lewo
     */
    public static final int ANIM_LEFT_FLAME = 0;
    /**
     * Animacja konca plomieni skierowanych w lewo
     */
    public static final int ANIM_LEFT_END = 1;
    /**
     * Animacja srodkowych plomieni skierowanych w gore
     */
    public static final int ANIM_UP_FLAME = 2;
    /**
     * Animacja konca plomieni skierowanych w gore
     */
    public static final int ANIM_UP_END = 3;
    /**
     * Animacja srodkowych plomieni skierowanych w prawo
     */
    public static final int ANIM_RIGHT_FLAME = 4;
    /**
     * Animacja konca plomieni skierowanych w prawo
     */
    public static final int ANIM_RIGHT_END = 5;
    /**
     * Animacja srodkowych plomieni skierowanych w dol
     */
    public static final int ANIM_DOWN_FLAME = 6;
    /**
     * Animacja konca plomieni skierowanych w dol
     */
    public static final int ANIM_DOWN_END = 7;
    /**
     * Ilosc animacji
     */
    public static final int ANIM_SIZE = 8;
    /**
     * Czas utworzenia plomieni
     */
    public long time;
    /**
     * Kierunek plomienia
     */
    public int kierunek = 0;
    /**
     * Miejsce w rzadzie plomieni
     */
    public int rzad = 0;
    /**
     * Kolizja z innymi obiektami
     */
    public boolean kolizja = false;

    /**
     * Konstruktor plomienia
     * @param transform polozenie plomienia
     */
    public Flames(Transform transform) {
        super(ANIM_SIZE, transform);
        ID = Id.PLOMIEN;         // 3
        time = System.currentTimeMillis();
        setAnimation(ANIM_LEFT_FLAME, new Animation(4, 2, "flames/left/flame"));
        setAnimation(ANIM_LEFT_END, new Animation(4, 2, "flames/left/end"));
        setAnimation(ANIM_UP_FLAME, new Animation(4, 2, "flames/up/flame"));
        setAnimation(ANIM_UP_END, new Animation(4, 2, "flames/up/end"));
        setAnimation(ANIM_RIGHT_FLAME, new Animation(4, 2, "flames/right/flame"));
        setAnimation(ANIM_RIGHT_END, new Animation(4, 2, "flames/right/end"));
        setAnimation(ANIM_DOWN_FLAME, new Animation(4, 2, "flames/down/flame"));
        setAnimation(ANIM_DOWN_END, new Animation(4, 2, "flames/down/end"));
        bounding_box.solid = true;
    }

    /**
     * Aktualizacja stanu plomieni. Ustawia odpowiednie animacje dla danych plomieni.
     * Usuwa plomien po czasie dwoch sekund
     * @param delta roznica czasu pomiedzy aktualizacjamik
     * @param window ekran gry
     * @param camera kamera sledzaca obiekt
     * @param world swiat w ktorym znajduje sie obiekt
     */
        @Override
    public void update(float delta, Window window, Camera camera, World world) {

        if (kierunek == 1){
            useAnimation(ANIM_UP_FLAME);
            if (rzad == Bomb.flames_size -1 || kolizja)
                useAnimation(ANIM_UP_END);
        }

        if (kierunek == 2){
            useAnimation(ANIM_RIGHT_FLAME);
            if (rzad == Bomb.flames_size -1 || kolizja)
                useAnimation(ANIM_RIGHT_END);
        }

        if (kierunek == 3){
            useAnimation(ANIM_DOWN_FLAME);
            if (rzad == Bomb.flames_size -1 || kolizja)
                useAnimation(ANIM_DOWN_END);
        }
        if (kierunek == 4){
            useAnimation(ANIM_LEFT_FLAME);
            if (rzad == Bomb.flames_size -1 || kolizja)
                useAnimation(ANIM_LEFT_END);
        }

        if (time + 2000 < System.currentTimeMillis()) world.deleteEntity(this);
    }

    /**
     * Niszczenie plomieni
     * @param world swiat z ktorego obiekt ma byc usuniety
     */
    public void zniszcz(World world) {

    }

    /**
     * Kolizja plomieni w kafelkami swiata. Sprawdza czy plomien koliduje z kafelkami, jezeli tak ustawia
     * pole kolizja na true i zwraca true.
     * @param world swiat w ktorym sprawdzana ma byc kolizja
     * @return true jesli kolizja zachodzi, false jesli nie
     */
    public boolean collideWithTiles(World world) {

        AABB[] boxes = new AABB[25];
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    boxes[i + j * 5] = world.getTileBoundingBox((int) (((transform.pos.x / 2) + 0.5f) - (5 / 2)) + i, (int) (((-transform.pos.y / 2) + 0.5f) - (5 / 2)) + j);
                }
            }

            AABB box = null;
            for (int i = 0; i < boxes.length; i++) {
                if (boxes[i] != null) {
                    if (box == null) box = boxes[i];

                    Vector2f length1 = box.getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());
                    Vector2f length2 = boxes[i].getCenter().sub(transform.pos.x, transform.pos.y, new Vector2f());

                    if (length1.lengthSquared() > length2.lengthSquared()) {
                        box = boxes[i];
                    }
                }
            }

            if (box != null) {

                Collision data = bounding_box.getCollision(box);
                if (data.isIntersecting) {

                    if (ID == Id.PLOMIEN) {
                        transform.pos.z = -1;
                        kolizja = true;
                        return true;
                    }
                }
            }

    return false;
    }

    /**
     * Kolizja z innymi jednostkami. Sprawdza czy plomien koliduje z innymi jednostkami swiata. Jezeli tak ustawia
     * pole kolizja na true i zwraca true.
     * @param world swiat w ktorym odbywa sie kolizja
     * @return true jeli kolizja wystep;uje, false jesli nie
     */
    public boolean isColiding(World world){
        for (int i = 0; i < world.entities.size(); i++){
            Entity entity = world.entities.get(i);

            if (entity.ID == Id.SCIANA || entity.ID == Id.PRZECIWNIK)
            {
                Collision collision = bounding_box.getCollision(entity.bounding_box);
                if (collision.isIntersecting) {
                    kolizja = true;
                return true;
            }}}
        return false;
    }

    /**
     * Kopiowanie obiektu
     * @return kopia obiektu
     */
    public Entity copy(){
        Transform transformCopy = new Transform();
        transformCopy.pos.x = this.transform.pos.x;
        transformCopy.pos.y = this.transform.pos.y;
        Flames flamesCopy = new Flames(transformCopy);
        return flamesCopy;
    }

}
