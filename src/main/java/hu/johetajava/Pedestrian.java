package hu.johetajava;

public class Pedestrian extends Entity{

    public Pedestrian(int id, Position position, Directions direction, int speed, Commands nextCommand) {
        super(id, position, direction, speed, nextCommand);
    }
}
