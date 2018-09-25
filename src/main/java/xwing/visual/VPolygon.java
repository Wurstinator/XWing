package xwing.visual;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import xwing.Constants;
import xwing.logic.Position;

import java.io.Closeable;
import java.util.Arrays;

import static org.lwjgl.opengl.GL30.*;
import static xwing.visual.Util.loadAndCompileShader;

/**
 * Represents a drawable polygon of one single color.
 */
public class VPolygon implements Closeable {
    private Polygon polygon;
    private Position position;

    private int vertexArray;
    private int vertexBuffer;
    private int elementBuffer;
    private int vertexShader;
    private int fragmentShader;
    private int program;

    public VPolygon(Polygon polygon) {
        this.polygon = polygon;
        this.position = new Position(0, 0, 0);
        assert (polygon.getNumPoints() > 2);
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
        Coordinate[] polygonCoordinates = Arrays.copyOfRange(polygon.getCoordinates(), 0, polygon.getNumPoints() - 1);
        float[] vertices = new float[polygonCoordinates.length * 2];
        for (int i = 0; i < polygonCoordinates.length; ++i) {
            vertices[2 * i] = boardCoordinateToGL(polygonCoordinates[i].x, false);
            vertices[2 * i + 1] = boardCoordinateToGL(polygonCoordinates[i].y, false);
        }

        // Create element buffer indices.
        int[] eboIndices = new int[3 * (polygonCoordinates.length - 2)];
        for (int i = 0; i < polygonCoordinates.length - 2; ++i) {
            eboIndices[3 * i] = 0;
            eboIndices[3 * i + 1] = i + 1;
            eboIndices[3 * i + 2] = i + 2;
        }

        // Initialize VAO.
        vertexArray = glGenVertexArrays();
        glBindVertexArray(vertexArray);

        // Initialize VBO.
        vertexBuffer = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

        // Initialize EBO.
        elementBuffer = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, eboIndices, GL_STATIC_DRAW);

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
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
        glBindVertexArray(vertexArray);

        int positionOffset = glGetUniformLocation(program, "positionOffset");
        glUniform2f(positionOffset, boardCoordinateToGL(position.x, true), boardCoordinateToGL(position.y, true));
        int angle = glGetUniformLocation(program, "angle");
        glUniform1f(angle, (float) position.getAngle());
        int color = glGetUniformLocation(program, "color");
        glUniform3f(color, 1.0f, 0.0f, 0.0f);

        glDrawElements(GL_TRIANGLES, 3 * (polygon.getNumPoints() - 3), GL_UNSIGNED_INT, 0);
    }

    @Override
    public void close() {
        glDeleteProgram(program);
        glDeleteShader(fragmentShader);
        glDeleteShader(vertexShader);
        glDeleteBuffers(elementBuffer);
        glDeleteBuffers(vertexBuffer);
        glDeleteVertexArrays(vertexArray);
    }
}
