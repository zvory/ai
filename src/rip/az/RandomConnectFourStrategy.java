package rip.az;

public class RandomConnectFourStrategy extends ConnectFourStrategy {
    @Override
    public ConnectFourMove getMove(ConnectFourBoard board) {
        var moves = board.getPossibleMoves();
        return moves.get(AIUtil.randInRange(moves.size()));
    }
}
