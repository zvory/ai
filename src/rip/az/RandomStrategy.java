package rip.az;

public class RandomStrategy<B extends Board, M extends Move> extends Strategy {
    @Override
    public Move getMove(Board board) {
        var moves = board.getPossibleMoves();
        return moves.get(AIUtil.randInRange(moves.size()));
    }
}
