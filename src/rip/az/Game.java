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

    public Result runGame() {
        Strategy<B, M>[] strategies = new Strategy[]{
                strategyOne,
                strategyTwo
        };
        long firstTimeSpent = 0;
        long secondTimeSpent = 0;
        while (true) {
            Player winner = board.getWinner();
            if (winner != Player.NONE) {
                return new Result(winner, firstTimeSpent, secondTimeSpent);
            }
            if (board.noMoreMovesPossible()) {
                return new Result(Player.NONE, firstTimeSpent, secondTimeSpent);
            }

            long startTime = System.nanoTime();
            M move = strategies[board.getTurn() % 2].getMove(board);
            long endTime = System.nanoTime();
            long totalTime = endTime - startTime;
            if (board.getTurn() % 2 == 0) {
                firstTimeSpent += totalTime;
            } else {
                secondTimeSpent += totalTime;
            }
            board.applyMove(move);
        }
    }

    public class Result {
        Player winner;
        long firstTimeSpent;
        long secondTimeSpent;

        public Result(Player winner, long firstTimeSpent, long secondTimeSpent) {
            this.winner = winner;
            this.firstTimeSpent = firstTimeSpent;
            this.secondTimeSpent = secondTimeSpent;
        }
    }
}
