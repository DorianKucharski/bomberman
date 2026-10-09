package com.bomberman.game;

/**
 * @author Dorian Kucharski
 * @version 1.6
 *
 */

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import com.bomberman.assets.Assets;
import com.bomberman.gui.*;
import com.bomberman.io.Audio;
import com.bomberman.io.ScreenShots;
import com.bomberman.render.Camera;
import com.bomberman.render.Shader;
import com.bomberman.world.TileRenderer;
import com.bomberman.world.World;
import com.bomberman.io.Timer;
import com.bomberman.io.Window;
import org.lwjgl.opengl.GL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Glowna klasa gry
 */
public class Main {
	/**
	 * Poziom gry
	 */
	private static int level = 0;
	/**
	 * Okno gry
	 */
	public static Window window;
	/**
	 * Creditsy
	 */
	private static Credits credits;
	/**
	 * Cutscenki
	 */
	private static Cutscenes cutscenes;
	/**
	 * Tablica wynikow
	 */
	private static ScoreBoard scoreboard;
	/**
	 * Quick Save
	 */
	private static Saves quickSave;
	/**
	 * Intro
	 */
	private static Intro intro;
	/**
	 * Menu
	 */
	private static Menu menu;
	/**
	 * Zmiena odpowiedzialna za pauze.
	 * Jesli true pauza jest wlaczona, jesli false wylaczona
	 */
	public static boolean pauza = false;
	/**
	 * Flaga potrzbena do robienia screen shotow w nasteplnej klatce gry.
	 * Potrzebna jest do tego aby na zrzucie ekranu nie bylo widzocznego hudu
	 */
	protected static boolean screenShotforSaves = false;
	/**
	 * Zmienna przekazujaca nazwe zrzutu ekranu
	 */
	protected static String nameOfscreenShotforSaves = "";
	/**
	 * Zmienna okreslajaca ilosc zyc gracza
	 */
	private static int lives = 3;
	/**
	 * Zmienna przechowujaca wynik gracza
	 */
	private static int score = 0;
	/**
	 * Swiat gry
	 */
	public static World world;

	/**
	 * Enum okreslajacy stany gry
	 */
	public enum State {
		/**
		 * Intro
		 */
		INTRO,
		/**
		 * Menu
		 */
		MENU,
		/**
		 * Credits
		 */
		CREDITS,
		/**
		 * Gra
		 */
		GAME,
		/**
		 * Przerywniki filmowe
		 */
		CUTSCENES,
		/**
		 * Tablica wynikow
		 */
		SCOREBOARD,
		/**
		 * Okno przegranej
		 */
		LOSE,
		/**
		 * Okno wygranej
		 */
		WIN,
		/**
		 * Wyjscie z gry
		 */
		EXIT
	}

	/**
	 * Pole okreslajace aktualny stan gry
	 */
	public static State state;
    /**
     * Zmienna wylacza i wlacza krew. Jesli true to krew jest wlaczona, jeli false wylaczona
     */
	public static boolean blood = true;

