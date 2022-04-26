package rip.az;

public class Main {
    public static void main(String[] args) {
        ConnectFourBoard board = new ConnectFourBoard();
        Game game = new Game(board);
        game.runGame();
    }
}
