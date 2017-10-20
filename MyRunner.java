import msrcpsp.Ga.GARunner;

import java.util.logging.Logger;

/**
 * Created by korpa on 19.03.2017.
 */
public class MyRunner {
    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    public static void main(String[] args) {
        GARunner gaRunner = new GARunner();
        gaRunner.run();
    }


}
