package com.bomberman.world;

import java.awt.image.BufferedImage;
import com.bomberman.game.Main;
import com.bomberman.io.Window;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import com.bomberman.collision.AABB;
import com.bomberman.render.Camera;
import com.bomberman.render.Shader;
import org.joml.*;
import com.bomberman.entity.*;

/**
 * Swiat gry
 */
public class World {
	private int viewX;
	private int viewY;
	private byte[] tiles;
	private AABB[] bounding_boxes;
	public  List<Entity> entities;
	private  List<Entity> entitiesToDelete;
	public Player player;
	private int width;
	private int height;
	private int scale;
	private int enemies = 1;
	private Matrix4f world;

	/**
	 * Konstruktor swiata, tworzy swiat na podstawie pliku png, oraz wstawia do swiata kafelki wedle schematu w pliku.
	 * @param world nazwa swiata
	 */
	public World(String world) {
		try {

			BufferedImage tile_sheet = ImageIO.read(new File("./levels/" + world + "/tiles.png"));

			width = tile_sheet.getWidth();
			height = tile_sheet.getHeight();
			scale = 34;

			this.world = new Matrix4f().setTranslation(new Vector3f(0));
			this.world.scale(scale);

			int[] colorTileSheet = tile_sheet.getRGB(0, 0, width, height, null, 0, width);

			tiles = new byte[width * height];
			bounding_boxes = new AABB[width * height];
			entities = new ArrayList<>();
			entitiesToDelete = new ArrayList<>();


			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int red = (colorTileSheet[x + y * width] >> 16) & 0xFF;

					Tile t;
					try {
						t = Tile.tiles[red];
					} catch (ArrayIndexOutOfBoundsException e) {
						t = null;
					}

					if (t != null) setTile(t, x, y);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Dodaje do swiata gry jednostki pobierajac ich polozenie z pliku danego swiata.
	 * Przeciwnicy ladowani z entities, zniszczalne cegly z pliku bricks.
	 * Gracz jest dodawany w lewym gornym rogu mapy.
	 * @param world nazwa folderu swiata
	 */
	public void createWorld( String world) {
		try {

			BufferedImage entity_sheet = ImageIO.read(new File("./levels/" + world + "/entities.png"));
			BufferedImage bricks_sheet = ImageIO.read(new File("./levels/" + world + "/bricks.png"));

			int[] colorEntitySheet = entity_sheet.getRGB(0, 0, width, height, null, 0, width);
			int[] colorBricks = bricks_sheet.getRGB(0, 0, width, height, null, 0, width);
			Transform transform;

			transform = new Transform();
			transform.pos.x = 2;
			transform.pos.y = -2;
			player = new Player(transform);
			entities.add(player);
			enemies = 0;

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int entity_alpha = (colorEntitySheet[x + y * width] >> 24) & 0xFF;
					int bricks_alpha = (colorBricks[x + y * width] >> 24) & 0xFF;		//

					if (entity_alpha > 0) {
						transform = new Transform();
						transform.pos.x = x * 2;
						transform.pos.y = -y * 2;
						Enemy enemy;
						if(Main.getLevel() < 5) {
							if (enemies % 4 == 1) enemy = new Pepe(transform);
							else if (enemies % 4 == 2) enemy = new Zombie(transform);
							else if (enemies % 4 == 3) enemy = new Gumba(transform);
							else enemy = new Enemy(transform);
						}
						else enemy = new Boss(transform);
						entities.add(enemy);
						enemies++;
						System.out.println(Main.getLevel());

					}

					if (bricks_alpha > 0) {
						transform = new Transform();
						transform.pos.x = x * 2;
						transform.pos.y = -y * 2;
						 Brick brick = new Brick(transform);
						entities.add(brick);
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Liczy aktualny widok okna
	 * @param window okno gry
	 */
	public void calculateView(Window window) {
		viewX = (window.getWidth() / (scale * 2)) + 4;
		viewY = (window.getHeight() / (scale * 2)) + 4;
	}

	/**
	 * Getter matrycy swiata
	 * @return matryca swiata
	 */
	public Matrix4f getWorldMatrix() {
		return world;
	}

	/**
	 * Renderowanie swiata.
	 * Renderowane na poczatku sa tekstury podloza, nastepnie kafelki, następnie poszczegolne jednostki.
	 * @param render obiekt klasy TileRenderer odpowiedzialny za modele graficzne, przypisanie tekstur i ich renderowanie
	 * @param shader shader
	 * @param cam kamera
	 */
	public void render(TileRenderer render, Shader shader, Camera cam) {

		int posX = (int) cam.getPosition().x / (scale * 2);
		int posY = (int) cam.getPosition().y / (scale * 2);

		for (int i = 0; i < viewX; i++) {
			for (int j = 0; j < viewY; j++) {
				Tile t = getTile(i - posX - (viewX / 2) + 1, j + posY - (viewY / 2));
				if (t != null)
					render.renderTile(t, i - posX - (viewX / 2) + 1, -j - posY + (viewY / 2), shader, world, cam);
			}
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.SCIANA) entity.render(shader, cam, this);
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.BLOOD) entity.render(shader, cam, this);
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.BOMBA) entity.render(shader, cam, this);
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.PRZECIWNIK) entity.render(shader, cam, this);
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.PLOMIEN) entity.render(shader, cam, this);
		}

		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.GOD) entity.render(shader, cam, this);
		}
		for (Entity entity : entities) {
			if (entity.ID == Entity.Id.GRACZ) entity.render(shader, cam, this);
		}


	}

	/**
	 * Aktualizowanie stanu swiata
	 * @param delta delta czasu aktualizacji stanu
	 * @param window okno gry
	 * @param camera kamera gry
	 * @param world swiat do zaktualizowania
	 */
	public void update(float delta, Window window, Camera camera,World world) {

		for (int i = 0; i < entitiesToDelete.size(); i++) {
			entities.remove(entitiesToDelete.get(i));
		}

		for (int i = 0; i < entities.size(); i++){
			entities.get(i).update(delta, window, camera, this);
		}


		for (int i = 0; i < entities.size(); i++) {
			entities.get(i).collideWithTiles(this);
			for (int j = i + 1; j < entities.size(); j++) {
				if (entities.get(i).ID == Entity.Id.GRACZ && entities.get(j).ID == Entity.Id.BOMBA && !entities.get(i).outOfBomb(entities.get(j)))
					break;
				else
					entities.get(i).collideWithEntity(entities.get(j), world);

			}
			entities.get(i).collideWithTiles(this);
		}
	}

	/**
	 * Korygowanie pozycji kamery
	 * @param camera kamera gry
	 * @param window okno gry
	 */
	public void correctCamera(Camera camera, Window window) {
		Vector3f pos = camera.getPosition();

		int w = -width * scale * 2;
		int h = height * scale * 2;

		if (pos.x > -(window.getWidth() / 2) + scale) pos.x = -(window.getWidth() / 2) + scale;
		if (pos.x < w + (window.getWidth() / 2) + scale) pos.x = w + (window.getWidth() / 2) + scale;

		if (pos.y < (window.getHeight() / 2) - scale) pos.y = (window.getHeight() / 2) - scale;
		if (pos.y > h - (window.getHeight() / 2) - scale) pos.y = h - (window.getHeight() / 2) - scale;
	}

	/**
	 * Ustawianie kafelkow swiata.
	 * Jezeli kafelek jest solid to ustawia jego bryle, jezeli nie ustawia jedynie jego polozenie
	 * @param tile kafelek
	 * @param x polozenie w osi x
	 * @param y polozenie w osi y
	 */
	public void setTile(Tile tile, int x, int y) {
		tiles[x + y * width] = tile.getId();
		if (tile.isSolid()) {
			bounding_boxes[x + y * width] = new AABB(new Vector2f(x * 2, -y * 2), new Vector2f(1, 1));
		} else {
			bounding_boxes[x + y * width] = null;
		}
	}

	/**
	 * Zwraca kafelek swiata na podstawie jego polozenia
	 * @param x polozenie w osi x
	 * @param y polozenie w osi y
	 * @return obiekt Tile
	 */
	public Tile getTile(int x, int y) {
		try {
			return Tile.tiles[tiles[x + y * width]];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Zwraca bryle kafleka swiata znajdujacego sie w polozeniu przekazanym w argumentach
	 * @param x polozenie w osi x
	 * @param y polozenie w osi y
	 * @return bryla kafelka (obiekt AABB)
	 */
	public AABB getTileBoundingBox(int x, int y) {
		try {
			return bounding_boxes[x + y * width];
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Zwraca skale wszystkich elementow swiata
	 * @return skala
	 */
	public int getScale() {
		return scale;
	}

	/**
	 * Dodaje jednostke do swiata
	 * @param entity jednostka ktora ma byc dodana
	 */
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	/**
	 * Usuwa dana jednostke ze swiata
	 * @param entity referencja do jednostki ktora ma byc usunieta
	 */
	public void deleteEntity(Entity entity){
		entitiesToDelete.add(entity);
	}

	/**
	 * Usuwa wszystkie jednostki znajdujace sie na swiecie.
	 * Wykonuje to poprzez dodanie jednostek do listy obiektow do usuniecia. Zas te sa usuwane przy nastepnej
	 * aktualizacji swiata.
	 */
	public void clearEntities(){
		for (int i = 0; i < entities.size(); i++) {
			entitiesToDelete.add(entities.get(i));
		}
	}


	/**
	 * Zwraca ilosc przeciwnikow w swiecie
	 * @return ilosc przeciwnikow
	 */
	public int countEnemies(){
		int j = 0;
		for (int i = 0; i < entities.size(); i++){
			if (entities.get(i).ID == Entity.Id.PRZECIWNIK) j++;
			if (entities.get(i).ID == Entity.Id.GOD) j++;
		}
		return  j;
	}

	/**
	 * Zabija wszystkich przeciwnikow
	 */
	public void killEnemies(){

		for (int i = 0; i < entities.size(); i++){
			if (entities.get(i).ID == Entity.Id.PRZECIWNIK) deleteEntity(entities.get(i));
		}

	}


}
