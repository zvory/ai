package rip.az.Strategy;

import rip.az.Board;
import rip.az.Move;

public abstract class Strategy<B extends Board<M>, M extends Move> {
    protected final int DEPTH = 3;
    public abstract M getMove(B board);
}
