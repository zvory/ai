package rip.az.Strategy;

import rip.az.Board;
import rip.az.Move;
import rip.az.Player;
import rip.az.Util;

import java.util.List;

public class AlphaBetaStrategy<B extends Board<M>, M extends Move> extends Strategy {
    private final int ROLLOUT_COUNT = 100;

    // board may be won when it's called
    private double rollout(Board board) {
        int movesDone = 0;
        while (!board.noMoreMovesPossible()) {
            Player winner = board.getIsLatestMoveWinning();
            if (winner != Player.NONE) {
                board.undoTimes(movesDone);
                return winner == Player.ONE ? 1 : -1;
            } else {
                List<Move> moves = (List<Move>) board.getPossibleMoves();
                movesDone++;
                Move move = moves.get(Util.randInRange(moves.size()));
                board.applyMove(move);
            }
        }
        board.undoTimes(movesDone);
        return 0;
    }

    // runs random rollouts starting from this state and returns a score
    private double rollouts(Board board) {
        double score = 0;
        for (int i = 0; i < ROLLOUT_COUNT; i++) {
            score += rollout(board);
        }
        return score / ROLLOUT_COUNT;
    }

    // Player.ONE is always max
    private SearchResult minimax(Board board, int depth, double atLeastAlpha, double atMostBeta) {
        Player winner = board.getIsLatestMoveWinning();
        if (winner == Player.ONE) {
            return new SearchResult(Double.MAX_VALUE / 2, null);
        } else if (winner == Player.TWO) {
            return new SearchResult(-Double.MAX_VALUE / 2, null);
        }
        if (board.noMoreMovesPossible()) {
            return new SearchResult(0, null);
        }
        if (depth == 0) {
            return new SearchResult(rollouts(board), null);
        }

        List<Move> moves = (List<Move>) board.getPossibleMoves();


        double v;
        if (isCurrentPlayerMax(board)) {
            v = Double.NEGATIVE_INFINITY;
        } else {
            v = Double.POSITIVE_INFINITY;
        }

        Move bestMove = null;
        for (Move move : moves) {
            board.applyMove(move);
            SearchResult result = minimax(board, depth - 1, atLeastAlpha, atMostBeta);
            board.undo();
            if (isCurrentPlayerMax(board)) {
                // if this move is better than our best so far move
                if (result.utility > v) {
                    // make it our best move
                    v = result.utility;
                    bestMove = move;
                    // don't consider moves any worse than this
                    atLeastAlpha = Math.max(atLeastAlpha, v);
                }
                //
                if (v >= atMostBeta) {
                    return new SearchResult(v, bestMove);
                }
            } else {
                if (result.utility < v) {
                    v = result.utility;
                    bestMove = move;
                    atMostBeta = Math.min(atMostBeta, v);
                }
                if (v <= atLeastAlpha) {
                    return new SearchResult(v, bestMove);
                }
            }
        }
        return new SearchResult(v, bestMove);

//        if (depth == DEPTH) {
//            System.out.println("evaluation of moves: ");
//            for (SearchResult res : moveResults) {
//                System.out.println(res);
//            }
//        }

    }

    @Override
    public String toString() {
        return "AlphaBetaStrategy{" +
                "DEPTH=" + DEPTH +
                ", ROLLOUT_COUNT=" + ROLLOUT_COUNT +
                '}';
    }

    private boolean isCurrentPlayerMax(Board board) {
        return board.getCurrentPlayer() == Player.ONE;
    }

    @Override
    public Move getMove(Board board) {
        SearchResult result = minimax(board, DEPTH, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//        System.out.println("AI EVALUATION: " + result.utility + " Doing move " + result.move + " for turn " + board.getTurn());
        return result.move;
    }

    private class SearchResult {
        double utility;
        Move move;

        public SearchResult(double utility, Move move) {
            this.utility = utility;
            this.move = move;
        }

        public int compareTo(SearchResult other) {
            return (int) Math.signum(utility - other.utility);
        }

        @Override
        public String toString() {
            return "SearchResult{" +
                    "utility=" + utility +
                    ", move=" + move +
                    '}';
        }
    }
}
