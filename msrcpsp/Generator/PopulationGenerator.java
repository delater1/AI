package msrcpsp.Generator;

import msrcpsp.Ga.Population;
import msrcpsp.scheduling.BaseIndividual;
import msrcpsp.validation.CompleteValidator;
import msrcpsp.validation.ValidationResult;

/**
 * Created by korpa on 19.03.2017.
 */
public class PopulationGenerator {
    private IndividualGenerator individualGenerator;
    private CompleteValidator completeValidator;

    public PopulationGenerator(IndividualGenerator individualGenerator){
        this.individualGenerator = individualGenerator;
        completeValidator = new CompleteValidator();
    }

    public Population generateRandomPopulation(int size){
        BaseIndividual[] population = new BaseIndividual[size];
        for (int i = 0; i < size; i++) {
            population[i] = individualGenerator.generateRandomIndividual();
            if (completeValidator.validate(population[i].getSchedule()) == ValidationResult.FAILURE) {
                System.out.println("2.ERROR");
            } else {
                System.out.println("2.SUCCES");
            }
        }
        return new Population(population);
    }

}
