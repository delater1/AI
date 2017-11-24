package msrcpsp;

import msrcpsp.Generator.IndividualGenerator;
import msrcpsp.Generator.RandomGenerator;
import msrcpsp.Tabu.Result;
import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.io.CsvWriter;
import msrcpsp.io.MSRCPSPIO;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.Resource;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.greedy.Greedy;

import java.util.ArrayList;
import java.util.List;

public class SimulatedAnnealing {
    MSRCPSPIO reader = new MSRCPSPIO();
    Schedule schedule = reader.readDefinition(SimulatedAnnealingConfig.getDefinitionFileName());
    BaseEvaluator evaluator = new DurationEvaluator(schedule);
    IndividualGenerator individualGenerator = new IndividualGenerator(schedule, evaluator);
    Greedy greedy = new Greedy(schedule.getSuccesors());
    RandomGenerator randomGenerator = RandomGenerator.getInstance();


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("Run --" + i);

            SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
            List<Result> results = simulatedAnnealing.run();
            CsvWriter csvWriter = new CsvWriter();
            csvWriter.write(SimulatedAnnealingConfig.getFilePath(), SimulatedAnnealingConfig.getDescritionString(), results);
        }
    }

    public List<Result> run() {
        BaseIndividual bestBest = null;
        List<Result> results = new ArrayList<>();
        BaseIndividual vc = individualGenerator.generateRandomIndividual();
        greedy.buildTimestamps(vc.getSchedule());
        vc.setDurationAndCost();
        bestBest = vc;
        for (int i = SimulatedAnnealingConfig.T; i > 0; i-=5) {
            for (int j = 0; j < SimulatedAnnealingConfig.innerLoop; j++) {
                List<BaseIndividual> neighbours = generateRandomNeighbours(vc, SimulatedAnnealingConfig.K);
                BaseIndividual vn = findRandom(neighbours);
                if (vn != null && vn.getDuration() <= vc.getDuration()) {
                    vc = vn;
                } else if (vc.getDuration() != vn.getDuration() && randomGenerator.generateRandomDouble() < getAnnealing(vc.getDuration(), i, vn.getDuration())) {
                    vc = vn;
                }
                results.add(new Result(bestBest.getDuration(), vc.getDuration()));
                if(bestBest.getDuration() > vc.getDuration()){
                    bestBest=vc;
                }
//                System.out.println("bestIn neighbouts:  " + vn.getDuration()() + " best: " + vc.getDuration()());
            }
        }
        System.out.println("best : " + bestBest.getDuration());
        return results;
    }

    private BaseIndividual findRandom(List<BaseIndividual> neighbours) {
        return neighbours.get(randomGenerator.generateRandomInt(neighbours.size()));
    }

    private double getAnnealing(double vcEval, int i, double vnEval) {
        double res = Math.exp(((vcEval - vnEval) *100/ ((double) i)));
//        System.out.println(res);
        return res;
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
        while (capableResources.isEmpty()) {
            taskNumber = randomGenerator.generateRandomInt(baseIndividual.getSchedule().getTasks().length);
            capableResources = baseIndividual.getSchedule().getCapableResources(baseIndividual.getSchedule().getTasks()[taskNumber]);
        }
        baseIndividual.getSchedule().assign(baseIndividual.getSchedule().getTasks()[taskNumber], capableResources.get(randomGenerator.generateRandomInt(capableResources.size())));
    }
}
