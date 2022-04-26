package rip.az;

public class HumanInputStrategy<B extends Board, M extends Move> extends Strategy {
    public Move getMove(Board board) {
        return board.getHumanInput();
    }
}
