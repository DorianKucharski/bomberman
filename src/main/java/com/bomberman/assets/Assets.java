package com.bomberman.assets;

import com.bomberman.render.Model;

/**
 *  Model obiektow
 *
 */
public class Assets {
	/**
	 * Model assetu
	 */
	private static Model model;

	/**
	 * Metoda zwraca model
	 * @return model
	 */
	public static Model getModel() { return model; }

	/**
	 * Inicjalizacja rysowania modelu 2D
	 * Metoda tworzy tablice wierzcholkow, kierunek rysowania tekstur i indeksy wierzcholkow
	 */
	public static void initAsset() {
		float[] vertices = new float[] {
				-1f, 1f, 0,
				1f, 1f, 0,
				1f, -1f, 0,
				-1f, -1f, 0,
		};
		
		float[] texture = new float[] {
				0,0,
				1,0,
				1,1,
				0,1,
		};
		
		int[] indices = new int[] {
				0,1,2,
				2,3,0
		};
		
		model = new Model(vertices, texture, indices);
	}

	/**
	 * Metoda usuwa model
	 */
	public static void deleteAsset() {
		model = null;
	}
}
