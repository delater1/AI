package msrcpsp;

public class TabuConfig {
    public static int T = 10000;
    public static int K = 100;
    public static String definitionsFile = "assets/def/";
    public static String name = "200_40_133_15.def";

    public static String getDefinitionFileName() {
        return definitionsFile + name;
    }
}
