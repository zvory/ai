package rip.az.Strategy;

import rip.az.Board;
import rip.az.Move;
import rip.az.Player;
import rip.az.Util;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MinimaxV2Strategy<B extends Board<M>, M extends Move> extends Strategy {
    private final int ROLLOUT_COUNT = 100;

    private boolean isAlmostMated(Board board) {
        List<Move> moves = (List<Move>) board.getPossibleMoves();
        for (Move move : moves) {
            board.applyMove(move);
            if (board.getIsLatestMoveWinning() != Player.NONE) {
                board.undo();
                return true;
            }
            board.undo();
        }
        return false;
    }

    private double rollout(Board board) {
        int movesDone = 0;
        while (!board.noMoreMovesPossible()) {
            List<Move> moves = (List<Move>) board.getPossibleMoves();
            Player currentPlayer = board.getCurrentPlayer();
            boolean didMove = false;

            // select a move that doesn't leave us almost mated
            while (moves.size() != 0) {
                Move move = moves.remove(Util.randInRange(moves.size()));
                board.applyMove(move);
                movesDone++;

                Player winner = board.getIsLatestMoveWinning();
                // this move won us the game
                if (winner == currentPlayer) {
                    board.undoTimes(movesDone);
                    return winner == Player.ONE ? -1 : 1;
                }

                // so we didn't win
                // are we about to lose and there's another move?
                if (moves.size() > 0 && isAlmostMated(board)) {
                    // do another move
                    board.undo();
                    movesDone--;
                } else {
                    // take the move
                    break;
                }
            }
        }
        board.undoTimes(movesDone);
        return 0;
    }

    // runs random rollouts starting from this state and returns a score
    private double rollouts(Board board) {
        double score = 0;
        for (int i = 0; i < ROLLOUT_COUNT; i++) {
            Board before = board.clone();
            score += rollout(board);
        }
        return score / ROLLOUT_COUNT;
    }

    // Player.ONE is always max
    private SearchResult minimax(Board board, int depth) {
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

        List<SearchResult> moveResults = moves.stream().map(move -> {
            board.applyMove(move);
            SearchResult result = minimax(board, depth - 1);
            board.undo();
            return new SearchResult(result.utility, move);
        }).collect(Collectors.toList());

//        if (depth == DEPTH) {
//            System.out.println("evaluation of moves: ");
//            for (SearchResult res : moveResults) {
//                System.out.println(res);
//            }
//        }

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
