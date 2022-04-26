package rip.az;

public class Main {

    public static void main(String[] args) {
        ConnectFourBoard board = new ConnectFourBoard();
        Strategy<ConnectFourBoard, ConnectFourMove>[] strategies = new Strategy[]{
                new HumanInputStrategy(),
//                new ScriptStrategy(),
                new MinimaxStrategy()

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

            ConnectFourMove move = strategies[board.getTurn() % 2].getMove(board);
            board.applyMove(move);
        }
    }
}
