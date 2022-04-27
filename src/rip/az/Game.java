package rip.az;

import rip.az.Strategy.Strategy;

public class Game<B extends Board<M>, M extends Move> {
    private B board;
    private Strategy strategyOne;
    private Strategy strategyTwo;

    public Game(B board, Strategy strategyOne, Strategy strategyTwo) {
        this.board = board;
        this.strategyOne = strategyOne;
        this.strategyTwo = strategyTwo;
    }

    public Player runGame() {
        Strategy<B, M>[] strategies = new Strategy[]{
                strategyOne,
                strategyTwo
        };
        while (true) {
            Player winner = board.getWinner();
            if (winner != Player.NONE) {
                return winner;
            }
            if (board.noMoreMovesPossible()) {
                return Player.NONE;
            }

            M move = strategies[board.getTurn() % 2].getMove(board);
            board.applyMove(move);
        }

    }
}
