package com.bomberman.io;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;

import com.bomberman.game.Main;
import org.lwjgl.glfw.*;

/**
 * Okno gry
 */
public class Window {
	/**
	 * Referencja do okna
	 */
	private long window;

	/**
	 * Wymiary okna
	 */
	private int width, height;
	/**
	 * Czy ekran jest zmaksymalizowany
	 */
	private boolean fullscreen;

	/**
	 * Czy ekran zmienil rozmiar
	 */
	private boolean hasResized;

	/**
	 * Przywolanie zmiany rozmiaru okna
	 */
	private GLFWWindowSizeCallback windowSizeCallback;

	/**
	 * Sterowanie
	 */
	private Input input;

	/**
	 * Ustawianie wywolan okna GLFW
	 */
	public static void setCallbacks() {
		glfwSetErrorCallback(new GLFWErrorCallbackI() {
			@Override
			public void invoke(int error, long description) {
				throw new IllegalStateException(GLFWErrorCallback.getDescription(description));
			}
		});
	}

	/**
	 * Ustawienie wywolan lokalnych okna
	 */
	private void setLocalCallbacks() {
		windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long argWindow, int argWidth, int argHeight) {
				width = argWidth;
				height = argHeight;
				hasResized = true;
			}
		};
		
		glfwSetWindowSizeCallback(window, windowSizeCallback);
	}

	/**
	 * Konstruktor okna
	 */
	public Window() {
		setSize(1280, 720);
		setFullscreen(false);
		hasResized = false;

	}

	/**
	 * Tworzenie okna GLFW
	 * @param title tytul okna
	 */
	public void createWindow(String title) {
		window = glfwCreateWindow(width, height, title, fullscreen ? glfwGetPrimaryMonitor() : 0, 0);
		
		if (window == 0) throw new IllegalStateException("Okno sie nie otwarlo");
		
		if (!fullscreen) {
			GLFWVidMode vid = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(window, (vid.width() - width) / 2, (vid.height() - height) / 2);
		}
		
		glfwShowWindow(window);
		
		glfwMakeContextCurrent(window);
		
		input = new Input(window);
		setLocalCallbacks();
	}

	/**
	 * Sprzatanie okna
	 */
	public void cleanUp() {
		glfwFreeCallbacks(window);
	}

	/**
	 * Czy okno powinno sie zakmnac
	 * @return true jeli powinno, true jesli nie
	 */
	public boolean shouldClose() {

		if (glfwWindowShouldClose(window)) Main.state = Main.State.EXIT;
		return glfwWindowShouldClose(window);

	}

	/**
	 * Zamiana bufferu
	 */
	public void swapBuffers() {
		glfwSwapBuffers(window);
	}

	/**
	 * Ustawienie rozmiaru okna
	 * @param width szerokosc
	 * @param height wysokosc
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Maksymalizuje okno
	 * @param fullscreen true albo false
	 */
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	/**
	 * Aktualizacja stanu okna
	 */
	public void update() {
		hasResized = false;
		Konami.check(this);
		input.update();
		glfwPollEvents();
	}

	/**
	 * Getter szerokosci okna
	 * @return szerokosc okna
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Getter wysokosci okna
	 * @return wysokosc okna
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Czy okno zmienilo rozmiar
	 * @return true jesli tak, false jesli nie
	 */
	public boolean hasResized() {
		return hasResized;
	}

	/**
	 * Czy okno jest zmaksymalizowane
	 * @return true jesli tak, false jesli nie
	 */
	public boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Getter okna
	 * @return referencja okna
	 */
	public long getWindow() {
		return window;
	}

	/**
	 * Getter inputu
	 * @return obiekt klasy Input, przypisany do okna
	 */
	public Input getInput() {
		return input;
	}




}
