package hu.johetajava;

public class Car extends Entity{

    protected int hp = 100; // 0..100
    protected int MAX_SPEED = 3;
    protected int transportedPassengerCount;

    Car(int id, Position position, Direction direction, int speed, Command nextCommand, int hp, int transportedPassengerCount){
        super(id, position, direction, speed, nextCommand); // Call the parent constructor
        this.hp = hp;
        this.transportedPassengerCount = transportedPassengerCount;
    }

    @Override
    public void setSpeed(int speed){
        this.speed = Math.min(MAX_SPEED, Math.max(0, speed));
    }

}
