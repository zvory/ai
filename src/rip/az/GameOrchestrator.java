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
    private long firstTimeSpent;
    private long secondTimeSpent;


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

    private void addResult(ConcurrentHashMap<Player, Integer> results, Game.Result result) {
        Player winner = result.winner;
        if (results.get(winner) == null) {
            results.put(winner, 1);
        } else {
            results.put(winner, results.get(winner) + 1);
        }
        firstTimeSpent += result.firstTimeSpent;
        secondTimeSpent += result.secondTimeSpent;
    }


    public ConcurrentHashMap<Player, Integer> run() {
        ConcurrentHashMap<Player, Integer> results = new ConcurrentHashMap<>();
        firstTimeSpent = 0;
        secondTimeSpent = 0;
        System.out.println(firstStrategy + " vs " + secondStrategy);

        try (ProgressBar pb = new ProgressBarBuilder().setMaxRenderedLength(200).setTaskName("Running " + testCases + " games in " + numThreads + " threads.").setInitialMax(testCases).showSpeed().build()) {
            if (numThreads == 1) {
                for (int i = 0; i < testCases; i++) {
                    addResult(results, runGame(i));
                    updateProgressBar(pb, results);
                }
            } else {
                for (int i = 0; i < testCases; i++) {
                    final int finalI = i;
                    executor.execute(() -> {
                        addResult(results, runGame(finalI));
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
        System.out.println("First strategy ran in " + (double) firstTimeSpent / secondTimeSpent + " of the time of the second.");
        return results;// TODO don't return inner class
    }

    private Game.Result runGame(int testCase) {
        ConnectFourBoard board = new ConnectFourBoard();
        Game.Result result;
        if (testCase % 2 == 0) {
            Game game = new Game(board, firstStrategy, secondStrategy);
            result = game.runGame();
        } else {
            Game game = new Game(board, secondStrategy, firstStrategy);
            result = game.runGame();
            var temp = result.firstTimeSpent;
            result.firstTimeSpent = result.secondTimeSpent;
            ;
            result.secondTimeSpent = temp;
        }
        return result;
    }
}
