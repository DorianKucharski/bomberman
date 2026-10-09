package com.bomberman.collision;

import org.joml.Vector2f;

/**
 * Klasa kolizji
 */
public class Collision {
	/**
	 * Dystans pomiedzy dwoma obiektami wchodzacymi w kolizje
	 */
	public Vector2f distance;
	/**
	 * Czy obiekty na siebie wchodza
	 */
	public boolean isIntersecting;

	/**
	 * Konstruktor
	 * @param distance dystans pomiedzy dwoma obiektami
	 * @param intersects czy kolizja zachodzi
	 */
	public Collision(Vector2f distance, boolean intersects) {
		this.distance = distance;
		this.isIntersecting = intersects;
	}
}
