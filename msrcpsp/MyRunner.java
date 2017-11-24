package msrcpsp;

import com.sun.org.apache.bcel.internal.generic.POP;
import msrcpsp.Ga.Crosser;
import msrcpsp.Ga.Mutator;
import msrcpsp.Ga.Population;
import msrcpsp.Generator.IndividualGenerator;
import msrcpsp.Generator.PopulationGenerator;
import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.io.*;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.greedy.Greedy;
import msrcpsp.validation.BaseValidator;
import msrcpsp.validation.CompleteValidator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by korpa on 19.03.2017.
 */
public class MyRunner {
    private static final Logger LOGGER = Logger.getLogger(Runner.class.getName());

    public static void main(String[] args) {
        RunResults[] testResults = new RunResults[Configuration.RUNS];
        MSRCPSPIO reader = new MSRCPSPIO();
        Schedule schedule = reader.readDefinition(Configuration.getDefinitionFileName());
        if (null == schedule) {
            LOGGER.log(Level.WARNING, "Could not read the Definition " + Configuration.getDefinitionFileName());
        }

        Greedy greedy = new Greedy(schedule.getSuccesors());
        BaseEvaluator evaluator = new DurationEvaluator(schedule);
        PopulationGenerator populationGenerator = new PopulationGenerator(new IndividualGenerator(schedule, evaluator));
        Crosser crosser = new Crosser(Configuration.TOURNAMENT_SIZE, Configuration.CROSSOVER_PROBABILITY);
        Mutator mutator = new Mutator(Configuration.MUTATION_PROBABILITY);
        BaseValidator validator = new CompleteValidator();


        for (int j = 0; j < testResults.length; j++) {
            System.out.println("RUN " + j + " ===================================================================");
//            Population[] populations = new Population[Configuration.NUMBER_OF_POPULATIONS];
            Population population1 = populationGenerator.generateRandomPopulation(Configuration.POPULATION_SIZE);
            population1.evaluate(greedy);
            population1.validate(validator);
            for (int i = 1; i < Configuration.NUMBER_OF_POPULATIONS; i++) {
                Population newPopulation = new Population(population1, greedy);
                newPopulation.validate(validator);
                crosser.cross(newPopulation);
                mutator.mutatePopulation(newPopulation);
                newPopulation.evaluate(greedy);
                if (i == (Configuration.NUMBER_OF_POPULATIONS -1))
                    System.out.println("Population " + i + " ----Avg: " + newPopulation.getAvg() + " Best: " + newPopulation.getBest() + " Worst: " + newPopulation.getWorst());
                population1 = newPopulation;
            }
//            testResults[j] = new RunResults(populations);
        }

        NTimesRunAvgValues nTimesRunAvgValues = new NTimesRunAvgValues(testResults);
        nTimesRunAvgValues.setnRunsAvgValues();
        // save to a file
        CsvWriter csvWriter = new CsvWriter();
        try {
            csvWriter.writePopulationValues(nTimesRunAvgValues, getBestResult(testResults), getDate());
        } catch (IOException e) {
            System.out.print("Writing to a file failed");
        }
    }

    private static String getDate() {
        return Configuration.writeFile + (new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".csv";
    }

    private static double getBestResult(RunResults[] testResults) {
        double min = Double.MAX_VALUE;
        for (RunResults testResult : testResults) {
            for (PopulationResult populationResult : testResult.populationResults) {
                if (min > populationResult.best) {
                    min = populationResult.best;
                }
            }
        }
        return min;
    }


}
