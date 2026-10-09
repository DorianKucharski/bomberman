package com.bomberman.collision;

import org.joml.Vector2f;

/**
 * Axis-aligned bounding box
 * klasa bryly obiektow
 */
public class AABB {
	/**
	 * Wektor wskazujacy na srodek bryly
	 */
	public Vector2f center;
	/**
	 * Wektor wyrazajacy rozmiar bryly od srodka bryly
	 */
	public Vector2f	half_extent;
	/**
	 * Czy bryla moze byc przesuwana w przypadku kolizji
	 */
	public boolean solid = false;

	/**
	 * Konstruktor obiektu bryły
	 * @param center  srodek obiektu
	 * @param half_extent  rozmiar obiektu wyznaczony od srodka
	 */
	public AABB(Vector2f center, Vector2f half_extent) {
		this.center = center;
		this.half_extent = half_extent;
	}

	/**
	 * Kolizja z innym obiektem klasy AABB
	 * @param box2  drugi obiekt kolizji
	 * @return - zwraca obiekt klasy Collision
	 */
	public Collision getCollision(AABB box2) {
		Vector2f distance = box2.center.sub(center, new Vector2f());
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);
		
		distance.sub(half_extent.add(box2.half_extent, new Vector2f()));
		
		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	/**
	 * Kolizja z punktem klasy Vector2f
	 * @param point  obiekt klasy Vector2f
	 * @return zwraca obiekt klasy Collision
	 */
	public Collision getCollision(Vector2f point) {
		Vector2f distance = point.sub(center);
		distance.x = Math.abs(distance.x);
		distance.y = Math.abs(distance.y);
		
		distance.sub(half_extent);
		
		return new Collision(distance, distance.x < 0 && distance.y < 0);
	}

	/**
	 * Korygowanie pozycji w przypadku kolizji
	 * @param box2 obiekt z którym nastapila kolizja
	 * @param data obiekt klasy Collision zawierajacy informacje o kolizji
	 */
	public void correctPosition(AABB box2, Collision data) {
		Vector2f correctionDistance = box2.center.sub(center, new Vector2f());
		if (solid) return;
		if (data.distance.x > data.distance.y) {
			if (correctionDistance.x > 0) {
				center.add(data.distance.x, 0);
			}
			else {
				center.add(-data.distance.x, 0);
			}
		}
		else {
			if (correctionDistance.y > 0) {
				center.add(0, data.distance.y);
			}
			else {
				center.add(0, -data.distance.y);
			}
		}
	}

	/**
	 * Zwraca srodek obiektu
	 * @return srodek obiektu w postaci obiektu klasy Vector2f
	 */
	public Vector2f getCenter() {
		return center;
	}

	/**
	 * Zwraca wymiar obiektu od srodka
	 * @return rozmiar obiektu w postaci obiektu klasy Vector2f
	 */
	public Vector2f getHalfExtent() {
		return half_extent;
	}
}
