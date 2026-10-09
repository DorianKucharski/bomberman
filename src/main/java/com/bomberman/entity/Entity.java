package com.bomberman.entity;

import com.bomberman.collision.AABB;
import com.bomberman.collision.Collision;
import com.bomberman.render.Animation;
import com.bomberman.render.Camera;
import com.bomberman.render.Shader;
import com.bomberman.assets.Assets;
import com.bomberman.io.Window;
import com.bomberman.world.World;
import org.joml.*;
import java.io.Serializable;
import java.lang.Math;

/**
 * Abstrakcyjna klasa bazowa wszystkich klas jednostek gry.
 * Implementuje serializacje
 */
public abstract class Entity implements Serializable {

	/**
	 * Bryla jednostki
	 */
	transient public AABB bounding_box;
	/**
	 * Tablica animacji jednostki
	 */
	transient protected Animation[] animations;
	/**
	 * Okresla ktora animacja jest obecnie uzywana
	 */
	transient private int use_animation;

	/**
	 * Okresla rodzaj jednostki
	 */
	public enum Id {
		/**
		 * Gracz
		 */
		GRACZ,
		/**
		 * Bomba
		 */
		BOMBA,
		/**
		 * Plomien
		 */
		PLOMIEN,
		/**
		 * Sciana
		 */
		SCIANA,
		/**
		 * Przeciwnicy
		 */
		PRZECIWNIK,
		/**
		 * Krew
		 */
		BLOOD,
		/**
		 * Bog
		 */
		GOD;
	}

	/**
	 * Typ jednostki
	 */
	public Id ID;

	/**
	 * Polozenie jednostki
	 */
	public Transform transform;
	/**
	 * Poprzednie polozenie jednostki
	 */
	public Transform transform_delta;

	/**
	 * Konstruktor obiektu, ustawia jego animacje, polozenie i bryle.
	 * @param max_animations ilosc animacji
	 * @param transform polozenie obiektu
	 */
	public Entity(int max_animations, Transform transform) {
		this.animations = new Animation[max_animations];
		this.transform = transform;
		this.use_animation = 0;
		bounding_box = new AABB(new Vector2f(transform.pos.x, transform.pos.y), new Vector2f(transform.scale.x, transform.scale.y));

	}

	/**
	 * Ustawia/dodaje animacje
	 * @param index indeks ustawianej animacji
	 * @param animation animacja
	 */
	protected void setAnimation(int index, Animation animation) {
		animations[index] = animation;
	}

	/**
	 * Uaktywnia animacje
	 * @param index indeks animacji
	 */
	public void useAnimation(int index) {
		this.use_animation = index;
	}

	/**
	 * Metoda poruszajaca obiekt.
	 * Odpowiada rowniez za scinanie zakretow
	 * @param direction wektor wyrazajacy kierunek poruszania
	 */
	public void move(Vector2f direction) {


		if (transform.pos.x % 4 > 0 && transform.pos.x % 4 < 2 && direction.x >= 0){
			if (direction.y != 0)
				direction.x += 0.1 - direction.x;
		}

		if (transform.pos.x % 4 > 2 && transform.pos.x % 4 > 0 && direction.x <= 0){
		if (direction.y != 0)
			direction.x -=0.1 + direction.x;
		}


		if (Math.abs(transform.pos.y) % 4 > 2 && Math.abs(transform.pos.y) % 4 > 0 && direction.y >=0){
			if (direction.x != 0)
				direction.y +=0.1 - direction.y;
		}

		if (Math.abs(transform.pos.y) % 4 > 0 && Math.abs(transform.pos.y) % 4 < 2 && direction.y <= 0){
			if (direction.x != 0)
				direction.y -=0.1 + direction.y;
		}

		transform.pos.add(new Vector3f(direction, 0));

		bounding_box.getCenter().set(transform.pos.x, transform.pos.y);
	}

	/**
	 * Metoda sprawdzajaca czy jednostka moze sie poruszac w danym kierunku
	 * @param direction kierunek w ktorym ma poruszac sie dana jednostka
	 * @return zwraca true jezeli obiekt moze sie poruszac, false jesli nie
	 */
	public boolean czyDaSieIsc(Vector2f direction){
		boolean daSie = false;
		if (transform.pos.x % 4 > 0.5 && transform.pos.x % 4 < 2){

				daSie = true;
		}

		if (transform.pos.x % 4 > 2 && transform.pos.x % 4 > 0.5){

				daSie = true;
		}


		if (Math.abs(transform.pos.y) % 4 > 2 && Math.abs(transform.pos.y) % 4 > 0.5){

				daSie = true;
		}

		if (Math.abs(transform.pos.y) % 4 > 0.5 && Math.abs(transform.pos.y) % 4 < 2){

				daSie = true;
		}
		return daSie;
	}

