package msrcpsp;

import msrcpsp.Ga.Population;
import msrcpsp.Generator.IndividualGenerator;
import msrcpsp.Generator.PopulationGenerator;
import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.io.CsvWriter;
import msrcpsp.io.MSRCPSPIO;
import msrcpsp.io.NTimesRunAvgValues;
import msrcpsp.io.RunResults;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.greedy.Greedy;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by korpa on 24.03.2017.
 */
public class RandomRunner {
//    private static final Logger LOGGER = Logger.getLogger(RandomRunner.class.getName());
//    private static final String definitionFile = "assets/def_small/10_3_5_3.def";
//    private static final String writeFile = "assets/solutions_small/myOut";
//    private static final int POPULATION_SIZE = 100;
//    private static final int NUMBER_OF_POPULATIONS = 100;
//    private static final int TOURNAMENT_SIZE = 5;
//    private static final double MUTATION_PROBABILITY = 0.7;
//    private static final double CROSSOVER_PROBABILITY = 0.3;
//
//
//    public static void main(String[] args) {
//        RunResults[] testResults = new RunResults[10];
//        MSRCPSPIO reader = new MSRCPSPIO();
//        Schedule schedule = reader.readDefinition(definitionFile);
//        if (null == schedule) {
//            LOGGER.log(Level.WARNING, "Could not read the Definition " + definitionFile);
//        }
//
//        Greedy greedy = new Greedy(schedule.getSuccesors());
//        BaseEvaluator evaluator = new DurationEvaluator(schedule);
//        IndividualGenerator individualGenerator = new IndividualGenerator(schedule, evaluator);
//        PopulationGenerator populationGenerator = new PopulationGenerator(individualGenerator);
//
//
//        for (int j = 0; j < testResults.length; j++) {
//            System.out.println("RUN " + j + " ===================================================================");
//            Population[] populations = new Population[NUMBER_OF_POPULATIONS];
//            populations[0] = populationGenerator.generateRandomPopulation();
//            populations[0].evaluate(greedy);
//            for (int i = 1; i < populations.length; i++) {
//                populations[i] = populationGenerator.generateRandomPopulation();
//                populations[i].evaluate(greedy);
//                System.out.println("Population " + i + " ----Avg: " + populations[i].getAvg() + " Best: " + populations[i].getBest() + " Worst: " + populations[i].getWorst());
//            }
//            testResults[j] = new RunResults(populations);
//        }
//
//        NTimesRunAvgValues nTimesRunAvgValues = new NTimesRunAvgValues(testResults);
//        nTimesRunAvgValues.setnRunsAvgValues();
//        // save to a file
//        CsvWriter csvWriter = new CsvWriter();
//        try {
//            csvWriter.writeValues(nTimesRunAvgValues, writeFile + (System.currentTimeMillis() / 1000) + ".csv");
//        } catch (IOException e) {
//            System.out.print("Writing to a file failed");
//        }
//    }
}
