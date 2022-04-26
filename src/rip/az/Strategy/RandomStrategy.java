package rip.az.Strategy;

import rip.az.Board;
import rip.az.Move;
import rip.az.Util;

import java.util.List;

public class RandomStrategy<B extends Board, M extends Move> extends Strategy {
    @Override
    public Move getMove(Board board) {
        List<Move> moves = board.getPossibleMoves();
        return moves.get(Util.randInRange(moves.size()));
    }
}
