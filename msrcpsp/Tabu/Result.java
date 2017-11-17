package msrcpsp.Tabu;

public class Result {
    double best;
    double current;

    public Result(double iteration, double duration) {
        this.best = duration;
        this.current = iteration;
    }

    public double getBest() {
        return best;
    }

    public double getCurrent() {
        return current;
    }
}
