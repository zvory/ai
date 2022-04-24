package rip.az;

public class Main {

    public static void main(String[] args) {
        ConnectFourBoard board = new ConnectFourBoard();
        ConnectFourStrategy[] strategies = new ConnectFourStrategy[]{
                new HumanConnectFourStrategy(),
                new RandomConnectFourStrategy()
        };
        while (true) {
            if (board.isTie()) {
                System.out.println("Tie!");
                System.exit(0);
            }
            Player winner = board.getWinner();
            if (winner != Player.NONE) {
                System.out.println("Player " + winner.name() + " wins!");
                System.exit(0);
            }

            ConnectFourMove move = strategies[board.getTurn() % 2].getMove(board);
            board.applyMove(move);
        }
    }
}
