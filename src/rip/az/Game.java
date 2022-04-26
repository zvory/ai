package rip.az;

public class Game<B extends Board<M>, M extends Move> {
    private B board;

    public Game(B board) {
        this.board = board;
    }

    public void runGame() {
        Strategy<B, M>[] strategies = new Strategy[]{
                new HumanInputStrategy<B, M>(),
//                new ScriptStrategy(),
                new MinimaxStrategy<B, M>()

        };
        while (true) {
            Player winner = board.getWinner();
            if (winner != Player.NONE) {
                System.out.println("Player " + winner.name() + " wins!");
                System.exit(0);
            }
            if (board.noMoreMovesPossible()) {
                System.out.println("Tie!");
                System.exit(0);
            }

            M move = strategies[board.getTurn() % 2].getMove(board);
            board.applyMove(move);
        }

    }
}
