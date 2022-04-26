package rip.az;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import rip.az.ConnectFour.ConnectFourBoard;
import rip.az.ConnectFour.ConnectFourMove;

import java.util.List;
import java.util.Random;

public class Benchmarks {

    @org.openjdk.jmh.annotations.Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    public void measureFastGetWinner(Blackhole blackhole) {
        ConnectFourBoard board = new ConnectFourBoard();
        final int testCases = 100;
        final int seed = 0;
        int firstWinCount = 0;
        Random rand = new Random(0);
        for (int test = 0; test < testCases; test++) {
            board = new ConnectFourBoard();
            while (!board.noMoreMovesPossible()) {
                List<ConnectFourMove> moves = board.getPossibleMoves();
                ConnectFourMove move = moves.get(rand.nextInt(moves.size()));
                board.applyMove(move);
                Player winner = board.getIsLatestMoveWinning();
                if (winner == Player.ONE) firstWinCount++;

            }
        }
        blackhole.consume(firstWinCount);
    }

    @org.openjdk.jmh.annotations.Benchmark
    @BenchmarkMode(Mode.Throughput)
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 1)
    public void measureGetWinner(Blackhole blackhole) {
        ConnectFourBoard board = new ConnectFourBoard();
        final int testCases = 100;
        final int seed = 0;
        int firstWinCount = 0;
        Random rand = new Random(0);
        for (int test = 0; test < testCases; test++) {
            board = new ConnectFourBoard();
            while (!board.noMoreMovesPossible()) {
                List<ConnectFourMove> moves = board.getPossibleMoves();
                ConnectFourMove move = moves.get(rand.nextInt(moves.size()));
                board.applyMove(move);
                Player winner = board.getWinner();
                if (winner == Player.ONE) firstWinCount++;
            }
        }
        blackhole.consume(firstWinCount);
    }

}
