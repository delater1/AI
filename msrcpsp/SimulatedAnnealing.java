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
    Schedule schedule = reader.readDefinition(Configuration.getDefinitionFileName());
    BaseEvaluator evaluator = new DurationEvaluator(schedule);
    IndividualGenerator individualGenerator = new IndividualGenerator(schedule, evaluator);
    Greedy greedy = new Greedy(schedule.getSuccesors());
    RandomGenerator randomGenerator = RandomGenerator.getInstance();


    public static void main(String[] args) {
        SimulatedAnnealing simulatedAnnealing = new SimulatedAnnealing();
        List<Result> results = simulatedAnnealing.run();
        CsvWriter csvWriter = new CsvWriter();
        csvWriter.write(SimulatedAnnealingConfig.getFilePath(), SimulatedAnnealingConfig.getDescritionString(), results);
    }

    public List<Result> run() {
        BaseIndividual bestBest = null;
        List<Result> results = new ArrayList<>();
        BaseIndividual vc = individualGenerator.generateRandomIndividual();
        greedy.buildTimestamps(vc.getSchedule());
        vc.setNormalDurationAndCost();
        bestBest = vc;
        for (int i = SimulatedAnnealingConfig.T; i > 0; i--) {
            for (int j = 0; j < SimulatedAnnealingConfig.innerLoop; j++) {
                List<BaseIndividual> neighbours = generateRandomNeighbours(vc, SimulatedAnnealingConfig.K);
                BaseIndividual vn = findRandom(neighbours);
                if (vn != null && vn.getEvalValue() < vc.getEvalValue()) {
                    vc = vn;
                } else if (vc.getEvalValue() != vn.getEvalValue() && randomGenerator.generateRandomDouble() < getAnnealing(vc.getEvalValue(), i, vn.getEvalValue())) {
                    vc = vn;
                }
                if(bestBest.getEvalValue() > vc.getEvalValue()){
                    bestBest=vc;
                }
                results.add(new Result(bestBest.getEvalValue(), vn.getEvalValue()));
//                System.out.println("bestIn neighbouts:  " + vn.getEvalValue() + " best: " + vc.getEvalValue());
            }
        }
        return results;
    }

    private BaseIndividual findRandom(List<BaseIndividual> neighbours) {
        return neighbours.get(randomGenerator.generateRandomInt(neighbours.size()));
    }

    private double getAnnealing(double vcEval, int i, double vnEval) {
        double res = Math.exp(((vcEval - vnEval)*10000 / ((double) i)));
        System.out.println(res);
        return res;
    }


    private BaseIndividual findBest(List<BaseIndividual> neighbours) {
        BaseIndividual baseIndividual = neighbours.stream().min((o1, o2) -> {
            if (o1.getEvalValue() < o2.getEvalValue())
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
            baseIndividual.setNormalDurationAndCost();
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
