package logic;

public class Position {
    public double x;
    public double y;
    private double angle; // in radians; clockwise; angle 0 = pointing upwards

    public Position(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle % (Math.PI * 2);
    }

    public void moveTo(double distance, double directionAngle) {
        x += Math.sin(directionAngle) * distance;
        y += Math.cos(directionAngle) * distance;
    }

    public void turn(double turn) {
        angle = (angle + turn) % (Math.PI * 2);
    }

    public void forward(double distance) {
        moveTo(distance, angle);
    }

    public void right(double distance) {
        moveTo(distance, (angle + Math.PI / 4) % (Math.PI * 2));
    }

    public void turn90() {
        turn(Math.PI / 4);
    }

    public void turn180() {
        turn(Math.PI / 2);
    }

    public void turn270() {
        turn(Math.PI * 3 / 4);
    }
}