	/**
	 * Metoda kolizji z kafelkami mapy. Jezeli kolizja nastepuje obiekt nie moze przejsc przez kafelek
	 * @param world swiat w ktorym sprawdzana ma byc kolizja
	 * @return zwraca zawsze true
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
					bounding_box.correctPosition(box, data);
					transform.pos.set(bounding_box.getCenter(), 0);
				}

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

				data = bounding_box.getCollision(box);
				if (data.isIntersecting) {

					bounding_box.correctPosition(box, data);
					transform.pos.set(bounding_box.getCenter(), 0);

				}

			}

		return true;
	}

	/**
	 * Abstrakcyjna metoda aktualiozacji stanu jednostki
	 * @param delta roznica czasu pomiedzy aktualizacjamik
	 * @param window ekran gry
	 * @param camera kamera sledzaca obiekt
	 * @param world swiat w ktorym znajduje sie obiekt
	 */
	public abstract void update(float delta, Window window, Camera camera, World world);

	/**
	 * Metoda wyswietlajaca jednostke
	 * @param shader shader renderowania
	 * @param camera kamera sledzaca obraz
	 * @param world swiat ktory ma byc renderowany
	 */
	public void render(Shader shader, Camera camera, World world) {
		Matrix4f target = camera.getProjection();
		target.mul(world.getWorldMatrix());
		shader.bind();
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", transform.getProjection(target));
		animations[use_animation].bind(0);
		Assets.getModel().render();
	}


	/**
	 * Metoda kolizji jednostek z innymi jednostkami
	 * W zaleznosci od typow obiektow ktore ze soba koliduja, uniemozliwia im przejscie lub niszczy dany obiekt
	 * @param entity druga jednostka z ktora nastepuje kolizja
	 * @param world swiat w ktorym nastepuje kolizja
	 */
	public void collideWithEntity(Entity entity, World world) {

						Collision collision = bounding_box.getCollision(entity.bounding_box);

						if (collision.isIntersecting) {
							collision.distance.x /= 2;
							collision.distance.y /= 2;

							entity.bounding_box.correctPosition(bounding_box, collision);
						if (entity.ID == Id.SCIANA || entity.ID == Id.BOMBA || entity.ID == Id.PRZECIWNIK){
							bounding_box.correctPosition(entity.bounding_box, collision);
							transform.pos.set(bounding_box.getCenter().x, bounding_box.getCenter().y, 0);
						}


							entity.transform.pos.set(entity.bounding_box.getCenter().x, entity.bounding_box.getCenter().y, 0);
							if (entity.ID == Id.PLOMIEN && (this.ID == Id.PRZECIWNIK || this.ID == Id.GRACZ || this.ID == Id.SCIANA || this.ID == Id.BOMBA))
							{
								if ((Math.abs(transform.pos.x - entity.transform.pos.x) < 1.4) && (Math.abs(transform.pos.y - entity.transform.pos.y) < 1.4))
									{
										this.zniszcz(world);
									}
							}

							if (entity.ID == Id.PRZECIWNIK &&  this.ID == Id.GRACZ)
							{
								if ((Math.abs(transform.pos.x - entity.transform.pos.x) < 2.5) && (Math.abs(transform.pos.y - entity.transform.pos.y) < 2.5))
								{
									this.zniszcz(world);
								}
							}
					}
	}

	/**
	 * Metoda sprawdzajaca czy dana jednostka nie znajduje sie wewnatrz innej jednostki.
	 * Sluzy do sprawdzania czy gracz wyszedl z bryly bomby ktora postawil
	 * @param entity drugi obiekt
	 * @return zwraca true jesli obiekty na siebie nie nachodza false jesli tak
	 */
	public boolean outOfBomb(Entity entity){
		boolean x, y;
		x = y = false;
		if ((entity.transform.pos.x - 1 > this.transform.pos.x) ||  (entity.transform.pos.x +1 < this.transform.pos.x)) x = true;
		if ((entity.transform.pos.y - 1 > this.transform.pos.y) ||  (entity.transform.pos.y +1 < this.transform.pos.y)) y = true;
		return x || y;

	}

	/**
	 * Zwraca polozenie w osi X
	 * @return Wspolrzedna X
	 */
	public float getX(){
		return transform.pos.x;
	}

	/**
	 * Zwraca polozenie w osi Y
	 * @return Wspolrzedna Y
	 */
	public float getY(){
		return  transform.pos.y;
	}

	/**
	 * Metoda usuwajaca obiekt z listy obiektow aktywnych w swiecie gry
	 * @param world swiat z ktorego obiekt ma byc usuniety
	 */
	public void zniszcz(World world) {
		world.deleteEntity(this);
	}

	/**
	 * Abstrakcyjna metoda kopiowania obiektow
	 * @return zwraca kopie obiektu
	 */
	public abstract Entity copy();

}
