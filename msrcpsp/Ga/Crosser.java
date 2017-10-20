package msrcpsp.Ga;

import msrcpsp.Generator.RandomGenerator;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.Task;
import msrcpsp.scheduling.greedy.Greedy;
import msrcpsp.validation.BaseValidator;
import msrcpsp.validation.CompleteValidator;
import msrcpsp.validation.ValidationResult;

import java.util.List;


/**
 * Created by korpa on 19.03.2017.
 */
class Crosser {
    private final double CROSSOVER_PROBABILITY;
    private RandomGenerator randomGenerator;
    private Selector selector;
    private BaseValidator validator;


    Crosser(int tournamentPopulation, double crossoverProbability) {
        CROSSOVER_PROBABILITY = crossoverProbability;
        randomGenerator = RandomGenerator.getInstance();
        selector = new Selector(tournamentPopulation);
        validator = new CompleteValidator();
    }

    void cross(Population population) {
        int numOfCrossings = (int) (population.getSize() * CROSSOVER_PROBABILITY);
        for (int i = 0; i < numOfCrossings; i++) {
            preformCrossing(population);
        }
    }

    private void preformCrossing(Population population) {
        BaseIndividual[] parents = selector.tournamentSelection(population);
        int tasksSize = parents[0].getSchedule().getTasks().length;
        if (tasksSize != parents[1].getSchedule().getTasks().length) {
            throw new RuntimeException("Error");
        }
        // -2 to avoid cutting in 0 or max
        int cutIndex = randomGenerator.generateRandomInt(tasksSize - 2);
        mixGenes(parents, cutIndex);
    }

    private void mixGenes(BaseIndividual[] parents, int cutIndex) {
        if (validator.validate(parents[0].getSchedule()) == ValidationResult.FAILURE ||
                validator.validate(parents[1].getSchedule()) == ValidationResult.FAILURE) {
            System.out.println("1.ERROR");
        } else {
            System.out.println("1.SUCCES");
        }
        Task[] firstParentTasks = parents[0].getSchedule().getTasks();
        Task[] secondParentTasks = parents[1].getSchedule().getTasks();
        for (int i = 0; i < firstParentTasks.length; i++) {
            if (i < cutIndex) {
                exchangeTaskResources(firstParentTasks[i], secondParentTasks[i]);
            }
            else {
                assignRandomCapableResource(firstParentTasks[i], parents[0]);
                assignRandomCapableResource(secondParentTasks[i], parents[1]);
            }
        }
        if (validator.validate(parents[0].getSchedule()) == ValidationResult.FAILURE ||
                validator.validate(parents[1].getSchedule()) == ValidationResult.FAILURE) {
            System.out.println("ERROR");
        } else {
            System.out.println("SUCCES");
        }
    }

    private void exchangeTaskResources(Task firstTask, Task secondTask) {
        int resId = firstTask.getResourceId();
        firstTask.setResourceId(secondTask.getResourceId());
        secondTask.setResourceId(resId);
    }

    private void assignRandomCapableResource(Task task, BaseIndividual baseIndividual) {
        List<Resource> capableResources = baseIndividual.getSchedule().getCapableResources(task);
        task.setResourceId(capableResources.get(randomGenerator.generateRandomInt(capableResources.size())).getId());
    }
}
