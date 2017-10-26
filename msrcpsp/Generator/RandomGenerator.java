package msrcpsp.Generator;

import java.util.Random;

/**
 * Created by korpa on 20.03.2017.
 */
public class RandomGenerator {
    private static RandomGenerator instance;
    private Random random;

    private RandomGenerator(){
        random = new Random(System.currentTimeMillis());
    }

    public static RandomGenerator getInstance(){
        if(instance == null)
            return new RandomGenerator();
        return instance;
    }

    public int generateRandomInt(int bound){
        return (int) (random.nextDouble() * bound);
    }


    public double generateRandomDouble() {
        return random.nextDouble();
    }
}
