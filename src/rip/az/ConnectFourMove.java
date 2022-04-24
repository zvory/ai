package rip.az;

public class ConnectFourMove extends Move {
    private int column;

    public ConnectFourMove(int column) {
        this.column = column;
    }
    public int getColumn() {
        return column;
    }
}
