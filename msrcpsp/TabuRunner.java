package msrcpsp;

import msrcpsp.Generator.IndividualGenerator;
import msrcpsp.Generator.RandomGenerator;
import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.io.MSRCPSPIO;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.greedy.Greedy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TabuRunner {
    MSRCPSPIO reader = new MSRCPSPIO();
    Schedule schedule = reader.readDefinition(Configuration.getDefinitionFileName());
    BaseEvaluator evaluator = new DurationEvaluator(schedule);
    IndividualGenerator individualGenerator = new IndividualGenerator(schedule, evaluator);
    Greedy greedy = new Greedy(schedule.getSuccesors());
    RandomGenerator randomGenerator = RandomGenerator.getInstance();


    public static void main(String[] args) {
        TabuRunner tabuRunner = new TabuRunner();
        tabuRunner.run();
    }

    public void run() {
        HashSet<BaseIndividual> tabu = new HashSet<>();
        BaseIndividual vc = individualGenerator.generateRandomIndividual();
        greedy.buildTimestamps(vc.getSchedule());
        vc.setDurationAndCost();
        tabu.add(vc);
        for (int i = 0; i < TabuConfig.T; i++) {
            List<BaseIndividual> neighbours = generateRandomNeighbours(vc, TabuConfig.K);
            removeTabuFromNeighours(neighbours, tabu);
            BaseIndividual bestInNeighbours = findBest(neighbours);
            if (bestInNeighbours != null && bestInNeighbours.getDuration() <= vc.getDuration()) {
                vc = bestInNeighbours;
            }
            System.out.println(" duration: " + vc.getDuration());
            tabu.add(bestInNeighbours);
        }
    }


    private void removeTabuFromNeighours(List<BaseIndividual> neighbours, HashSet<BaseIndividual> tabu) {
        tabu.forEach(x -> neighbours.removeIf(baseIndividual -> baseIndividual.equals(x)));
    }


    private BaseIndividual findBest(List<BaseIndividual> neighbours) {
        BaseIndividual baseIndividual = neighbours.stream().min((o1, o2) -> {
            if (o1.getDuration() < o2.getDuration())
                return -1;
            return 1;
        }).orElse(null);
        return baseIndividual;
    }

    private List<BaseIndividual> generateRandomNeighbours(BaseIndividual base, final int K) {
        List<BaseIndividual> neighbours = new ArrayList<>();
        for (int i = 0; i < K; i++) {
            BaseIndividual baseIndividual = new BaseIndividual(base.getSchedule(), base.getSchedule().getEvaluator());
            mutate(baseIndividual);
            greedy.buildTimestamps(baseIndividual.getSchedule());
            baseIndividual.setDurationAndCost();
            neighbours.add(baseIndividual);
        }
        return neighbours;
    }

    private void mutate(BaseIndividual baseIndividual) {
        int taskNumber = randomGenerator.generateRandomInt(baseIndividual.getSchedule().getTasks().length);
        List<Resource> capableResources = baseIndividual.getSchedule().getCapableResources(baseIndividual.getSchedule().getTasks()[taskNumber]);
        if (!capableResources.isEmpty()) {
            baseIndividual.getSchedule().assign(baseIndividual.getSchedule().getTasks()[taskNumber],
                    capableResources.get(randomGenerator.generateRandomInt(capableResources.size())));
        }
    }
}
