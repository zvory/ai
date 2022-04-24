package rip.az;

import java.util.Scanner;

public class HumanConnectFourStrategy extends ConnectFourStrategy {
    @Override
    public ConnectFourMove getMove(ConnectFourBoard board) {
        System.out.print(board);
        System.out.print("Your move: ");
        Scanner scan = new Scanner(System.in);
        int move = scan.nextInt();
        return new ConnectFourMove(move);
    }
}
