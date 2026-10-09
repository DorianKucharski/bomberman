package com.bomberman.render;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

/**
 * Tekstura
 */
public class Texture {
	/**
	 * Wskaznik na program renderowania tekstur
	 */
	private int textureObject;
	/**
	 * Szerokosc tekstury
	 */
	private int width;
	/**
	 * Wysokosc tekstury
	 */
	private int height;

	/**
	 * Kostruktor tekstury. Wczytuje teksture z pliku do obiektu klasy BufferedImage, z tego obiektu wczytuje
	 * wymiary tekstury, na podstawie tych wyumiarow tworzy tablice przechowujaca piksele tekstury w formacie
	 * RGBA, nastepnie czyta plik tekstury pixel po pixelu dodajac do tablicy pixeli informacje o kolejnych
	 * skladowych kolorow. Tablica ta nastepnie jest obracana tak by mogla byc poprawnie wyrenderowana przez OpenGL.
	 * Na koniec tekstura jest bindowana
	 * @param filename nazwa pliku przechuowujacego teksture
	 */
	public Texture(String filename) {
		BufferedImage bufferedImage;
		try {
			URI file = getClass().getResource("/textures/" + filename).toURI();
			bufferedImage = ImageIO.read(new File(file));
			width = bufferedImage.getWidth();
			height = bufferedImage.getHeight();
			
			int[] pixels_raw = new int[width * height * 4];
			pixels_raw = bufferedImage.getRGB(0, 0, width, height, null, 0, width);
			
			ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
			
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					int pixel = pixels_raw[i * width + j];
					pixels.put((byte) ((pixel >> 16) & 0xFF));
					pixels.put((byte) ((pixel >> 8) & 0xFF));
					pixels.put((byte) (pixel & 0xFF));
					pixels.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			
			pixels.flip();
			
			textureObject = glGenTextures();
			
			glBindTexture(GL_TEXTURE_2D, textureObject);
			
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
			
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}


	/**
	 * Podpiecie tekstury pod sampler GL_TEXTURE
	 * @param sampler numer samplera
	 */
	public void bind(int sampler) {
		if (sampler >= 0 && sampler <= 31) {
			glActiveTexture(GL_TEXTURE0 + sampler);
			glBindTexture(GL_TEXTURE_2D, textureObject);
		}
	}
}
