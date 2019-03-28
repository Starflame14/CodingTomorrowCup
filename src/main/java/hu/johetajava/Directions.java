package hu.johetajava;

enum Directions {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    NONE
}
class Direction{
    public static Directions getDirectionBySign(char sign){
        switch (sign){
            case '>': return Directions.RIGHT;
            case '<': return Directions.LEFT;
            case 'v': return Directions.DOWN;
            case '^': return Directions.UP;
            default:
                return Directions.NONE;
        }
    }

    public static Position getPositionByDirection(Position oldPosition, Directions direction, int speed) {
        Position newPos = oldPosition.clone();
        switch (direction) {
            case UP:
                newPos.y -= speed;
                break;
            case DOWN:
                newPos.y += speed;
                break;
            case LEFT:
                newPos.x -= speed;
                break;
            case RIGHT:
                newPos.x += speed;
                break;
        }
        return newPos;
    }
}