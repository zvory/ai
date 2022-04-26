package rip.az;

import java.util.Arrays;

public class ScriptStrategy<B extends Board, M extends Move> extends Strategy<B, M> {
    private int[] script = new int[]{
//            3, 2, 1, 4
            3, 4, 4, 1, 2, 2, 0
    };

    @Override
    public Move getMove(Board board) {
        int index = board.getTurn() / 2;
        if (index >= script.length) {
            return new HumanInputStrategy<>().getMove(board);
        } else {
            Move m = new ConnectFourMove(script[index]);
            System.out.println("Doing move " + script[index] + " of " + Arrays.toString(script));
            board.applyMove(m);
            System.out.println(board);
            board.undo();

            return m;
        }
    }
}
