package msrcpsp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SimulatedAnnealingConfig {
    public static int T = 500;
    public static int K = 10;
    public static String definitionsFile = "assets/def/";
    public static String name = "200_40_133_15.def";
    public static String writeFile = "assets/results/simulatedAnnealing";
    public static int innerLoop = 100;

    public static String getDefinitionFileName() {
        return definitionsFile + name;
    }

    public static String getDescritionString() {
        return "plik: " + name + "; liczba iteracji: " + T + ";" + " liczba sasiadow" + K;
    }

    public static String getFilePath() {
        return writeFile + (new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".csv";
    }
}
