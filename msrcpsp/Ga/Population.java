package msrcpsp.Ga;

import msrcpsp.io.PopulationResult;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.scheduling.greedy.Greedy;
import msrcpsp.validation.BaseValidator;

import java.util.Arrays;

/**
 * Created by korpa on 19.03.2017.
 */
public class Population {

    private BaseIndividual[] individuals;

    public Population(BaseIndividual[] individuals) {
        this.individuals = individuals;
    }

    public Population(Population population) {
        individuals = new BaseIndividual[population.getSize()];
        for (int i = 0; i < this.getIndividuals().length; i++) {
            individuals[i] = new BaseIndividual(population.getIndividuals()[i].getSchedule(), population.getIndividuals()[i].getSchedule().getEvaluator());
        }
    }

    public double getAvg() {
        return Arrays.stream(individuals).mapToDouble(BaseIndividual::getDuration).average().orElse(0.0);
    }

    public double getBest() {
        return Arrays.stream(individuals).mapToDouble(BaseIndividual::getDuration).min().orElse(123123);
    }

    public double getWorst() {
        return Arrays.stream(individuals).mapToDouble(BaseIndividual::getDuration).max().orElse(0.0);
    }

    public int getSize() {
        return individuals.length;
    }

    public BaseIndividual[] getIndividuals() {
        return individuals;
    }

    public void setIndividuals(BaseIndividual[] individuals) {
        this.individuals = individuals;
    }

    public void evaluate(Greedy greedy) {
        for (BaseIndividual individual : individuals) {
            greedy.buildTimestamps(individual.getSchedule());
            individual.setDurationAndCost();
        }
    }

    public PopulationResult getPopulationResult() {
        double sum = 0;
        double best = Double.MAX_VALUE;
        double worst = 0;
        for (BaseIndividual individual : individuals) {
            int individualDuration = individual.getDuration();
            sum += individualDuration;
            if (best > individualDuration) {
                best = individualDuration;
            }
            if (worst < individualDuration) {
                worst = individualDuration;
            }
        }
        return new PopulationResult(sum / individuals.length, best, worst);
    }

    public void validate(BaseValidator validator){
        for (int i = 0; i < individuals.length; i++) {
            System.out.println(validator.validate(individuals[i].getSchedule()));
        }
    }
}
