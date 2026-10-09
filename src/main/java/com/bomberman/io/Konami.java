package com.bomberman.io;

import com.bomberman.game.Main;

import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Kody
 */
public class Konami {

    /**
     * Lista ostatnio wcisnietych zdefiniowanych klawiszy
     */
    private static ArrayList <Integer> lista = new ArrayList<>();

    /**
     * Sprawdza czy dany kod zostal wpisany i odpowiada za jego uruchomienie.
     * @param window okno gry
     */
    public static void check(Window window){
        if (window.getInput().isKeyPressed(GLFW_KEY_A)) lista.add(1);
        if (window.getInput().isKeyPressed(GLFW_KEY_E)) lista.add(2);
        if (window.getInput().isKeyPressed(GLFW_KEY_Z)) lista.add(3);
        if (window.getInput().isKeyPressed(GLFW_KEY_K)) lista.add(4);
        if (window.getInput().isKeyPressed(GLFW_KEY_M)) lista.add(5);
        if (window.getInput().isKeyPressed(GLFW_KEY_I)) lista.add(6);
        if (window.getInput().isKeyPressed(GLFW_KEY_T)) lista.add(7);
        if (window.getInput().isKeyPressed(GLFW_KEY_L)) lista.add(8);
        if (window.getInput().isKeyPressed(GLFW_KEY_7)) lista.add(9);
        if (window.getInput().isKeyPressed(GLFW_KEY_B)) lista.add(10);
        if (window.getInput().isKeyPressed(GLFW_KEY_G)) lista.add(12);
        if (window.getInput().isKeyPressed(GLFW_KEY_O)) lista.add(13);
        if (window.getInput().isKeyPressed(GLFW_KEY_Y)) lista.add(14);
        if (window.getInput().isKeyPressed(GLFW_KEY_U)) lista.add(15);
        if (window.getInput().isKeyPressed(GLFW_KEY_S)) lista.add(16);
        if (window.getInput().isKeyPressed(GLFW_KEY_R)) lista.add(17);
        if (window.getInput().isKeyPressed(GLFW_KEY_H)) lista.add(18);

        if (lista.size() == 11) lista.remove(0);


        String kod = lista.toString();
        if (kod.contains("1, 2, 3, 1, 4, 5, 6")) {
            Main.increaseScore(9000);
//            Main.increaseLives();
//            Main.increaseLives();
//            Main.increaseLives();
//            Main.increaseLives();
//            Main.increaseLives();
            Main.decreaseLives();
            lista.clear();
        }

        if (kod.contains("5, 2, 7, 1, 8, 9, 9")) {
            Main.easteregg();
            lista.clear();
        }

        if (kod.contains("5, 2, 5, 2, 10, 6, 12, 10, 13, 14")) {
            Main.getWorld().player.grow();
            lista.clear();
        }

        if (kod.contains("3, 15, 4, 1, 8, 7")) {
            Main.getWorld().player.shrink();
            lista.clear();
        }

        if (kod.contains("16, 7, 17, 1, 6, 12, 18, 7")) {
            Main.getWorld().player.normalize();
            lista.clear();
        }

        if (kod.contains("4, 6, 8, 8, 2, 5, 1, 8, 8")) {
            Main.getWorld().killEnemies();
            lista.clear();
        }

    }
}