	/**
	 * Konstruktor gry
	 */
	public Main() {

		Window.setCallbacks();

		if (!glfwInit()) {
			System.err.println("Cos sie, cos sie popsulo");
			System.exit(1);
		}

		window = new Window();
		window.setSize(1280, 720);
		window.setFullscreen(false);
		window.createWindow("Bomberman 2077");


		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		GL.createCapabilities();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glEnable(GL_TEXTURE_2D);


		Camera camera = new Camera(window.getWidth(), window.getHeight());
		TileRenderer tiles = new TileRenderer();
		Assets.initAsset();
		Shader shader = new Shader("shader");
		menu = new Menu(window);
		Bar bar = new Bar(window);
		credits = new Credits(window);
		cutscenes = new Cutscenes(window);
		scoreboard = new ScoreBoard(window);
		intro = new Intro(window);
		quickSave = new Saves("quick_save");
		world = new World("test_level");
		world.calculateView(window);
		state = State.INTRO;

		/**
		 * Zmienne odpowiedzialne za odpowiednie renderowanie gry i utrzymanie stabilnej liczby klatek
		 */
		double frame_cap = 1.0 / 60.0;
		double frame_time = 0;
		int frames = 0;
		double time = Timer.getTime();
		double unprocessed = 0;

		/**
		 * Glowna petla okna gry. Jest ona podzielona na kilka innych petli odpowiedzialnych za poszczegolne stany
		 * gry, np. tablica wynikow, gra, menu itp.
		 */

		while (!window.shouldClose()) {
			while (state == State.INTRO && !window.shouldClose()) {
				skipCheck();
				if (intro.render()) {
					startMusic();
					state = State.MENU;
				}
				window.update();
				window.swapBuffers();
			}

			while (state == State.SCOREBOARD && !window.shouldClose()) {
				if (level != 0 && !pauza) {
					if (scoreboard.render()) skipCheck();

				}
				else {
					scoreboard.rednerTable();
					skipCheck();
				}

				window.update();
				window.swapBuffers();
			}

			while (state == State.MENU && !window.shouldClose()) {

				menu.update(window.getInput());
				menu.render();
				window.update();
				window.swapBuffers();

			}

			while (state == State.CREDITS && !window.shouldClose()){
				//pauzaCheck();
				skipCheck();
				if (!pauza) credits.update();
				credits.render();
				if (pauza) menu.update(window.getInput());
				if (pauza) menu.render();
				window.update();
				window.swapBuffers();
			}

			while (state == State.CUTSCENES && !window.shouldClose()){
				pauzaCheck();
				skipCheck();
				if (cutscenes.render(Cutscenes.NEXTLEVEL)) {
						state = State.GAME;
						cutscenes.startCutscenes();
				}
				if (!pauza){
					cutscenes.update();
				}

				if (pauza) menu.update(window.getInput());
				if (pauza) menu.render();
				window.update();
				window.swapBuffers();
			}

			while (state == State.LOSE && !window.shouldClose()){
				pauzaCheck();
				skipCheck();

				if (cutscenes.render(Cutscenes.LOSE)) {
					state = State.SCOREBOARD;
					cutscenes.startCutscenes();

				}

				if (!pauza){
					cutscenes.update();
				}

				if (pauza) menu.update(window.getInput());
				if (pauza) menu.render();
				window.update();
				window.swapBuffers();
			}

			while (state == State.WIN && !window.shouldClose()){
				pauzaCheck();
				skipCheck();

				if (cutscenes.render(Cutscenes.WIN)) {
					state = State.SCOREBOARD;
					cutscenes.startCutscenes();

				}

				if (!pauza){
					cutscenes.update();
				}

				if (pauza) menu.update(window.getInput());
				if (pauza) menu.render();
				window.update();
				window.swapBuffers();
			}



			while (state == State.GAME && !window.shouldClose()) {
				boolean can_render = false;
				double time_2 = Timer.getTime();
				double passed = time_2 - time;
				unprocessed += passed;
				frame_time += passed;
				time = time_2;

				while (unprocessed >= frame_cap) {
					if (window.hasResized()) {
						camera.setProjection(window.getWidth(), window.getHeight());
						if (pauza) menu.resizeCamera(window);
						bar.resizeCamera(window);
						world.calculateView(window);
						glViewport(0, 0, window.getWidth(), window.getHeight());
					}

					unprocessed -= frame_cap;
					can_render = true;


					if (pauza) menu.update(window.getInput());

					try {
						if (!pauza) world.update((float) frame_cap, window, camera, world);
					} catch (Exception e) {
						e.printStackTrace();
					}

					world.correctCamera(camera, window);
					window.update();

					if (frame_time >= 1.0) {
						frame_time = 0;
						frames = 0;
					}
				}

				if (can_render) {
					glClear(GL_COLOR_BUFFER_BIT);
					world.render(tiles, shader, camera);
					if (level > 0 && level < 10) bar.render();


					if (lives <= 0) {
						//cutscenes.render(Cutscenes.LOSE);
						state = State.LOSE;
						lose();
					}

					if (world.countEnemies() == 0 && level > 0 && level < 5) {
							nextLevel();
					}

					if (world.countEnemies() == 0 && level == 5) {
						state = State.WIN;
						lose();
					}

					if (pauza) menu.render();
					window.swapBuffers();
					frames++;

					pauzaCheck();

				}

				if (screenShotforSaves){
					ScreenShots.take("src/resources/saves/" + nameOfscreenShotforSaves);
					//menu.loadPictures();
					screenShotforSaves = false;
				}

				if (window.getInput().isKeyPressed(GLFW_KEY_F5)){
					quickSave.save(world);
					quickSave.saveToFile();
				}


				if (window.getInput().isKeyPressed(GLFW_KEY_F8)){
					quickSave = quickSave.loadFromFile();
					quickSave.load(world);
				}

				if (window.getInput().isKeyPressed(GLFW_KEY_F9)){
					LocalDateTime date = LocalDateTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss");
					String formattedDate = date.format(formatter);
					System.out.println(date);
					ScreenShots.take("screenshots/" + formattedDate);
				}

				if (window.getInput().isKeyPressed(GLFW_KEY_F12)){
					world.killEnemies();
				}

				Audio.replay();
			}
		}
		Assets.deleteAsset();
		glfwTerminate();
	}


	/**
	 * Metoda wylaczajaca gre. Przestawia stan gry na exit i zamyka okno.
	 */
	public static void exit(){
		state = State.EXIT;
		glfwSetWindowShouldClose(window.getWindow(), true);

	}


	/**
	 * Metoda czyszczaca swiat
	 */
	public static void lose(){
		world.clearEntities();
	}

