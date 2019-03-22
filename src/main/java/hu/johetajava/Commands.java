package hu.johetajava;

public enum Commands {
    // Normal commands:
    ACCELERATION,
    DECELERATION,
    TURN_LEFT,
    TURN_RIGHT,
    CLEAR_LAST_COMMAND
}

class Command{
    public static Commands getCommandBySign(char sign){
        // TODO get command by sign
        return Commands.TURN_LEFT;
    }
}
