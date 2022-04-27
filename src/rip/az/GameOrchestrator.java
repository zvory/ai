package rip.az;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import rip.az.ConnectFour.ConnectFourBoard;
import rip.az.Strategy.Strategy;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameOrchestrator {
    private final ExecutorService executor;
    private Strategy firstStrategy;
    private Strategy secondStrategy;
    private int testCases;
    private int numThreads;


    public GameOrchestrator(Strategy firstStrategy, Strategy secondStrategy, int testCases, int numThreads) {
        this.firstStrategy = firstStrategy;
        this.secondStrategy = secondStrategy;
        this.testCases = testCases;
        this.numThreads = numThreads;
        executor = Executors.newFixedThreadPool(numThreads);
    }

    private void updateProgressBar(ProgressBar pb, ConcurrentHashMap results) {
        pb.step();

        String message = " [" + firstStrategy.getClass().getSimpleName() + " wins : " + results.get(Player.ONE) + ". " +
                secondStrategy.getClass().getSimpleName() + " wins: " + results.get(Player.TWO) + ". " +
                "Ties: " + results.get(Player.NONE) + "]";

        pb.setExtraMessage(message);
    }

    private void addResult(ConcurrentHashMap<Player, Integer> results, Player winner) {
        if (results.get(winner) == null) {
            results.put(winner, 1);
        } else {
            results.put(winner, results.get(winner) + 1);
        }
    }


    public ConcurrentHashMap<Player, Integer> run() {
        ConcurrentHashMap<Player, Integer> results = new ConcurrentHashMap<>();

        try (ProgressBar pb = new ProgressBarBuilder().setMaxRenderedLength(150).setTaskName("Running " + testCases + " games in " + numThreads + " threads.").setInitialMax(testCases).showSpeed().build()) {
            if (numThreads == 1) {
                for (int i = 0; i < testCases; i++) {
                    addResult(results, runGame());
                    updateProgressBar(pb, results);
                }
            } else {
                for (int i = 0; i < testCases; i++) {
                    executor.execute(() -> {
                        addResult(results, runGame());
                        updateProgressBar(pb, results);
                    });
                }
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return results;// TODO don't return inner class
    }

    private Player runGame() {
        ConnectFourBoard board = new ConnectFourBoard();
        Game game = new Game(board, firstStrategy, secondStrategy);
        Player winner = game.runGame();
        return winner;
    }
}
