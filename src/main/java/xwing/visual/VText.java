package xwing.visual;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.stb.STBEasyFont;
import xwing.Constants;
import xwing.logic.Position;

import java.io.Closeable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL30.*;
import static xwing.visual.Util.loadAndCompileShader;

public class VText implements Closeable {
    private String text;
    private Position position;

    private int vertexArray;
    private int vertexBuffer;
    private int vertexCount;
    private int vertexShader;
    private int fragmentShader;
    private int program;

    public VText(String text) {
        this.text = text;
        this.position = new Position(0, 0, 0);
        initGL();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float boardCoordinateToGL(double pos, boolean center) {
        float n = (float) (2 * pos / Constants.BOARD_SIZE);
        if (center) n -= 1;
        return n;
    }

    /**
     * Little endian.
     *
     * @param a
     * @param b
     * @param c
     * @param d
     * @return
     */
    private static float bytesToFloat(byte a, byte b, byte c, byte d) {
        return ByteBuffer.wrap(new byte[]{a, b, c, d}).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }

    /**
     * Renders a font and returns an array of vec2 vertices that correspond to positions.
     *
     * @param text
     * @return
     */
    private static float[] renderFont(@NotNull String text, float fontSize) {
        float SHRINK = 5000;
        float SPACING = -1.f;

        // Render font to byte array.
        STBEasyFont.stb_easy_font_spacing(SPACING);
        int quadsPerChar = 400;
        ByteBuffer fontBuffer = ByteBuffer.allocateDirect(text.length() * quadsPerChar).order(ByteOrder.nativeOrder());
        int fontNumOfQuads = STBEasyFont.stb_easy_font_print(0.f, 0.f, text, null, fontBuffer);

        // Convert to list of quads.
        float[][] quads = new float[fontNumOfQuads][];
        for (int i = 0; i < fontNumOfQuads; ++i) {
            quads[i] = new float[4 * 7];
            for (int q = 0; q < 4; ++q) {
                byte[] quadBuffer = new byte[16];
                fontBuffer.get(quadBuffer, 0, 16);
                quads[i][4 * q] = bytesToFloat(quadBuffer[0], quadBuffer[1], quadBuffer[2], quadBuffer[3]);
                quads[i][4 * q + 1] = bytesToFloat(quadBuffer[4], quadBuffer[5], quadBuffer[6], quadBuffer[7]);
                quads[i][4 * q + 2] = bytesToFloat(quadBuffer[8], quadBuffer[9], quadBuffer[10], quadBuffer[11]);
                quads[i][4 * q + 3] = quadBuffer[12] / 255.f;
                quads[i][4 * q + 4] = quadBuffer[13] / 255.f;
                quads[i][4 * q + 5] = quadBuffer[14] / 255.f;
                quads[i][4 * q + 6] = quadBuffer[15] / 255.f;
            }
        }

        // Convert to list of triangles.
        ArrayList<Float> triangles = new ArrayList<>();
        for (float[] quad : quads) {
            int[] copyVertices = {0, 1, 2, 0, 2, 3};
            for (int q : copyVertices) {
                triangles.add(quad[4 * q] / SHRINK * fontSize);
                triangles.add(quad[4 * q + 1] / SHRINK * fontSize);
            }
        }

        // Convert to array.
        float[] result = new float[triangles.size()];
        for (int i = 0; i < result.length; ++i)
            result[i] = triangles.get(i);

        // Flip vertically.
        for (int i = 0; i < result.length / 2; ++i)
            result[2 * i + 1] *= -1;

        // Center coordinates.
        float width = STBEasyFont.stb_easy_font_width(text) / SHRINK * fontSize;
        float height = STBEasyFont.stb_easy_font_height(text) / SHRINK * fontSize;
        for (int i = 0; i < result.length / 2; ++i) {
            result[2 * i] -= width / 2;
            result[2 * i + 1] += height / 2;
        }

        // Return
        return result;
    }

    private void initGL() {
        // Initialize VAO.
        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        // Initialize VBO.
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        float[] fontRender = renderFont(this.text, 20);
        glBufferData(GL_ARRAY_BUFFER, fontRender, GL_STATIC_DRAW);
        vertexCount = fontRender.length / 2;

        // Compile shaders.
        vertexShader = loadAndCompileShader("src/main/java/xwing/visual/shaders/vpolygon.vert", GL_VERTEX_SHADER);
        fragmentShader = loadAndCompileShader("src/main/java/xwing/visual/shaders/vpolygon.frag", GL_FRAGMENT_SHADER);

        // Link program.
        program = glCreateProgram();
        glAttachShader(program, vertexShader);
        glAttachShader(program, fragmentShader);
        glBindFragDataLocation(program, 0, "outColor");
        glLinkProgram(program);
        glUseProgram(program);

        // Set attributes.
        int positionAttr = glGetAttribLocation(program, "position");
        glEnableVertexAttribArray(positionAttr);
        glVertexAttribPointer(positionAttr, 2, GL_FLOAT, false, 0, 0);
    }


    void draw() {
        glUseProgram(program);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBindVertexArray(vertexArray);

        int positionOffset = glGetUniformLocation(program, "positionOffset");
        glUniform2f(positionOffset, boardCoordinateToGL(position.x, true), boardCoordinateToGL(position.y, true));
        int angle = glGetUniformLocation(program, "angle");
        glUniform1f(angle, 0.f);
        int color = glGetUniformLocation(program, "color");
        glUniform3f(color, 0.0f, 0.0f, 0.5f);

        glDrawArrays(GL_TRIANGLES, 0, vertexCount * 2);
    }

    @Override
    public void close() {
        glDeleteProgram(program);
        glDeleteShader(fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteBuffers(vertexBuffer);
        glDeleteVertexArrays(vertexArray);
    }
}