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
    @Fork(value = 1, warmups = 0)
    @Warmup(iterations = 0)
    public void measureFastGetWinner(Blackhole blackhole) {
        final int testCases = 50000;
        int firstWinCount = 0;
        Random rand = new Random();
        for (int test = 0; test < testCases; test++) {
            ConnectFourBoard board = new ConnectFourBoard();
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
    @Fork(value = 1, warmups = 0)
    @Warmup(iterations = 0)
    public void measureGetWinner(Blackhole blackhole) {
        final int testCases = 50000;
        int firstWinCount = 0;
        Random rand = new Random();
        for (int test = 0; test < testCases; test++) {
            ConnectFourBoard board = new ConnectFourBoard();
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
