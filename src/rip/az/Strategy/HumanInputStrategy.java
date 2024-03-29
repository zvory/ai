package rip.az.Strategy;

import rip.az.Board;
import rip.az.Move;

public class HumanInputStrategy<B extends Board<M>, M extends Move> extends Strategy {
    @Override
    public Move getMove(Board board) {
        return board.getHumanInput();
    }
}
