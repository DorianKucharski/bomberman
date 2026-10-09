package com.bomberman.entity;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.Serializable;

/**
 * Klasa przechowujaca informacje o polozeniu i skali jednostki.
 * Informacje przechowywane sa w postaci obiektow klasy Vector3f
 */
public class Transform implements Serializable {

	public Vector3f pos;
	public Vector3f scale;

	/**
	 * Konstruktor bezparametrowy.
	 * Tworzy obiekt i inicjalizuje wektory, ale nie przypisuje im wartosci.
	 */
	public Transform() {
		pos = new Vector3f();
		scale = new Vector3f(1, 1, 1);
	}

	/**
	 * Metoda przypisuje polozenie i skale zapisane w obiekcie do matrycy Matrix4f
	 * @param target matryca Matrix4f
	 * @return przeksztalcona matryca Matrix4f
	 */
	public Matrix4f getProjection(Matrix4f target) {
		target.translate(pos);
		target.scale(scale);
		return target;
	}

	/**
	 * Metoda kopiujaca obiekt
	 * @return zwraca skopiowany obiekt
	 */
	public Transform copy(){
		Transform transform = new Transform();
		transform.pos.x = pos.x;
		transform.pos.y = pos.y;
		transform.pos.z = pos.z;
		return transform;
	}
}
