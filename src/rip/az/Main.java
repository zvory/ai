package rip.az;

import rip.az.Strategy.MinimaxStrategy;
import rip.az.Strategy.MinimaxV2Strategy;

public class Main {
    public static void main(String[] args) {
        int testCases = 100;
        int numThreads = Runtime.getRuntime().availableProcessors();
//        int numThreads = 1;

        GameOrchestrator runner = new GameOrchestrator(
                new MinimaxStrategy(),
                new MinimaxV2Strategy(),
                testCases,
                numThreads
        );
        var results = runner.run();

        // one: minimax. two: minimaxv2
        // 6 ties, 3 one win, 1 two win

        // one: minimax. two: minimax
        // 6 one win, 4 two win

        // one: minimax. two: minimax
        // Done{ONE=62, TWO=32, NONE=6}
        System.out.println("Done" + results);
    }
}
