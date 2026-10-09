package com.bomberman.render;

import com.bomberman.assets.Assets;
import com.bomberman.collision.AABB;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_BLEND;

/**
 * Tworzenie i renderowanie napisow
 */
public class DrawString {

    /**
     * Model napisu
     */
    private AABB boundingBox;

    /**
     * Polozenie projekcji napisow
     */
    private static Matrix4f transform = new Matrix4f();

    /**
     * Konstruktor napisu
     * @param position pozycja na ekranie
     * @param scale rozmiar napisu
     */
    public DrawString(Vector2f position, Vector2f scale) {
        this.boundingBox = new AABB(position, scale);
    }

    /**
     * Zmiana pozycji napisu
     * @param x polozenie w osi x
     * @param y polozenie w osi y
     */
    public void changePosition(int x, int y) {
        this.boundingBox.center.y = y;
        this.boundingBox.center.x = x;
    }

    /**
     * Przesuwanie napisu o kreslona ilosc pikseli w plaszczyznach x i y
     * @param x o ile pikseli przesunac w osi x
     * @param y o ile pikseli przesunac w osi y
     */
    public void move(int x, int y){
        this.boundingBox.center.y += y;
        this.boundingBox.center.x += x;

    }

    /**
     * Rednerowanie napisow
     * @param camera kamera okna
     * @param sheet sheet zawierajacy czcionke
     * @param shader shader do renderowania
     * @param string napis do wyrenderowania
     */
    public void render(Camera camera, TileSheet sheet, Shader shader, String string) {

        for (int i = 0; i < string.length(); i++){
            Vector2f position = boundingBox.getCenter(), scale = boundingBox.getHalfExtent();
            transform.identity().translate(position.x+(scale.x*i*2), position.y, 0).scale(scale.x, scale.y, 1);
            shader.setUniform("projection", camera.getProjection().mul(transform));
            if (string.equals("HEAD"))
            {
                sheet.bindTile(shader, 7, 9);
                Assets.getModel().render();
                break;
            }
            if (string.equals("BLACK"))
            {
                sheet.bindTile(shader, 6, 9);
                Assets.getModel().render();
                break;
            }

            if (string.equals("WHITE"))
            {
                sheet.bindTile(shader, 5, 9);
                Assets.getModel().render();
                break;
            }

            if (string.equals("PICTURE"))
            {
                sheet.bindTile(shader, 0, 1);
                Assets.getModel().render();

                transform.identity().translate(position.x+(scale.x*2), position.y, 0).scale(scale.x, scale.y, 1);
                shader.setUniform("projection", camera.getProjection().mul(transform));

                sheet.bindTile(shader, 1, 1);
                Assets.getModel().render();
                break;
            }

            else {
                char znak = string.charAt(i);
                switch (znak) {
                    case '0':
                        sheet.bindTile(shader, 1, 0);
                        break;
                    case '1':
                        sheet.bindTile(shader, 2, 0);
                        break;
                    case '2':
                        sheet.bindTile(shader, 3, 0);
                        break;
                    case '3':
                        sheet.bindTile(shader, 4, 0);
                        break;
                    case '4':
                        sheet.bindTile(shader, 5, 0);
                        break;
                    case '5':
                        sheet.bindTile(shader, 6, 0);
                        break;
                    case '6':
                        sheet.bindTile(shader, 7, 0);
                        break;
                    case '7':
                        sheet.bindTile(shader, 8, 0);
                        break;
                    case '8':
                        sheet.bindTile(shader, 9, 0);
                        break;
                    case '9':
                        sheet.bindTile(shader, 10, 0);
                        break;

                    case 'A':
                        sheet.bindTile(shader, 0, 4);
                        break;
                    case 'B':
                        sheet.bindTile(shader, 1, 4);
                        break;
                    case 'C':
                        sheet.bindTile(shader, 2, 4);
                        break;
                    case 'D':
                        sheet.bindTile(shader, 3, 4);
                        break;
                    case 'E':
                        sheet.bindTile(shader, 4, 4);
                        break;
                    case 'F':
                        sheet.bindTile(shader, 5, 4);
                        break;
                    case 'G':
                        sheet.bindTile(shader, 6, 4);
                        break;
                    case 'H':
                        sheet.bindTile(shader, 7, 4);
                        break;
                    case 'I':
                        sheet.bindTile(shader, 8, 4);
                        break;
                    case 'J':
                        sheet.bindTile(shader, 9, 4);
                        break;
                    case 'K':
                        sheet.bindTile(shader, 10, 4);
                        break;
                    case 'L':
                        sheet.bindTile(shader, 11, 4);
                        break;
                    case 'M':
                        sheet.bindTile(shader, 12, 4);
                        break;
                    case 'N':
                        sheet.bindTile(shader, 13, 4);
                        break;
                    case 'O':
                        sheet.bindTile(shader, 14, 4);
                        break;
                    case 'P':
                        sheet.bindTile(shader, 15, 4);
                        break;
                    case 'Q':
                        sheet.bindTile(shader, 0, 5);
                        break;
                    case 'R':
                        sheet.bindTile(shader, 1, 5);
                        break;
                    case 'S':
                        sheet.bindTile(shader, 2, 5);
                        break;
                    case 'T':
                        sheet.bindTile(shader, 3, 5);
                        break;
                    case 'U':
                        sheet.bindTile(shader, 4, 5);
                        break;
                    case 'V':
                        sheet.bindTile(shader, 5, 5);
                        break;
                    case 'W':
                        sheet.bindTile(shader, 6, 5);
                        break;
                    case 'X':
                        sheet.bindTile(shader, 7, 5);
                        break;
                    case 'Y':
                        sheet.bindTile(shader, 8, 5);
                        break;
                    case 'Z':
                        sheet.bindTile(shader, 9, 5);
                        break;
                    case '#':
                        sheet.bindTile(shader, 0, 1);
                        break;
                    case ':':
                        sheet.bindTile(shader, 4, 1);
                        break;
                    case '-':
                        sheet.bindTile(shader, 12, 0);
                        break;

                    default:
                        sheet.bindTile(shader, 0, 0);
                        break;

                }
            }
            Assets.getModel().render();

        }




    }



}
