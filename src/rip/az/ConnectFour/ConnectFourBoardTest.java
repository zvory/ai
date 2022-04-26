package rip.az.ConnectFour;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import rip.az.Player;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConnectFourBoardTest {
    ConnectFourBoard board = new ConnectFourBoard();

    @org.junit.jupiter.api.Test
    void applyMove() {
        board.applyMoveSeries(new int[]{
                0, 0, 0, 0, 0, 0
        });
        assertThrows(IllegalArgumentException.class, () -> board.applyMoveSeries(new int[]{
                0, 0, 0, 0, 0, 0
        }));
    }

    private void applyMoveSequenceString(String moveSequence) {
        int[] moves = new int[moveSequence.length()];
        for (int index = 0; index < moveSequence.length(); index++) {
            int column = moveSequence.charAt(index) - '0';
            moves[index] = column;
        }
        board.applyMoveSeries(moves);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "", // empty board
            "001122", // row
            "010101", // column

    })
    void getWinnerNoWinner(String moveSequence) {
        applyMoveSequenceString(moveSequence);
        Assertions.assertEquals(Player.NONE, board.getWinner());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "0101010", // starting from 0,0 row
            "00112233", // starting from 0,0 column
            "01122323303", // starting from 0,0 up and to the right
            "012036261615050", // down and to the right
            "23232323", // starting from 2,0 row
            "22334455", // starting from 2,0 column
            "23344545525", // starting from 2,0 up and to the right
    })
    void getWinnerPlayerOneWinner(String moveSequence) {
        applyMoveSequenceString(moveSequence);
        assertEquals(Player.ONE, board.getWinner(), "Expected player one to win in \n" + board.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "60101010", // starting from 0,0 row
            "600112233", // starting from 0,0 column
            "601122323303", // starting from 0,0 up and to the right
            "6012036261615050", // down and to the right
            "623232323", // starting from 2,0 row
            "622334455", // starting from 2,0 column
            "623344545525", // starting from 2,0 up and to the right
    })
    void getWinnerPlayerTwoWinner(String moveSequence) {
        applyMoveSequenceString(moveSequence);
        assertEquals(Player.TWO, board.getWinner(), "Expected player two to win in \n" + board.toString());
    }

}