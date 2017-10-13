public class Coniguration {
    static String definitionsFile = "assets/def/";
    static String name = "200_40_133_15.def";
    static String writeFile = "assets/results/r";
    static int RUNS = 10;
    static int POPULATION_SIZE = 100;
    static int NUMBER_OF_POPULATIONS = 500;
    static int TOURNAMENT_SIZE = 10;
    static double MUTATION_PROBABILITY = 0.5;
    static double CROSSOVER_PROBABILITY = 0.1;

    public static String getDefinitionFileName() {
        return definitionsFile + name;
    }
}
