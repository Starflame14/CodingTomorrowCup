package hu.johetajava;

public class Car extends Entity {

    protected int hp = 100; // 0..100
    protected int MAX_SPEED = 3;
    protected int transportedPassengerCount;

    Car(int id, Position position, Directions direction, int speed, Commands nextCommand, int hp, int transportedPassengerCount) {
        super(id, position, direction, speed, nextCommand); // Call the parent constructor
        this.hp = hp;
        this.transportedPassengerCount = transportedPassengerCount;
    }

    public static boolean isThereObstacle(Position position) {

        return false;
    }

    @Override
    public void setSpeed(int speed) {
        this.speed = Math.min(MAX_SPEED, Math.max(0, speed));
    }

    @Override
    public String toString() {
        return "Car{" +
                "hp=" + hp +
                ", MAX_SPEED=" + MAX_SPEED +
                ", transportedPassengerCount=" + transportedPassengerCount +
                ", id=" + id +
                ", position=" + position +
                ", direction=" + direction +
                ", speed=" + speed +
                ", nextCommand=" + nextCommand +
                '}';
    }
}
