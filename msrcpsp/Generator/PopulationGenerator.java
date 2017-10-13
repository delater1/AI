package msrcpsp.Generator;

import msrcpsp.Ga.Population;
import msrcpsp.scheduling.BaseIndividual;

/**
 * Created by korpa on 19.03.2017.
 */
public class PopulationGenerator {
    private IndividualGenerator individualGenerator;

    public PopulationGenerator(IndividualGenerator individualGenerator){
        this.individualGenerator = individualGenerator;
    }

    public Population generateRandomPopulation(int size){
        BaseIndividual[] population = new BaseIndividual[size];
        for (int i = 0; i < size; i++) {
            population[i] = individualGenerator.generateRandomIndividual();
        }
        return new Population(population);
    }

}
