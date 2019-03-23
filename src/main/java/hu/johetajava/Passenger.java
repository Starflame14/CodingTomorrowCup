package hu.johetajava;

public class Passenger extends Entity{
    private static final int MAX_SPEED = 1;
    protected Position destination;
    /**
     * The passenger's car id
     */
    protected int carId;


    public Passenger(int id, Position position, int carId, Position destination) {
        super(id, position, Directions.NONE, 0, Commands.NO_OP);
        this.destination = destination;
        this.carId = carId;
    }

    @Override
    public void setSpeed(int speed){
        this.speed = Math.min(MAX_SPEED, Math.max(0, speed));
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "destination=" + destination +
                ", carId=" + carId +
                ", id=" + id +
                ", position=" + position +
                '}';
    }
}
