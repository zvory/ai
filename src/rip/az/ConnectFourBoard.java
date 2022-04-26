package rip.az;

import org.jetbrains.annotations.NotNull;
import rip.az.LocationGenerator.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ConnectFourBoard implements Board<ConnectFourMove> {
    public static final LocationGenerator ROW_LOCATION_GENERATOR = new RowLocationGenerator();
    public static final LocationGenerator DOWN_AND_RIGHT_LOCATION_GENERATOR = new DownAndRightLocationGenerator();
    public static final LocationGenerator UP_AND_RIGHT_LOCATION_GENERATOR = new UpAndRightLocationGenerator();
    public static final ColumnLocationGenerator COLUMN_LOCATION_GENERATOR = new ColumnLocationGenerator();
    private final int WIDTH = 7;
    private final int HEIGHT = 6;
    private final int CONNECT_AMOUNT = 4;
    // r0c0, r0c1.... c5c5, r5c6
    Player[] board;
    int turn;
    int[] heights;
    List<Integer> moveSequence;

    public ConnectFourBoard() {
        this.board = new Player[WIDTH * HEIGHT];
        this.moveSequence = new ArrayList<>();
        Arrays.fill(this.board, Player.NONE);
        this.turn = 0;
        this.heights = new int[WIDTH];
    }

    public void undo() {
        int toUndo = moveSequence.remove(moveSequence.size() - 1);
        int boardLocation = rowColumnToBoardIndex(toUndo, heights[toUndo] - 1);
        heights[toUndo]--;
        turn--;
        board[boardLocation] = Player.NONE;
    }

    @Override
    public void undoTimes(int times) {
        for (int i = 0; i < times; i++) {
            undo();
        }
    }

    public Player getContentsAt(int column, int row) {
        if (isOutOfBounds(column, row)) {
            throw new IllegalArgumentException("Attempting to access OutOfBounds content. Column: " + column + ", row: " + row);
        }
        return board[rowColumnToBoardIndex(column, row)];
    }

    private int openRowForColumn(int column) {
        return heights[column];
    }

    private int rowColumnToBoardIndex(int column, int row) {
        return column + row * WIDTH;
    }

    @Override
    public String toString() {
        String boardString = "0123456\n";
        for (int row = HEIGHT - 1; row >= 0; row--) {
            for (int column = 0; column < WIDTH; column++) {
                Player contentsAt = getContentsAt(column, row);
                boardString += switch (contentsAt) {
                    case NONE -> ".";
                    case ONE -> "o";
                    case TWO -> "x";
                };
            }
            boardString += '\n';
        }
        return "Turn: " + turn + "\n" + boardString;
    }

    public void applyMoveSeries(int[] moves) {
        for (int move : moves) {
            applyMove(new ConnectFourMove(move));
        }
    }

    public void applyMove(@NotNull ConnectFourMove baseMove) {
        ConnectFourMove move = ConnectFourMove.class.cast(baseMove);
        int column = move.getColumn();
        int row = openRowForColumn(column);

        if (isOutOfBounds(column, row)) {
            System.err.println("Placing tile in out of bounds. Column: " + column + " Row: " + row);
            System.err.println(this);
            throw new IllegalArgumentException();
        }

        moveSequence.add(column);
        Player newContents = Player.getTurn(turn);
        turn++;
        board[rowColumnToBoardIndex(column, row)] = newContents;
        heights[column]++;
    }

    public boolean noMoreMovesPossible() {
        for (int height : heights) {
            if (height != HEIGHT) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getTurn() {
        return turn;
    }

    @Override
    public Player getCurrentPlayer() {
        return Player.getTurn(turn);
    }

    @Override
    public ConnectFourMove getHumanInput() {
        System.out.print(this);
        System.out.print("Your move: ");
        Scanner scan = new Scanner(System.in);
        int move = scan.nextInt();
        return new ConnectFourMove(move);
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
            result = evalSeries(column, 0, COLUMN_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;

            result = evalSeries(column, HEIGHT - 1, DOWN_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;

            result = evalSeries(column, 0, UP_AND_RIGHT_LOCATION_GENERATOR);
            if (result != Player.NONE) return result;
        }
        return Player.NONE;
    }

    @Override
    public List<ConnectFourMove> getPossibleMoves() {
        ArrayList<ConnectFourMove> moves = new ArrayList<>();
        for (int column = 0; column < WIDTH; column++) {
            if (!isOutOfBounds(column, openRowForColumn(column))) {
                moves.add(new ConnectFourMove(column));
            }
        }
        return moves;
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
