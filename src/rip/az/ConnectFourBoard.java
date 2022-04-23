package rip.az;

import org.jetbrains.annotations.NotNull;
import rip.az.LocationGenerator.DownAndRightLocationGenerator;
import rip.az.LocationGenerator.LocationGenerator;
import rip.az.LocationGenerator.RowLocationGenerator;
import rip.az.LocationGenerator.UpAndRightLocationGenerator;

import java.util.Arrays;

public class ConnectFourBoard implements Board {
    public static final LocationGenerator ROW_LOCATION_GENERATOR = new RowLocationGenerator();
    public static final LocationGenerator DOWN_AND_RIGHT_LOCATION_GENERATOR = new DownAndRightLocationGenerator();
    public static final LocationGenerator UP_AND_RIGHT_LOCATION_GENERATOR = new UpAndRightLocationGenerator();
    private final int WIDTH = 7;
    private final int HEIGHT = 6;
    private final int CONNECT_AMOUNT = 4;
    // r0c0, r0c1.... c5c5, r5c6
    Player[] board;
    int turn;
    int[] heights;

    public ConnectFourBoard() {
        this.board = new Player[WIDTH * HEIGHT];
        this.turn = 0;
        this.heights = new int[WIDTH];
    }

    private ConnectFourBoard(Player[] board, int turn, int[] heights) {
        this.board = board;
        this.turn = turn;
        this.heights = heights;
    }

    public Player getContentsAt(int column, int row) {
        return board[rowColumnToBoardIndex(column, row)];
    }

    private int columnToOpenSpot(int column) {
        return heights[column];
    }

    private int rowColumnToBoardIndex(int column, int row) {
        return column + row * WIDTH;
    }

    @Override
    public ConnectFourBoard clone() {
        Player[] newBoard = Arrays.copyOf(board, WIDTH * HEIGHT);
        return new ConnectFourBoard(newBoard, turn, heights);
    }

    public void applyMove(@NotNull ConnectFourMove move) {
        int column = move.getColumn();
        int row = columnToOpenSpot(column);

        if (row == HEIGHT) {
            System.err.println("Placing tile on column that's topped out. Column: " + column);
            System.err.println(this.toString());
            throw new IllegalStateException();
        }
        if (getContentsAt(column, row) != Player.NONE) {
            System.err.println("Placing tile on spot that's already filled. Column: " + column);
            System.err.println(this.toString());
            throw new IllegalStateException();
        }

        Player newContents = Player.getTurn(turn);
        turn++;
        board[rowColumnToBoardIndex(column, row)] = newContents;
        heights[row]++;
    }

    @Override
    public Player getWinner() {
        Player result;
        for (int row = 0; row < HEIGHT; row++) {
            result = evalSeries(0, row, ROW_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;

            result = evalSeries(0, row, DOWN_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;

            result = evalSeries(0, row, UP_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;
        }

        for (int column = 0; column < WIDTH; column++) {
            result = evalSeries(column, HEIGHT - 1, DOWN_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;

            result = evalSeries(column, 0, UP_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;
        }
        return Player.NONE;
    }

    @NotNull
    private Player evalSeries(int startCol, int startRow, LocationGenerator generator) {
        Player lastSeen = Player.NONE;
        int inARow = 0;

        for (int distance = 0; true; distance++) {
            int[] offsets = generator.getLocationForIteration(distance);
            int column = startCol + offsets[0];
            int row = startRow + offsets[1];
            if (isOutOfBounds(column, row)) {
                break;
            } else {
                Player contentsAt = getContentsAt(column, row);
                if (contentsAt == Player.NONE) {
                    inARow = 0;
                    lastSeen = contentsAt;
                } else if (contentsAt == lastSeen) {
                    inARow++;
                    if (inARow == CONNECT_AMOUNT) {
                        return contentsAt;
                    }
                } else {
                    inARow = 1;
                    lastSeen = contentsAt;
                }
            }
        }
        return Player.NONE;
    }

    private boolean isOutOfBounds(int column, int row) {
        if (column >= WIDTH) {
            return true;
        } else if (column < 0) {
            return true;
        } else if (row >= HEIGHT) {
            return true;
        } else if (row < 0) {
            return true;
        } else {
            return false;
        }
    }
}
