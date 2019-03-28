package hu.johetajava;

public enum Commands {
    // Normal commands:
    NO_OP,
    ACCELERATION,
    DECELERATION,
    CAR_INDEX_LEFT,
    CAR_INDEX_RIGHT,
    X,

    // Immediate commands:
    CLEAR,
    FULL_THROTTLE,
    EMERGENCYY_BRAKE,
    GO_LEFT,
    GO_RIGHT
}

class Command {
    public static Commands getNextCommandBySign(char sign) {
        switch (sign) {
            case '0':
                return Commands.NO_OP;
            case '+':
                return Commands.ACCELERATION;
            case '-':
                return Commands.DECELERATION;
            case '<':
                return Commands.CAR_INDEX_LEFT;
            case '>':
                return Commands.CAR_INDEX_RIGHT;
            case 'X':
                return Commands.X;
            default:
                Main.error("INVALID COMMAND IDENTIFIER \"" + sign + "\"");
                return Commands.NO_OP;
        }

    }
}
