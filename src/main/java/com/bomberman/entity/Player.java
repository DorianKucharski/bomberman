package com.bomberman.entity;

import com.bomberman.game.Main;
import com.bomberman.io.Audio;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.io.Window;
import com.bomberman.world.World;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import javax.xml.transform.TransformerFactory;

/**
 * Klasa jednostki - Gracz
 */
public class Player extends Entity {
	/**
	 * Animacja spoczynku
	 */
	public static final int ANIM_IDLE = 0;
	/**
	 * Animacja poruszania sie w prawo
	 */
	public static final int ANIM_WALK_RIGHT = 1;
	/**
	 * Animacja poruszania sie w lewo
	 */
	public static final int ANIM_WALK_LEFT = 2;
	/**
	 * Animacja poruszania sie w gore
	 */
	public static final int ANIM_WALK_UP = 3;
	/**
	 * Animacja poruszania sie w dol
	 */
	public static final int ANIM_WALK_DOWN = 4;
	/**
	 * Ilosc animacji
	 */
	public static final int ANIM_SIZE = 5;
	/**
	 * Przechowuje czas wlaczenia GodMode
	 */
	public long timeGodMode;

	
	public Player(Transform transform) {
		super(ANIM_SIZE, transform);
		ID  = Id.GRACZ;
		setAnimation(ANIM_IDLE, new Animation(1, 1, "player/idle"));
		setAnimation(ANIM_WALK_RIGHT, new Animation(3, 10, "player/walking/right"));
		setAnimation(ANIM_WALK_LEFT, new Animation(3, 10, "player/walking/left"));
		setAnimation(ANIM_WALK_UP, new Animation(3, 10, "player/walking/up"));
		setAnimation(ANIM_WALK_DOWN, new Animation(3, 10, "player/walking/down"));
	}

	/**
	 * Wlacza GodMode na 2 sekundy. Na ten czas zmienia typ jednostki z Gracz na God, dzieki czemu gracz nie moze
	 * byc zniszczony.
	 */
	public void godmode(){
		if (System.currentTimeMillis() < timeGodMode + 2000){
			this.ID = Id.GOD;
		}
		else{
			this.ID = Id.GRACZ;
		}
	}

	/**
	 * Niszczenie jednostki. Gracz jest teleportowany na poczatek mapy, ilosc zyc jest dekrementowana, ustawiany
	 * jest czas godmode, przez co przez dwie sekundy gracz jest niesmiertelny.
	 * @param world swiat z ktorego obiekt ma byc usuniety
	 */
	public void zniszcz(World world) {
		world.player.transform.pos.x = 2;
		world.player.transform.pos.y = -2;
		Main.decreaseLives();
		timeGodMode = System.currentTimeMillis();
		godmode();
	}

	/**
	 * Aktualizacja stanu jednostki. Odpowiada za sterowanie graczem, jego poruszanie,podkladanie bomb, ustawianie
	 * animacji poruszania w zaleznosci od kierunkui i sledzenie przez kamere.
	 * @param delta roznica czasu pomiedzy aktualizacjamik
	 * @param window ekran gry
	 * @param camera kamera sledzaca obiekt
	 * @param world swiat w ktorym znajduje sie obiekt
	 */
	@Override
	public void update(float delta, Window window, Camera camera, World world) {
		Vector2f movement = new Vector2f();

		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_A)) movement.add(-10 * delta, 0);
		
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_D)) movement.add(10 * delta, 0);

		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_W)) movement.add(0, 10 * delta);
		
		if (window.getInput().isKeyDown(GLFW.GLFW_KEY_S)) movement.add(0, -10 * delta);

		if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_TAB)) {
			Blood blood = new Blood(transform.copy());
			world.addEntity(blood);
		}


		if (window.getInput().isKeyPressed(GLFW.GLFW_KEY_SPACE)) {

			Transform transform_bomb = new Transform();

			double x = Math.round(transform.pos.x);
			double y = Math.round(transform.pos.y);

			if (x % 2 != 0) {
				if (x%4 == 1)
					x++;
				else x--;
			}

			if (y % 2 != 0) {
				if (y%4 == -1)
					y--;
				else y++;
			}

			transform_bomb.pos.x = (float)x;
			transform_bomb.pos.y = (float)y;

			Bomb bomb = new Bomb(transform_bomb);
			world.addEntity(bomb);
			Audio.playAudio("music/bomb.wav");
		}
		
		move(movement);
		godmode();
		

		if (movement.x > 0) useAnimation(ANIM_WALK_RIGHT);
		if (movement.x < 0) useAnimation(ANIM_WALK_LEFT);
		if (movement.y > 0) useAnimation(ANIM_WALK_UP);
		if (movement.y < 0) useAnimation(ANIM_WALK_DOWN);
		if (movement.x == 0 && movement.y == 0) useAnimation(ANIM_IDLE);


		camera.getPosition().lerp(transform.pos.mul(-world.getScale(), new Vector3f()), 0.05f);
	}

	/**
	 * Zwiekszenie rozmiaru jednostki
	 */
	public void grow(){
		Vector3f vector3f = new Vector3f(1.0f, 1.0f, 1.0f);
		vector3f.x += transform.scale.x;
		vector3f.y += transform.scale.y;
		vector3f.z += transform.scale.z;
		transform.scale = vector3f;
	}

	/**
	 * Zmniejszenie rozmiaru jednostki
	 */
	public void shrink(){
		Vector3f vector3f = new Vector3f(-1.0f, -1.0f, -1.0f);
		vector3f.x += transform.scale.x;
		vector3f.y += transform.scale.y;
		vector3f.z += transform.scale.z;
		transform.scale = vector3f;
	}

	/**
	 * Ustawienie normalnego rozmiaru jednostki
	 */
	public void normalize(){
		transform.scale  = new Vector3f(1.0f, 1.0f, 1.0f);
	}

	/**
	 * Kopiowanie jednostki
	 * @return kopia obiektu
	 */
	public Entity copy(){
		Transform transformCopy = new Transform();
		transformCopy.pos.x = this.transform.pos.x;
		transformCopy.pos.y = this.transform.pos.y;
		Player playerCopy = new Player(transformCopy);
		return playerCopy;
	}



}
