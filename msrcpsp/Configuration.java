package msrcpsp;

public class Configuration {
  public static String definitionsFile = "assets/def/";
  public static String name = "200_40_133_15.def";
  public static String writeFile = "assets/results/r";
  public static int RUNS = 10;
  public static int POPULATION_SIZE = 100;
  public static int NUMBER_OF_POPULATIONS = 500;
  public static int TOURNAMENT_SIZE = 10;
  public static double MUTATION_PROBABILITY = 0.01;
  public static double CROSSOVER_PROBABILITY = 0.2;

    public static String getDefinitionFileName() {
        return definitionsFile + name;
    }
}
