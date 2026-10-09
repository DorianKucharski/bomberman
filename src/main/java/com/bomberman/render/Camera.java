package com.bomberman.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Kamera
 */
public class Camera {

	/**
	 * Pozycja kamery wyrazona obiektem klasy Vector3f
	 */
	private Vector3f position;
	/**
	 * Projekcja kamery na macierzy Matrix4f
	 */
	private Matrix4f projection;

	/**
	 * Konstruktor kamery, tworzy kamere i ustawia ja w punkcie 0, 0, swiata.
	 * @param width szerokosc projekcji kamery
	 * @param height wysokosc projekcji kamery
	 */
	public Camera(int width, int height) {
		position = new Vector3f(0,0,0);
		setProjection(width, height);
	}

	/**
	 * Ustawia projekcje kamery. Na podstawie szerokosci i wysokosci obszaru objetego kamera, tworzy
	 * macierz Matrix4f określająca czterema punktami(gora, dol, lewo, prawo) granice pokazywania przez kamere.
	 * @param width szerokosc
	 * @param height wysokosc
	 */
	public void setProjection(int width, int height) {
		projection = new Matrix4f().setOrtho2D(-width/2, width/2, -height/2, height/2);
	}

	/**
	 * Ustawia pozycje kamery
	 * @param position pozycja kamery zapisana w obiekcie Vector3f
	 */
	public void setPosition(Vector3f position) {
		this.position.set(position);
	}

	/**
	 * Getter pozycji kamery
	 * @return pozycja kamery
	 */
	public Vector3f getPosition() { return position; }

	/**
	 * Getter projekcji kamery
	 * @return zwraca projekcje w macierzy Matrix4f
	 */
	public Matrix4f getProjection() {
		return projection.translate(position, new Matrix4f());
	}
}
