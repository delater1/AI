package msrcpsp.Tabu;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TabuConfig {
    public static int T = 10000;
    public static int K = 200;
    public static String definitionsFile = "assets/def/";
    public static String name = "200_40_133_15.def";
    public static int sinkingListSize = 20;
    public static String writeFile = "assets/results/tabu";

    public static String getDefinitionFileName() {
        return definitionsFile + name;
    }

    public static String getDescritionString() {
        return "plik: " + name + "; liczba iteracji: " + T + ";" + " liczba sasiadow" + K + ";" + " pojemnosc kolejki: " + sinkingListSize;
    }

    public static String getFilePath() {
        return writeFile + (new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date(System.currentTimeMillis()))) + ".csv";
    }
}