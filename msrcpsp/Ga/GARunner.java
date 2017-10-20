package msrcpsp.Ga;

import msrcpsp.Configuration;
import msrcpsp.Generator.IndividualGenerator;
import msrcpsp.Generator.PopulationGenerator;
import msrcpsp.evaluation.BaseEvaluator;
import msrcpsp.evaluation.DurationEvaluator;
import msrcpsp.io.*;
import msrcpsp.scheduling.Schedule;
import msrcpsp.scheduling.greedy.Greedy;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GARunner {
    private static final Logger LOGGER = Logger.getLogger(GARunner.class.getName());
    private MSRCPSPIO reader;
    private RunResults[] testResults;
    private PopulationGenerator populationGenerator;
    private Schedule schedule;
    private Crosser crosser;
    private Mutator mutator;
    private BaseEvaluator evaluator;
    private Greedy greedy;


    public GARunner() {
        testResults = new RunResults[Configuration.RUNS];
        reader = new MSRCPSPIO();
        schedule = reader.readDefinition(Configuration.getDefinitionFileName());
        if (null == schedule) {
            LOGGER.log(Level.WARNING, "Could not read the Definition " + Configuration.getDefinitionFileName());
        }
        greedy = new Greedy(schedule.getSuccesors());
        evaluator = new DurationEvaluator(schedule);
        populationGenerator = new PopulationGenerator(new IndividualGenerator(schedule, evaluator));
        crosser = new Crosser(Configuration.TOURNAMENT_SIZE, Configuration.CROSSOVER_PROBABILITY);
        mutator = new Mutator(Configuration.MUTATION_PROBABILITY);
    }


    public void run() {
        for (int j = 0; j < testResults.length; j++) {
//            System.out.println("RUN " + j + " ===================================================================");
            Population[] populations = new Population[Configuration.NUMBER_OF_POPULATIONS];
            populations[0] = populationGenerator.generateRandomPopulation(Configuration.POPULATION_SIZE);
            populations[0].evaluate(greedy);
            for (int i = 1; i < populations.length; i++) {
                populations[i] = new Population(populations[i - 1]);
                crosser.cross(populations[i]);
                mutator.mutatePopulation(populations[i]);
                populations[i].evaluate(greedy);
//                System.out.println("Population " + i + " ----Avg: " + populations[i].getAvg() + " Best: " + populations[i].getBest() + " Worst: " + populations[i].getWorst());
            }
            testResults[j] = new RunResults(populations);
        }
        save();
        System.out.println("DONE !!!");
    }

    private void save() {
        NTimesRunAvgValues nTimesRunAvgValues = new NTimesRunAvgValues(testResults);
        nTimesRunAvgValues.setnRunsAvgValues();
        CsvWriter csvWriter = new CsvWriter();
        try {
            csvWriter.writeValues(nTimesRunAvgValues, getBestResult(testResults), Configuration.writeFile + (new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".csv");
        } catch (IOException e) {
            System.out.print("Writing to a file failed");
        }
    }

    private double getBestResult(RunResults[] testResults) {
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
