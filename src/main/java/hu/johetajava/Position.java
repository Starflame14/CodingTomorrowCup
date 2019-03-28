package hu.johetajava;

public class Position {
    public int x;
    public int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        Position pos2 = (Position) obj;
        return this.x == pos2.x && this.y == pos2.y;
    }

    public Position clone() {
        return new Position(x, y);
    }
}
