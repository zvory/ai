package rip.az;

import java.util.List;

public interface Board<M extends Move> {
    Player getWinner();

    Player getIsLatestMoveWinning();

    List<M> getPossibleMoves();

    boolean noMoreMovesPossible();

    int getTurn();

    Player getCurrentPlayer();

    M getHumanInput();

    void applyMove(M m);

    void undo();

    void undoTimes(int movesDone);
}
