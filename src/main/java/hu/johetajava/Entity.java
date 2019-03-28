package hu.johetajava;

public class Entity {
    /**
     * ID
     * unique identifier
     * It's -1 if something has no id
     */
    protected int id;

    /**
     * The current position of the entity
     */
    protected Position position;

    /**
     * The absolute direction of the entity's movement
     */
    protected Directions direction;

    /**
     * The current speed of the entity
     */
    protected int speed;

    /**
     * The expected command of the entity
     */
    protected Commands nextCommand;

    public Entity(int id, Position position, Directions direction, int speed, Commands nextCommand) {
        this.id = id;
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.nextCommand = nextCommand;
    }

    /**
     * Get the position that the entity would stand if it moves in  a direction
     *
     * @param position
     * @param direction
     * @param speed     The current speed of the
     * @return The position after the movement
     */
    public static Position getPositionByDirection(Position position, Directions direction, int speed) {
        // TODO get pos by direction
        return position;
    }

    public void move() {
        setPosition(getPositionByDirection(getPosition(), getDirection(), getSpeed()));
    }

    /**
     * Rotate the entity by 90° in a specified direction
     * @param direction
     */
    public void turn(Directions direction) {
        // TURN LEFT:
        if (direction == Directions.LEFT) {
            switch (getDirection()) {
                case LEFT:
                    setDirection(Directions.DOWN);
                    break;
                case DOWN:
                    setDirection(Directions.RIGHT);
                    break;
                case RIGHT:
                    setDirection(Directions.UP);
                    break;
                case UP:
                    setDirection(Directions.LEFT);
                    break;
            }
        }
        // TURN RIGHT
        else if(direction == Directions.RIGHT){
            switch (getDirection()) {
                case LEFT:
                    setDirection(Directions.UP);
                    break;
                case DOWN:
                    setDirection(Directions.LEFT);
                    break;
                case RIGHT:
                    setDirection(Directions.DOWN);
                    break;
                case UP:
                    setDirection(Directions.RIGHT);
                    break;
            }
        }
    }

    /**
     * Increase speed
     */
    public void accelerate(){
        setSpeed(getSpeed() + 1);
    }

    /**
     * Decrease speed
     */
    public void decelerate(){
        setSpeed(getSpeed() - 1);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = Math.max(speed, 0);
    }
}
