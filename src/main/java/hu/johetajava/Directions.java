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
        }
        Main.error("INVALID DIRECTION IDENTIFIER!");
        return Directions.UP;
    }
}