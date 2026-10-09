package com.bomberman.world;

import java.util.HashMap;

import com.bomberman.render.Camera;
import com.bomberman.render.Model;
import com.bomberman.render.Shader;
import com.bomberman.render.Texture;
import org.joml.Matrix4f;
import org.joml.Vector3f;

/**
 * Renderowanie kafelkow swiata
 */
public class TileRenderer {
	/**
	 * HashMapa przechowujaca wystapienia kafelkow na mapie w formacie (nazwa kafelka, jego tekstura)
	 */
	private HashMap<String, Texture> tileTextures;
	/**
	 * Model 2D kafelka
	 */
	private Model tileModel;

	/**
	 * Konstruktor.
	 * Tworzy model 2d pojedynczego kafelka i dodaje wszystkie wysąpienia do hashmapy kafelkow.
	 */
	public TileRenderer() {
		tileTextures = new HashMap<>();
		float[] vertices = new float[]{-1f, 1f, 0,
			1f, 1f, 0,
			1f, -1f, 0,
			-1f, -1f, 0,
		};
		
		float[] texture = new float[]{0, 0, 1, 0, 1, 1, 0, 1,};
		
		int[] indices = new int[]{0, 1, 2, 2, 3, 0};
		
		tileModel = new Model(vertices, texture, indices);
		
		for (int i = 0; i < Tile.tiles.length; i++) {
			if (Tile.tiles[i] != null) {
				if (!tileTextures.containsKey(Tile.tiles[i].getTexture())) {
					String tex = Tile.tiles[i].getTexture();
					tileTextures.put(tex, new Texture(tex + ".png"));
				}
			}
		}
	}

	/**
	 * Renderowanie kafelkow
	 * @param tile referencja do kafelka
	 * @param x polozenie w osi x
	 * @param y polozenie w osi y
	 * @param shader shader uzywany do renderu
	 * @param world swiat w ktorym znajduje sie kafelek
	 * @param cam kamera swiata
	 */
	public void renderTile(Tile tile, int x, int y, Shader shader, Matrix4f world, Camera cam) {
		shader.bind();
		if (tileTextures.containsKey(tile.getTexture())) tileTextures.get(tile.getTexture()).bind(0);
		
		Matrix4f tile_pos = new Matrix4f().translate(new Vector3f(x * 2, y * 2, 0));
		Matrix4f target = new Matrix4f();
		
		cam.getProjection().mul(world, target);
		target.mul(tile_pos);
		
		shader.setUniform("sampler", 0);
		shader.setUniform("projection", target);
		
		tileModel.render();
	}
}
