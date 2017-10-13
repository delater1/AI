package msrcpsp.Ga;

import msrcpsp.Generator.RandomGenerator;
import msrcpsp.scheduling.BaseIndividual;

/**
 * Created by korpa on 19.03.2017.
 */
public class Selector {
    private final int tournamentSize;
    private RandomGenerator randomGenerator;

    Selector(int tournamentSize) {
        this.tournamentSize = tournamentSize;
        randomGenerator = RandomGenerator.getInstance();
    }

    public BaseIndividual[] tournamentSelection(Population population) {
        BaseIndividual[] selectedParents = new BaseIndividual[2];
        int[] worstIndividualIndexes = getWorstIndividualsIndexes(population);
        for (int i = 0; i < selectedParents.length; i++) {
            selectedParents[i] = getBestIndividual(selectTournamentPlayers(population));
            population.getIndividuals()[worstIndividualIndexes[i]] = new BaseIndividual(selectedParents[i]);
            population.getIndividuals()[worstIndividualIndexes[i]].setDurationAndCost();
        }
        return selectedParents;
    }

    private BaseIndividual[] selectTournamentPlayers(Population population) {
        BaseIndividual[] tournamentPlayers = new BaseIndividual[tournamentSize];
        int populationSize = population.getSize();
        for (int i = 0; i < tournamentSize; i++) {
            tournamentPlayers[i] = population.getIndividuals()[randomGenerator.generateRandomInt(populationSize)];
        }
        return tournamentPlayers;
    }

    private BaseIndividual getBestIndividual(BaseIndividual[] tournamentPlayers) {
        int bestValue = tournamentPlayers[0].getDuration();
        int bestIndex = 0;
        for (int i = 1; i < tournamentPlayers.length; i++) {
            if (tournamentPlayers[i].getDuration() < bestValue) {
                bestIndex = i;
                bestValue = tournamentPlayers[i].getDuration();
            }
        }
        return tournamentPlayers[bestIndex];
    }

    private int[] getWorstIndividualsIndexes(Population population){
        int[] worstIndividualsIndexes = new int[2];
        worstIndividualsIndexes[0] = 0;
        worstIndividualsIndexes[1] = 0;
        int worstVal = 0;
        for (int i = 0; i < population.getSize(); i++) {
            if(population.getIndividuals()[i].getDuration() > worstVal){
                worstIndividualsIndexes[1] = worstIndividualsIndexes[0];
                worstIndividualsIndexes[0] = i;
                worstVal = population.getIndividuals()[i].getDuration();
            }
        }
        return worstIndividualsIndexes;
    }
}
