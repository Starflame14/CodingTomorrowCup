package hu.johetajava;

public class Pedestrian extends Entity{

    public Pedestrian(int id, Position position, Direction direction, int speed, Command nextCommand) {
        super(id, position, direction, speed, nextCommand);
    }
}
