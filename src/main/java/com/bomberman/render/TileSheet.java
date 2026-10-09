package com.bomberman.render;

import org.joml.Matrix4f;

/**
 * Siatka tekstur.
 * Klasa odpowiedzialna za wczytywanie siatki tekstur i podpinanie odpowiedniej tekstury z siatki
 */
public class TileSheet {

	/**
	 * Tekstura
	 */
	private Texture texture;
	/**
	 * Skala tekstury
	 */
	private Matrix4f scale;
	/**
	 * Translacja tekstury w matrycy Matrix4f, wyznacza granice poszczegolnej tekstury w siatce
	 */
	private Matrix4f translation;
	/**
	 * Ilosc tekstur w siatce tekstur
	 */
	private int amountOfTiles;

	/**
	 * Konstruktor siatki tekstur. Wczytuje siatke tekstur z pliku, ustawia skale tekstur na podstawie ilosci tekstur
	 * w danej siatce
	 * @param texture nazwa pliku zawierajacego siatke tekstur
	 * @param amountOfTiles ilosc tekstur w siatce
	 */
	public TileSheet(String texture, int amountOfTiles) {
		this.texture = new Texture("sheets/" + texture);
		
		scale = new Matrix4f().scale(1.0f / (float)amountOfTiles);
		translation = new Matrix4f();
		
		this.amountOfTiles = amountOfTiles;
	}

	/**
	 * Podpina teksture z siatki tekstur o podanym w argumentach polozeniu
	 * @param shader shader tekstury
	 * @param x polozenie tekstury w siatce tekstur w osi x
	 * @param y polozenie tekstury w siatce tekstur w osi y
	 */
	public void bindTile(Shader shader, int x, int y) {
		scale.translate(x, y, 0, translation);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("texModifier", translation);
		texture.bind(0);
	}

}
