package rip.az;

public abstract class Strategy<B extends Board, M extends Move> {
    public abstract M getMove(B board);
}
