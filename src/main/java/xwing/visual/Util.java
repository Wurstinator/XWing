package xwing.visual;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL30.*;

public class Util {
    static int loadAndCompileShader(String filename, int shaderType) {
        String src;
        try {
            src = new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            return 0;
        }
        int shader = glCreateShader(shaderType);
        glShaderSource(shader, src);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(shader));
        }
        return shader;
    }
}
