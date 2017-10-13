package msrcpsp.Generator;

import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.Task;
import msrcpsp.scheduling.greedy.Greedy;
import msrcpsp.validation.BaseValidator;
import msrcpsp.validation.CompleteValidator;

import java.util.List;
import java.util.Random;

/**
 * Created by korpa on 19.03.2017.
 */
public class IndividualGenerator {
    private final Schedule schedule;
    private BaseEvaluator evaluator;
    private Random random;

    public IndividualGenerator(Schedule schedule, BaseEvaluator evaluator) {
        this.schedule = schedule;
        this.evaluator = evaluator;
        random = new Random(System.currentTimeMillis());
    }

    public BaseIndividual generateRandomIndividual() {
        BaseIndividual individual = new BaseIndividual(schedule, evaluator);
        Schedule invidualSchedule = individual.getSchedule();
        Task[] tasks = invidualSchedule.getTasks();
        List<Resource> resources;
        for(Task aTask : tasks){
            resources = schedule.getCapableResources(aTask);
            schedule.assign(aTask, resources.get((int) (random.nextDouble() * resources.size())));
        }
        return individual;
    }
}
