package com.bomberman.render;

import com.bomberman.io.Timer;

/**
 * Animacje
 */
public class Animation {
	/**
	 * Tablica klatek animacji, zawiera tekstury animacji
	 */
	private Texture[] frames;

	/**
	 * Wskaznik na kolejna klatke animacji
	 */
	private int texturePointer;

	/**
	 * Czas ktory uplynal od poprzedniej klatki
	 */
	private double elapsedTime;
	/**
	 * Obecny czas
	 */
	private double currentTime;
	/**
	 * Czas ostatniej klatki animacji
	 */
	private double lastTime;
	/**
	 * Ilosc klatek na sekunde
	 */
	private double fps;

	/**
	 * Konstruktor. Wczytuje tekstury animacji i ustawia ich paramentry wyjsciowe
	 * @param amount ilosc klatek animacji
	 * @param fps ilosc klatek animacji na sekunde
	 * @param filename nazwa tekstury do wczytania
	 */
	public Animation(int amount, int fps, String filename) {
		this.texturePointer = 0;
		this.elapsedTime = 0;
		this.currentTime = 0;
		this.lastTime = Timer.getTime();
		this.fps = 1.0 / fps;
		
		this.frames = new Texture[amount];
		for (int i = 0; i < amount; i++) {
			this.frames[i] = new Texture(filename + "/" + i + ".png");
		}
	}

	/**
	 * Podlaczanie tekstury do renderowania pod domyslny sampler tekstur
	 */
	public void bind() {
		bind(0);
	}

	/**
	 * Podpiecie tekstury pod sampler przeslany w argumencie.
	 * @param sampler numer samplera
	 */
	public void bind(int sampler) {
		this.currentTime = Timer.getTime();
		this.elapsedTime += currentTime - lastTime;
		
		if (elapsedTime >= fps) {
			elapsedTime = 0;
			texturePointer++;
		}
		
		if (texturePointer >= frames.length) texturePointer = 0;
		
		this.lastTime = currentTime;
		
		frames[texturePointer].bind(sampler);
	}
}
