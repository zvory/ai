package rip.az;

import java.util.Random;

public class AIUtil {
    private static final long SEED = 0;
    private static final Random random = new Random(SEED);


    public static int randInRange(int max) {
        return random.nextInt(max);
    }
}