	/**
	 * Metoda inicjalizujaca muzyke i wlaczajaca glowny motyw muzyczny
	 */
	public static void startMusic(){
		Audio.playMusic();
		Audio.jumpTo(Audio.THEME);
	}

	/**
	 * Metoda uruchamiajaca wlasciwa gre. Ustawia wartosci poczatkowe, czysci swiat, tworzy plansze, uruchamia muzyke.
	 */
	public static void startGame(){
		level = 1;
		lives = 3;
		score = 0;
		world.clearEntities();
		world.createWorld("test_level");
		Audio.jumpTo(Audio.BEGIN);
		state = State.GAME;
		pauza = false;
		scoreboard.clearName();
	}

	/**
	 * Metoda uruchamiajaca Creditsy. Resetuje polozenie napisow, wlacza muzyke, zmienia stan gry.
	 */
	public static void startCredits(){
		//world.clearEntities();
		credits.start();
		Audio.jumpTo(Audio.CREDITS);
		state = State.CREDITS;
		pauza = false;
	}

	/**
	 * Metoda zmieniajaca plansze gry. Inkrementuje zmienna level, uruchamia cutscenke,
	 * czysci poprzednia plansze i wywoluje metoda tworzaca kolejny poziom.
	 */
	public static void nextLevel(){
		level ++;
		state = State.CUTSCENES;
		increaseLives();
		world.clearEntities();
		if (level == 2) world.createWorld("level2");
		if (level == 3) world.createWorld("level3");
		if (level == 4) world.createWorld("level4");
		if (level == 5) world.createWorld("level5");
	}

	/**
	 * Metoda przestawia stan gry na plansze z wynikami.
	 */
	public static void showScores(){
		state = State.SCOREBOARD;
	}

	/**
	 * Getter ilosci zyc gracza
	 * @return zwraca ilosc zyc
	 */
	public static int getLives(){
		return lives;
	}

	/**
	 * Metoda zwiekszajaca ilosc zyc gracza
	 */
	public static void increaseLives(){
		lives++;
	}

	/**
	 * Metoda zmiejaszajaca ilosc zyc gracza
	 */
	public static void decreaseLives(){
		lives--;
	}

	/**
	 * Getter swiata gry
	 * @return zwraca world
	 */
	public static World getWorld(){return world;}

	/**
	 * Getter punktow
	 * @return zwraca punkty
	 */
	public static int getScore(){
		return score;
	}

	/**
	 * Metoda zwiekszajaca punkty gracza
	 * @param amount ilosc punktow do dodania lub odjecia
	 */
	public static void increaseScore(int amount){
		score += amount;
	}

	/**
	 * Getter  pola level
	 * @return zwraca level
	 */
	public static int getLevel() { return level;}

	/**
	 * Getter obiektu window
	 * @return zwraca window
	 */
	public static Window getWindow(){return window;}

	/**
	 * Metoda uruchamiajaca jakis easteregg. Nie pamietam o co chodzi.
	 */
	public static void easteregg(){
		Audio.bonus();
	}

	/**
	 * Metoda sprawdza czy wcisnieto klawisz ESC, jesli tak przestawia zmienna pauza na true lub false
	 */
	public static void pauzaCheck(){
		if ((window.getInput().isKeyPressed(GLFW_KEY_ESCAPE))) {
			if (pauza) pauza = false;
			else pauza = true;
		}
	}

	/**
	 * Metoda sprawdza czy gracz chce pominac dana sekwencje i przejsc do nastepnej.
	 * Sprawdza czy wcisnieto jakikolwiek przycisk poza ESC, jezeli tak przechodzi
	 * do odpowiedniej sekwencji, np. jesli odtwarzana jest aktualnie cutscenka metoda przeskakuje do gry.
	 */
	public static void skipCheck(){
		if (window.getInput().returnkey() != 0 && window.getInput().returnkey() != 256) {
			if (state == State.SCOREBOARD) state = State.MENU;
			if (state == State.CUTSCENES) {
				state = State.GAME;
				cutscenes.startCutscenes();
			}
			if (state == State.CREDITS) state = State.MENU;
			if (state == State.LOSE) state = State.SCOREBOARD;
			if (state == State.INTRO) {
				startMusic();
				state = State.MENU;
			}


		}
	}

	/**
	 * Metoda ukrywajaca menu gry
	 */
	public static void hideMenu(){
		menu.hide();
		window.update();
		window.swapBuffers();

	}

	/**
	 * Metoda wyswietlajaca menu gry
	 */
	public static void showMenu(){
		menu.show();
		window.update();
		window.swapBuffers();
	}



	/**
	 * Metoda glowna programu.
	 * Wywoluje jedynie metode Unity()
	 * @param args niepotrzebna tablica argumentow.
	 */
	public static void main(String[] args) {
		new Unity();
	}

}
