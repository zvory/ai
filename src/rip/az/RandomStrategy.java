package rip.az;

import java.util.List;

public class RandomStrategy<B extends Board, M extends Move> extends Strategy {
    @Override
    public Move getMove(Board board) {
        List<Move> moves = board.getPossibleMoves();
        return moves.get(AIUtil.randInRange(moves.size()));
    }
}
