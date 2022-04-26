package rip.az;

import java.util.List;

public interface Board {
    Player getWinner();

    List<? extends Move> getPossibleMoves();

    boolean noMoreMovesPossible();

    int getTurn();

    Player getCurrentPlayer();

    Move getHumanInput();

    void applyMove(Move m);

    void undo();

    void undoTimes(int movesDone);
}
