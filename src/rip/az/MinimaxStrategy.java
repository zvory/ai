package rip.az;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MinimaxStrategy<B extends Board<M>, M extends Move> extends Strategy {
    private final int DEPTH = 4;
    private final int ROLLOUT_COUNT = 100;

    // board may be won when it's called
    private double rollout(Board board) {
        int movesDone = 0;
        while (!board.noMoreMovesPossible()) {
            Player winner = board.getWinner();
            if (winner != Player.NONE) {
                board.undoTimes(movesDone);
                return winner == Player.ONE ? 1 : -1;
            } else {
                List<Move> moves = (List<Move>) board.getPossibleMoves();
                movesDone++;
                Move move = moves.get(AIUtil.randInRange(moves.size()));
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
    private SearchResult minimax(Board board, int depth) {
        Player winner = board.getWinner();
        if (winner == Player.ONE) {
            return new SearchResult(Double.MAX_VALUE / 2, null);
        } else if (winner == Player.TWO) {
            if (board.getTurn() == 14 && depth == 2) {
                System.out.println("14" + depth);
            }
            return new SearchResult(-Double.MAX_VALUE / 2, null);
        }
        if (board.noMoreMovesPossible()) {
            return new SearchResult(0, null);
        }
        if (depth == 0) {
            return new SearchResult(rollouts(board), null);
        }

        List<Move> moves = (List<Move>) board.getPossibleMoves();

        List<SearchResult> moveResults = moves.stream().map(move -> {
            board.applyMove(move);
            SearchResult result = minimax(board, depth - 1);
            board.undo();
            return new SearchResult(result.utility, move);
        }).collect(Collectors.toList());

        if (depth == DEPTH) {
            System.out.println("evaluation of moves: ");
            for (SearchResult res : moveResults) {
                System.out.println(res);
            }
        }

        if (isCurrentPlayerMax(board)) {
            return Collections.max(moveResults, SearchResult::compareTo);
        } else {
            return Collections.min(moveResults, SearchResult::compareTo);
        }
    }

    private boolean isCurrentPlayerMax(Board board) {
        return board.getCurrentPlayer() == Player.ONE;
    }

    @Override
    public Move getMove(Board board) {
        SearchResult result = minimax(board, DEPTH);
        System.out.println("AI EVALUATION: " + result.utility + " Doing move " + result.move + " for turn " + board.getTurn());
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
