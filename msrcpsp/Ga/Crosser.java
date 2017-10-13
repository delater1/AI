package msrcpsp.Ga;

import msrcpsp.Generator.RandomGenerator;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Task;
import msrcpsp.validation.BaseValidator;
import msrcpsp.validation.CompleteValidator;


/**
 * Created by korpa on 19.03.2017.
 */
public class Crosser {
    final double CROSSOVER_PROBABILITY;
    RandomGenerator randomGenerator;
    Selector selector;
    BaseValidator validator;


    public Crosser(int tournamentPopulation, double crossoverProbability) {
        CROSSOVER_PROBABILITY = crossoverProbability;
        randomGenerator = RandomGenerator.getInstance();
        selector = new Selector(tournamentPopulation);
        validator = new CompleteValidator();
    }

    public Population cross(Population population) {
        int numOfCrossings = (int) (population.getSize() * CROSSOVER_PROBABILITY);
        for (int i = 0; i < numOfCrossings; i++) {
            preformCrossing(population);
        }
        return population;
    }

    private void preformCrossing(Population population) {
        BaseIndividual[] parents = selector.tournamentSelection(population);
        int tasksSize = parents[0].getSchedule().getTasks().length;
        if (tasksSize != parents[1].getSchedule().getTasks().length) {
            throw new RuntimeException("Error");
        }
        // -2 to avoid cutting in 0 or max
        int cutIndex = randomGenerator.generateRandomInt(tasksSize - 2) + 1;
        mixGenes(parents, cutIndex);
    }

    private void mixGenes(BaseIndividual[] parents, int cutIndex) {
        Task[] firstParentTasks = parents[0].getSchedule().getTasks();
        Task[] secondParentTasks = parents[1].getSchedule().getTasks();
        for (int i = 0; i < firstParentTasks.length; i++) {
            if (i < cutIndex) {
                exchangeTaskResources(firstParentTasks[i], secondParentTasks[i]);
            }
        }
    }

    private void exchangeTaskResources(Task firstTask, Task secondTask) {
        int resId = firstTask.getResourceId();
        firstTask.setResourceId(secondTask.getResourceId());
        secondTask.setResourceId(resId);
    }
}
