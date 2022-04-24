package rip.az;

import java.util.List;

public interface Board {
    Player getWinner();

    List<? extends Move> getPossibleMoves();

    boolean isTie();

    int getTurn();
}
