package msrcpsp.io;


import msrcpsp.Ga.Population;

/**
 * Created by korpa on 23.03.2017.
 */
public class NTimesRunAvgValues {
    private RunResults[] runResults;
    private PopulationResult[] xRunsAvgValues;
    private PopulationResult totalAvg;

    public NTimesRunAvgValues(RunResults[] runResults) {
        this.runResults = runResults;
        this.xRunsAvgValues = new PopulationResult[runResults[0].populationResults.length];
        this.totalAvg = new PopulationResult(0,0,0);
    }

    public void setnRunsAvgValues() {
        for (int j = 0; j < runResults[0].populationResults.length; j++) {
            double sumAvg = 0;
            double sumBest = 0;
            double sumWorst = 0;
            for (int i = 0; i < runResults.length; i++) {
                sumAvg += runResults[i].populationResults[j].avg;
                sumBest += runResults[i].populationResults[j].best;
                sumWorst += runResults[i].populationResults[j].worst;
            }
            xRunsAvgValues[j] = new PopulationResult(sumAvg / (runResults.length), sumBest / (runResults.length), sumWorst / (runResults.length));
            totalAvg.avg+=xRunsAvgValues[j].avg;
            totalAvg.best+=xRunsAvgValues[j].best;
            totalAvg.worst+=xRunsAvgValues[j].worst;
        }
        totalAvg.avg/=runResults[0].populationResults.length;
        totalAvg.best/=runResults[0].populationResults.length;
        totalAvg.worst/=runResults[0].populationResults.length;
    }

    public PopulationResult[] getxRunsAvgValues() {
        return xRunsAvgValues;
    }

    public void setxRunsAvgValues(PopulationResult[] xRunsAvgValues) {
        this.xRunsAvgValues = xRunsAvgValues;
    }

    public PopulationResult getTotalAvg() {
        return totalAvg;
    }
}
