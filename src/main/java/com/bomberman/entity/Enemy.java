package com.bomberman.entity;

import com.bomberman.game.Main;
import com.bomberman.io.Window;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.world.World;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.io.Serializable;
import java.util.Random;

/**
 * Klasa bazowa jednostek przeciwnikow i klasa domyslnego przeciwnika.
 * Dziedziczy po klasie jednostka (Entity)
 */
public class Enemy extends Entity {
	/**
	 * Animacja spoczynku
	 */
	protected static final int ANIM_IDLE = 0;
	/**
	 * Animacja poruszania sie
	 */
	protected static final int ANIM_WALK = 1;
	/**
	 * Ilosc dostepnych animacji
	 */
	protected static int ANIM_SIZE = 4;
	/**
	 * Czas uzywany do sprawdzenia czy jednostka sie porusza
	 */
	private long czas_poruszanie;
	/**
	 * Czas od poprzedniej zmiany kierunku poruszania sie jednostki
	 */
	private long czas_zmiana_kierunku;
	/**
	 * Kierunek poruszania w osi X.
	 * 0 - brak poruszania sie w osi X
	 * 1 - porusza sie w prawo
	 * -1 - porusza sie w lewo
	 */
	public int x;
	/**
	 * Kierunek poruszania w osi Y.
	 * 0 - brak poruszania sie w osi Y
	 * 1 - porusza sie w gore
	 * -1 - porusza sie w dol
	 */
	public int y;
	/**
	 * Predkosc poruszania sie jednostki
	 */
	protected int speed;

	/**
	 * Typ przeciwnika
	 */
	public enum Type {
		/**
		 * Standardowy przeciwnik
		 */
		STANDARD,
		/**
		 * Gumba
		 */
		GUMBA,
		/**
		 * Pepe
		 */
		PEPE,
		/**
		 * Zombie
		 */
		ZOMBIE,
		/**
		 * Boss
		 */
		BOSS};
	/**
	 * Typ przeciwnika
	 */
	protected Type type;
	/**
	 * Generator losowy
	 */
	private Random generator;

	/**
	 * Konmstruktor przeciwnika.
	 * Tworzy przeciwnika, ustala jego animacje oraz predkosc
	 * @param transform polozenie przeciwnika
	 */
	public Enemy(Transform transform) {
		super(ANIM_SIZE, transform);
		ID  = Id.PRZECIWNIK;
		setAnimation(ANIM_IDLE, new Animation(1, 1, "enemy/idle"));
		setAnimation(ANIM_WALK, new Animation(2, 5, "enemy/walking"));
		transform_delta = new Transform();
		transform_delta.pos.x = transform.pos.x;
		transform_delta.pos.y = transform.pos.y;
		czas_poruszanie = System.currentTimeMillis();
		czas_zmiana_kierunku = System.currentTimeMillis();
		x = 1;
		y = 0;
		speed = 6;
		type = Type.STANDARD;

		generator = new Random();
	}

	/**
	 * Przeslonieta metoda klasy Entity. Przesuwa obiekt i ustawia animacje.
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

		if (movement.x != 0 || movement.y != 0)
			useAnimation(ANIM_WALK);
		else useAnimation(ANIM_IDLE);

	}

	/**
	 * Metoda sprawdza czy obiekt sie porusza. Stosowana jest do zmiany kierunku poruszania sie przeciwnikow
	 * @return zwraca true jesli jednostka sie porusza i false jest nie
	 */
	public boolean czySiePorusza() {
		boolean poruszanie = true;
		if (czas_poruszanie + 100 < System.currentTimeMillis()) {
			czas_poruszanie = System.currentTimeMillis();
			if ((transform_delta.pos.x == transform.pos.x) && x != 0) {
				poruszanie = false;
			}
			if ((transform_delta.pos.y == transform.pos.y) && y != 0) {
				poruszanie = false;
			}

			transform_delta.pos.x = transform.pos.x;
			transform_delta.pos.y = transform.pos.y;
		}

		return poruszanie;
	}

	/**
	 * Metoda sluzaca do odbijania jednostek od scian.
	 * Stosowana w przypadku jesli obiekt sie nie porusza.
	 * Zmienia kierunek poruszania przeciwnika w losowa strone, sprawdza czy dana strona nie jest koncem planszy
	 */
	public void odbijanieOdScian(){

			if (x != 0) {
				if (generator.nextBoolean()) {
					x *= -1;
				} else {
					if ((int) transform.pos.y == -2) {
						x = 0;
						y = -1;
					} else if ((int) transform.pos.y == -36) {
						x = 0;
						y = 1;
					} else {
						if (generator.nextBoolean()) {
							x = 0;
							y = -1;
						} else {
							x = 0;
							y = 1;
						}
					}
				}
			}
			else {
				if (generator.nextBoolean()){
					y *= -1;
				}
				else{
					if((int)transform.pos.x == 2){
						y = 0;
						x = 1;
					}
					else if((int)transform.pos.x == 36){
						y = 0;
						x = -1;
					}
					else {
						if (generator.nextBoolean()) {
							y = 0;
							x = -1;
						} else {
							y = 0;
							x = 1;
						}
					}
				}
			}
		}

	/**
	 * Metoda zmienia kierunek poruszania przeciwnika na losowy co dwie sekundy
	 */
	public void zmianaKierunku() {

			if (czas_zmiana_kierunku  + 2000 < System.currentTimeMillis()) {

					if (x != 0) {
						x = 0;
						if (generator.nextBoolean())
							y = 1;
						else
							y = -1;

					} else {
						y = 0;
						if (generator.nextBoolean())
							x = 1;
						else
							x = -1;
					}
					czas_zmiana_kierunku = System.currentTimeMillis();


			}


		}

	/**
	 * Metoda wywolywana do zniszczenia jednostki. Usuwa jednostke ze swiata i tworzy obiekt krew.
	 * @param world swiat w ktorym znajduje sie jednsotka
	 */
	public void zniszcz(World world) {
		Blood blood = new Blood(transform.copy());
		world.addEntity(blood);
		world.deleteEntity(this);
		Main.increaseScore(100);

	}

	/**
	 * Metoda kopiujaca obiekt
	 * @return zwraca kopie obiektu
	 */
	public Entity copy(){
		Transform transformCopy = new Transform();
		transformCopy.pos.x = this.transform.pos.x;
		transformCopy.pos.y = this.transform.pos.y;
		Enemy enemyCopy = new Enemy(transformCopy);
		return enemyCopy;
	}



}
