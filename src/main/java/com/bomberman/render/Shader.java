package com.bomberman.render;

import static org.lwjgl.opengl.GL20.*;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

/**
 * Shader
 */
public class Shader {
	/**
	 * Wskaznik programu OpenGL przetwarzajacego Shader
	 */
	private int programObject;
	/**
	 * Wskaznik na vertex shader, shader odpowiedzialny za ogolne rysowanie ksztaltow
	 */
	private int vertexShaderObject;
	/**
	 * Wskaznik na fragment shader, shader odpowiedzialny za rysowanie tekstur
	 */
	private int fragmentShaderObject;

	/**
	 * Konstruktor shadera. Tworzy shadery, laduje shadery z plikow, dolacza fragment shader i vertex shader
	 * pod program GL odpowiedzialny za renderowanie shaderow, laczy i podpina progam.
	 * Vertex Shader znajduje sie w pliku z rozszerzeniem .vs, Fragmen Shader w pliku z rozszerzeniem .fs
	 * @param filename nazwa shaderow
	 */
	public Shader(String filename) {
		programObject = glCreateProgram();
		
		vertexShaderObject = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShaderObject, readFile(filename + ".vs"));
		glCompileShader(vertexShaderObject);
		if (glGetShaderi(vertexShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(vertexShaderObject));
			System.exit(1);
		}
		
		fragmentShaderObject = glCreateShader(GL_FRAGMENT_SHADER);
		glShaderSource(fragmentShaderObject, readFile(filename + ".fs"));
		glCompileShader(fragmentShaderObject);
		if (glGetShaderi(fragmentShaderObject, GL_COMPILE_STATUS) != 1) {
			System.err.println(glGetShaderInfoLog(fragmentShaderObject));
			System.exit(1);
		}
		
		glAttachShader(programObject, vertexShaderObject);
		glAttachShader(programObject, fragmentShaderObject);
		
		glBindAttribLocation(programObject, 0, "vertices");
		glBindAttribLocation(programObject, 1, "textures");
		
		glLinkProgram(programObject);
		if (glGetProgrami(programObject, GL_LINK_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
		glValidateProgram(programObject);
		if (glGetProgrami(programObject, GL_VALIDATE_STATUS) != 1) {
			System.err.println(glGetProgramInfoLog(programObject));
			System.exit(1);
		}
	}

	/**
	 * Przesylanie dodatkowych danych do shaderow
	 * @param uniformName nazwa zmiennej przekazywanej do shadera
	 * @param value wartosc zmiennej
	 */
	public void setUniform(String uniformName, int value) {
		int location = glGetUniformLocation(programObject, uniformName);
		if (location != -1) glUniform1i(location, value);
	}

	/**
	 * Przesylanie dodatkowych danych do shaderow
	 * @param uniformName nazwa zmiennej przekazywanej do shadera
	 * @param value wartosc zmiennej
	 */
	public void setUniform(String uniformName, Vector4f value) {
		int location = glGetUniformLocation(programObject, uniformName);
		if (location != -1) glUniform4f(location, value.x, value.y, value.z, value.w);
	}

	/**
	 * Przesylanie dodatkowych danych do shaderow
	 * @param uniformName nazwa zmiennej przekazywanej do shadera
	 * @param value wartosc zmiennej
	 */
	public void setUniform(String uniformName, Matrix4f value) {
		int location = glGetUniformLocation(programObject, uniformName);
		FloatBuffer matrixData = BufferUtils.createFloatBuffer(16);
		value.get(matrixData);
		if (location != -1) glUniformMatrix4fv(location, false, matrixData);
	}

	/**
	 * Podpinanie programu odpowiedzialnego za rednerowanie shaderow
	 */
	public void bind() {
		glUseProgram(programObject);
	}

	/**
	 * Wczytywanie shaderow z pliku
	 * @param filename nazwa shadera
	 * @return zawartosc pliku
	 */
	private String readFile(String filename) {
		StringBuilder outputString = new StringBuilder();
		BufferedReader bufferedReader;
		try {
			URI filePath = getClass().getResource("/shaders/" + filename).toURI();
			bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				outputString.append(line);
				outputString.append("\n");
			}
			bufferedReader.close();
		}
		catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return outputString.toString();
	}
}
