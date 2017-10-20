package msrcpsp.Ga;

import msrcpsp.Configuration;
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
import msrcpsp.validation.BaseValidator;
import msrcpsp.validation.CompleteValidator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GARunner {
    private static final Logger LOGGER = Logger.getLogger(GARunner.class.getName());


    public static void run() {
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
            Population[] populations = new Population[Configuration.NUMBER_OF_POPULATIONS];
            populations[0] = populationGenerator.generateRandomPopulation(Configuration.POPULATION_SIZE);
            populations[0].evaluate(greedy);
//            populations[0].validate(validator);
            for (int i = 1; i < populations.length; i++) {
                populations[i] = new Population(populations[i - 1]);
                crosser.cross(populations[i]);
                mutator.mutatePopulation(populations[i]);
                populations[i].evaluate(greedy);
                System.out.println("Population " + i + " ----Avg: " + populations[i].getAvg() + " Best: " + populations[i].getBest() + " Worst: " + populations[i].getWorst());
            }
            testResults[j] = new RunResults(populations);
        }

        NTimesRunAvgValues nTimesRunAvgValues = new NTimesRunAvgValues(testResults);
        nTimesRunAvgValues.setnRunsAvgValues();
        // save to a file
        CsvWriter csvWriter = new CsvWriter();
        try {
            csvWriter.writePopulationValues(nTimesRunAvgValues, Configuration.writeFile + (new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".csv");
        } catch (IOException e) {
            System.out.print("Writing to a file failed");
        }
    }
}
