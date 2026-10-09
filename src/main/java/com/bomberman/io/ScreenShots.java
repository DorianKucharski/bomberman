package com.bomberman.io;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Robienie i zapisywanie zrzutow ekranu
 */
public class ScreenShots {

    /**
     * Robienie i zapisywanie zrzutow ekranu
     * @param path sciezka pliku do ktorego zapisac zrzut
     */
    public static void take(String path){
        GL11.glReadBuffer(GL11.GL_FRONT);

        int width = 1280;
        int height= 1280;
        int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
        GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer );


        File file = new File(path + ".png");
        String format = "PNG";
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                int i = (x + (width * y)) * bpp;
                int r = buffer.get(i) & 0xFF;
                int g = buffer.get(i + 1) & 0xFF;
                int b = buffer.get(i + 2) & 0xFF;

                image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);


            }
        }

        try {
            ImageIO.write(image, format, file);
        } catch (IOException e) { e.printStackTrace(); }
    }
}
