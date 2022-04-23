package rip.az;

public enum Player {
    NONE,
    ONE,
    TWO;

    public static Player getTurn(int turn) {
        if (turn % 2 == 0) {
            return ONE;
        } else {
            return TWO;
        }
    }
}
