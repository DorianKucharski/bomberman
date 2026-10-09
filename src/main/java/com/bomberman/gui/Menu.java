package com.bomberman.gui;

import com.bomberman.game.Main;
import com.bomberman.render.Camera;
import com.bomberman.render.DrawString;
import com.bomberman.render.Shader;
import com.bomberman.render.TileSheet;
import com.bomberman.io.Input;
import com.bomberman.io.Window;
import org.joml.Vector2f;
import org.lwjgl.system.CallbackI;

/**
 * Menu gry
 */
public class Menu {
	private Shader shader;
	private Camera camera;
	private TileSheet sheet;

	private Shader shader_font;
	private Camera camera_font;
	private TileSheet sheet_font;

	
	private Button newGameButton;
	private Button loadButton;
	private Button saveButton;
	private Button creditsButton;
	private Button scoresButton;
	private Button exitButton;

	private DrawString newGameString;
	private DrawString loadString;
	private DrawString saveString;
	private DrawString creditsString;
	private DrawString scoresString;
	private DrawString exitString;
	private DrawString title;
	private DrawString black;

	SavesMenu savesMenu;


	
	public Menu(Window window)   {
		savesMenu = new SavesMenu(window);

		shader = new Shader("gui");
		camera = new Camera(window.getWidth(), window.getHeight());
		sheet = new TileSheet("gui.png", 9);
		black = new DrawString(new Vector2f(0, 0), new Vector2f(700, 400));
		newGameButton = new Button(new Vector2f(0, 96), new Vector2f(240, 48)){
			@Override
			public void output() {
				Main.startGame();
			}
		};
		loadButton = new Button(new Vector2f(-120, 0), new Vector2f(120, 48)){
			@Override
			public void output() {
				savesMenu.loadPictures();
				savesMenu.mode = savesMenu.LOAD;
				savesMenu.show();

			}
		};

		saveButton = new Button(new Vector2f(120, 0), new Vector2f(120, 48)){
			@Override
			public void output() {
				savesMenu.loadPictures();
				savesMenu.mode = savesMenu.SAVE;
				savesMenu.show();

			}
		};
		creditsButton = new Button(new Vector2f(0, -96), new Vector2f(240, 48)){
			@Override
			public void output() {
				Main.startCredits();
			}
		};
		scoresButton = new Button(new Vector2f(0, -192), new Vector2f(240, 48)){
			@Override
			public void output() {
				Main.showScores();
			}
		};
		exitButton = new Button(new Vector2f(0, -288), new Vector2f(240, 48)){
			@Override
			public void output() {
				Main.exit();
			}
		};


		shader_font = new Shader("gui");
		camera_font = new Camera(window.getWidth(), window.getHeight());
		sheet_font = new TileSheet("font.png", 18);
		newGameString = new DrawString(new Vector2f(-7*24, 96), new Vector2f(24, 24));
		loadString = new DrawString(new Vector2f(-120 - 6*14, 0), new Vector2f(14, 14));
		saveString = new DrawString(new Vector2f(120 - 5 * 14, 0), new Vector2f(14, 14));
		creditsString = new DrawString(new Vector2f(-6*24, -96), new Vector2f(24, 24));
		scoresString = new DrawString(new Vector2f(-5*24, -192), new Vector2f(24, 24));
		exitString = new DrawString(new Vector2f(-6*24, -288), new Vector2f(24, 24));
		title = new DrawString(new Vector2f(0 - 14* 39, 250), new Vector2f(42, 72));
	}
	
	public void resizeCamera(Window window) {
		camera.setProjection(window.getWidth(), window.getHeight());
	}

	public void loadPictures(){
		savesMenu.loadPictures();
	}
	public void update(Input input) {

		if (!savesMenu.state()) {
			newGameButton.update(input);
			if (Main.state == Main.State.GAME) saveButton.update(input);
			loadButton.update(input);
			creditsButton.update(input);
			scoresButton.update(input);
			exitButton.update(input);
		}
		else {
			savesMenu.update(input);
		}

	}
	
	public void render() {

		shader.bind();

		if (!savesMenu.state()) {
			if (!Main.pauza) black.render(camera_font, sheet_font, shader_font, "BLACK");
			newGameButton.render(camera, sheet, shader);
			loadButton.render(camera, sheet, shader);
			saveButton.render(camera, sheet, shader);
			creditsButton.render(camera, sheet, shader);
			scoresButton.render(camera, sheet, shader);
			exitButton.render(camera, sheet, shader);
			newGameString.render(camera_font, sheet_font, shader_font, "NOWA GRA");
			loadString.render(camera_font, sheet_font, shader_font, "WCZYTAJ");
			saveString.render(camera_font, sheet_font, shader_font, "ZAPISZ");
			creditsString.render(camera_font, sheet_font, shader_font, "AUTORZY");
			scoresString.render(camera_font, sheet_font, shader_font, "WYNIKI");
			exitString.render(camera_font, sheet_font, shader_font, "WYJSCIE");
			if (Main.getLevel() == 0) title.render(camera_font, sheet_font, shader_font, "BOMBERMAN 2077");
		}
		if (savesMenu.state()) {
			savesMenu.render();
		}

	}

	public void hide(){
		savesMenu.hide();
		Main.pauza = false;

	}

	public void show(){
		Main.pauza = true;
		savesMenu.show();
	}
}
