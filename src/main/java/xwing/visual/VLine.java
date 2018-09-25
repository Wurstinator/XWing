package xwing.visual;

import xwing.Constants;
import xwing.logic.Position;

import java.io.Closeable;

import static org.lwjgl.opengl.GL30.*;
import static xwing.visual.Util.loadAndCompileShader;

/**
 * Represents a drawable polygon of one single color.
 */
public class VLine implements Closeable {
    private float lineLength;
    private Position position;

    private int vertexArray;
    private int vertexBuffer;
    private int vertexShader;
    private int fragmentShader;
    private int program;

    public VLine(float length) {
        this.lineLength = length;
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

    private void initGL() {
        // Convert polygon to float array.
        float[] vertices = {0.f, 0.f, 0.f, boardCoordinateToGL(lineLength, false)};

        // Initialize VAO.
        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        // Initialize VBO.
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

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


    public void draw() {
        glUseProgram(program);
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBindVertexArray(vertexArray);

        int positionOffset = glGetUniformLocation(program, "positionOffset");
        glUniform2f(positionOffset, boardCoordinateToGL(position.x, true), boardCoordinateToGL(position.y, true));
        int angle = glGetUniformLocation(program, "angle");
        glUniform1f(angle, (float) position.getAngle());
        int color = glGetUniformLocation(program, "color");
        glUniform3f(color, 0.0f, 0.0f, 0.0f);

        glLineWidth(2);
        glDrawArrays(GL_LINE_STRIP, 0, 4);
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
