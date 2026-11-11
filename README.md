# Bomberman

A 2D Bomberman-style game written in Java with the LWJGL 3 OpenGL bindings. The
player drops bombs to destroy bricks and enemies across several levels, with a
menu, save slots, a high-score board, cut-scenes and sound.

This is a student / personal project; the in-code documentation (Javadoc
comments) is written in Polish.

## Features

- Tile-based world with multiple levels (`levels/`)
- Several enemy types (`Enemy`, `Boss`, `Gumba`, `Pepe`, `Zombie`)
- Bombs, flames and AABB collision detection
- Menu, save/load slots, high-score board, intro and cut-scenes
- Sprite animation, a custom bitmap-font text renderer and a camera
- Sound effects and music (`music/`)
- In-game screenshots (saved to `screenshots/`)

## Tech stack

- Java
- [LWJGL](https://www.lwjgl.org/) 3.1.2 (GLFW, OpenGL, STB)
- [JOML](https://github.com/JOML-CI/JOML) (math / camera)
- OpenGL shaders (GLSL, in `src/main/resources/shaders/`)

## Building and running

The repository is an IntelliJ IDEA project (`Bomberman.ipr` / `Bombereman.iml`).
There is no committed build script; the LWJGL and JOML dependencies are
referenced as IDE module libraries (they were resolved through the Gradle
dependency cache on the original machine). To build:

1. Open the project in IntelliJ IDEA.
2. Make sure the LWJGL 3.1.2 and JOML libraries (including the LWJGL native
   jar for your platform) are on the module classpath.
3. Run the `com.bomberman.game.Main` class.

The game loads assets with paths relative to the working directory, so run it
from the repository root (it expects the `music/`, `levels/`, `saves/`,
`shaders/` and `textures/` folders to be reachable).

Controls: move with `W` / `A` / `S` / `D`, drop a bomb with `Space`.

## Project structure

- `src/main/java/com/bomberman/` - game source, grouped by package
  (`game`, `entity`, `gui`, `render`, `world`, `io`, `collision`, `assets`,
  `scores`)
- `src/main/resources/` - shaders, textures and save-slot thumbnails
- `levels/` - level definitions (image-based tile maps)
- `music/` - sound effects and music (WAV)
- `screenshots/` - example screenshots
- `doc/` - generated Javadoc
