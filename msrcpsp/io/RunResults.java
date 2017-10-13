package msrcpsp.io;

import msrcpsp.Ga.Population;

/**
 * Created by korpa on 23.03.2017.
 */
public class RunResults {
    public PopulationResult[] populationResults;

    public RunResults(Population[] populations) {
        populationResults = new PopulationResult[populations.length];
        for (int i = 0; i < populations.length; i++) {
            populationResults[i] = populations[i].getPopulationResult();
        }
    }
}
