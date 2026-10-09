package com.bomberman.world;

/**
 * Kafelki gry. Kafelki to podstawowe obiekty swiata gry, takie jak podloze i bloki tworzące granice mapy.
 * Przechowywane i wczytywane sa w formie warstw, jedna warstwa to jeden rodzaj kafelkow.
 */
public class Tile {
	/**
	 * Tablica warstw kafelkow
	 */
	public static Tile tiles[] = new Tile[255];
	/**
	 * Number of Tales. Inkremenracja id warstw kafelkow
	 */
	public static byte not = 0;
	/**
	 * Warstwa podloza swiata (trawy)
	 */
	public static final Tile grass = new Tile("grass");
	/**
	 * Warstwa blokow
	 */
	public static final Tile test = new Tile("checker").setSolid();

	/**
	 * id warstwy
	 */
	private byte id;
	/**
	 * Jezeli true do kafelkow w danej warstwie zostanie dodana bryla obioektu, zatem bedzie wykrywana kolizja.
	 * Jezeli false kafelki nie beda mialy kolizji, jednostki beda mogly przez kafelek przechodzic (jak w przypadku podloza)
	 */
	private boolean solid;
	/**
	 * Nazwa tekstury kafelka
	 */
	private String texture;

	/**
	 * Konstruktor. Tworzy warstwe kafelkow i dodaje ja do tablicy warstw
	 * @param texture nazwa tekstury
	 */
	public Tile(String texture) {
		this.id = not;
		not++;
		this.texture = texture;
		this.solid = false;
		if (tiles[id] != null) throw new IllegalStateException("Tiles at [" + id + "] is already being used!");
		tiles[id] = this;
	}

	/**
	 * Ustawia dla danej warstwy solid na true
	 * @return zwraca warstwe
	 */
	public Tile setSolid() {
		this.solid = true;
		return this;
	}

	/**
	 * Sprawdza czy warstwa jest solid
	 * @return ture jezeli jest, false jezeli nie
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Getter numeru id warstwy
	 * @return numer id
	 */
	public byte getId() {
		return id;
	}

	/**
	 * Getter textury
	 * @return rekstura warstwy kafelkow
	 */
	public String getTexture() {
		return texture;
	}
}
