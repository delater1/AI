package msrcpsp.io;

import msrcpsp.Configuration;
import msrcpsp.Ga.Population;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by korpa on 19.03.2017.
 */
public class CsvWriter {

    public void writePopulationFeatures(Population[] population, String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        Arrays.stream(population).map(this::getPopulationFeaturesString).map(s -> s + "\n").forEach(writer::write);
        writer.close();
    }

    private String getPopulationFeaturesString(Population population) {
        return (population.getAvg() + ";" + population.getBest() + ";" + population.getWorst()).replace('.', ',');
    }

    private String getHeader() {
        return "Srednia" + ";" + "Najlepsza" + ";" + "Najgorsza" + "\n";
    }

    public void writePopulationValues(NTimesRunAvgValues nTimesRunAvgValues, double best, String filename) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(filename));
        writer.write(getConfigurationDescrition() + "\n");
        writer.write("Totalna średnia ;" + nTimesRunAvgValues.getTotalAvg().avg + ";" + nTimesRunAvgValues.getTotalAvg().best + ";" + nTimesRunAvgValues.getTotalAvg().worst + "\n");
        writer.write("Najlepszy wynik: " + best + "\n");
        Arrays.stream(nTimesRunAvgValues.getxRunsAvgValues()).map(this::getPopulationResultValuesString).map(s -> s + "\n").forEach(writer::write);
        writer.close();
    }

    private String getPopulationResultValuesString(PopulationResult populationResult) {
        return (String.format("%.2f", populationResult.avg) + ";" + String.format("%.2f", populationResult.best) + ";" + String.format("%.2f", populationResult.worst)).replace('.', ',');
    }

    private String getConfigurationDescrition() {
        return "plik:" + Configuration.name + ";"
                + "runs:" + Configuration.RUNS + ";"
                + "population size:" + Configuration.POPULATION_SIZE + ";"
                + "populations quantity:" + Configuration.NUMBER_OF_POPULATIONS + ";"
                + "tournament size: " + Configuration.TOURNAMENT_SIZE + ";"
                + "mutation probability: " + Configuration.MUTATION_PROBABILITY + ";"
                + "crossover probability: " + Configuration.CROSSOVER_PROBABILITY;
    }
}
