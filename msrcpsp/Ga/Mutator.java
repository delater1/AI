package msrcpsp.Ga;

import msrcpsp.Generator.RandomGenerator;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Task;

import java.util.List;

/**
 * Created by korpa on 19.03.2017.
 */
public class Mutator {
    private final double MUTATION_PROBABILITY;
    private RandomGenerator randomGenerator;

    public Mutator(double mutationProbability) {
        MUTATION_PROBABILITY = mutationProbability;
        randomGenerator = RandomGenerator.getInstance();
    }

    public Population mutatePopulation(Population population) {
        int populationSize = population.getSize();
        // To avoid additional for loop through all of elements I assume that there always will be constant number of mutations
        for (int i = 0; i < populationSize; i++) {
            mutateIndividual(population.getIndividuals()[i]);
        }
        return population;
    }

    public void mutateIndividual(BaseIndividual baseIndividual) {
        Task[] tasks = baseIndividual.getSchedule().getTasks();
        int taskCount = tasks.length;
        for (int i = 0; i < taskCount; i++) {
            double roll = randomGenerator.generateRandomDouble();
            if (roll < MUTATION_PROBABILITY) {
                int chosenTaskIndex = randomGenerator.generateRandomInt(taskCount);
                List<Resource> capableResources = baseIndividual.getSchedule().getCapableResources(tasks[chosenTaskIndex]);
                int capableResourcesCount = capableResources.size();

                if (capableResources.size() > 1) {
                    int alternativeResId = capableResources.get(randomGenerator.generateRandomInt(capableResourcesCount)).getId();
                    while (alternativeResId == tasks[i].getResourceId()) {
                        alternativeResId = capableResources.get(randomGenerator.generateRandomInt(capableResourcesCount)).getId();
                    }
                    baseIndividual.getSchedule().assign(tasks[chosenTaskIndex],
                            capableResources.get(randomGenerator.generateRandomInt(capableResourcesCount)));
                }
            }
        }
    }


}
