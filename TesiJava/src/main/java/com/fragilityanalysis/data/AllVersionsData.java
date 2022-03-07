package com.fragilityanalysis.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that stores all the data of all the releases
 */
public class AllVersionsData {

    /**
     * ArrayList that stores single release data
     */
    private final ArrayList<SingleVersionData> singleVersionDataArray;
    /**
     * Map that stores classpath -> number of changes
     */
    private final HashMap<String, Integer> classChanges;
    /**
     * Map that stores statisticName ("avg","variance","std","max","min") -> { metricCode (1-19) -> metric statistic value }
     */
    private final HashMap<String, HashMap<Integer, Double>> completeStatistics;
    /**
     * Stores the value of the Number of Tagged Releases
     */
    private Integer NTR;
    /**
     * Stores the value of the Modified Releases Ratio Metric
     */
    private Double MRR;
    /**
     * Stores the value of the Test Suite Volatility metric
     */
    private Double TSV;
    /**
     * Stores the name pf the repository analyzed
     */
    private String repoName;

    public Integer getNTR() {
        return NTR;
    }

    public void setNTR(Integer NTR) {
        this.NTR = NTR;
    }

    public Double getMRR() {
        return MRR;
    }

    public void setMRR(Double MRR) {
        this.MRR = MRR;
    }

    public Double getTSV() {
        return TSV;
    }

    public void setTSV(Double TSV) {
        this.TSV = TSV;
    }

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public ArrayList<SingleVersionData> getSingleVersionDataArray() {
        return singleVersionDataArray;
    }

    /**
     * Function that calculate the variance of the data
     *
     * @return the map { indexOfMetric (1-19) -> varianceOfTheMetric }
     */
    public HashMap<Integer, Double> calculateVariance() {
        HashMap<Integer, Double> statistics = new HashMap<>();
        HashMap<Integer, Double> sum = initStatisticMap();

        singleVersionDataArray.forEach((x) -> {
            sum.put(Constants.CODE_NTC, sum.get(Constants.CODE_NTC) + Math.pow(x.getNtc() - completeStatistics.get("avg").get(Constants.CODE_NTC), 2));
            sum.put(Constants.CODE_TTL, sum.get(Constants.CODE_TTL) + Math.pow(x.getTtl() - completeStatistics.get("avg").get(Constants.CODE_TTL), 2));
            sum.put(Constants.CODE_TLR, sum.get(Constants.CODE_TLR) + Math.pow(x.getTlr() - completeStatistics.get("avg").get(Constants.CODE_TLR), 2));
            sum.put(Constants.CODE_MTLR, sum.get(Constants.CODE_MTLR) + Math.pow(x.getMtlr() - completeStatistics.get("avg").get(Constants.CODE_MTLR), 2));
            sum.put(Constants.CODE_MRTL, sum.get(Constants.CODE_MRTL) + Math.pow(x.getMrtl() - completeStatistics.get("avg").get(Constants.CODE_MRTL), 2));
            sum.put(Constants.CODE_MCR, sum.get(Constants.CODE_MCR) + Math.pow(x.getMcr() - completeStatistics.get("avg").get(Constants.CODE_MCR), 2));
            sum.put(Constants.CODE_MMR, sum.get(Constants.CODE_MMR) + Math.pow(x.getMmr() - completeStatistics.get("avg").get(Constants.CODE_MMR), 2));
            sum.put(Constants.CODE_MCMMR, sum.get(Constants.CODE_MCMMR) + Math.pow(x.getMcmmr() - completeStatistics.get("avg").get(Constants.CODE_MCMMR), 2));
            sum.put(Constants.CODE_CC, sum.get(Constants.CODE_CC) + Math.pow(x.getCc() - completeStatistics.get("avg").get(Constants.CODE_CC), 2));
            sum.put(Constants.CODE_CODE_SMELLS, sum.get(Constants.CODE_CODE_SMELLS) + Math.pow(x.getCode_smells() - completeStatistics.get("avg").get(Constants.CODE_CODE_SMELLS), 2));
            sum.put(Constants.CODE_TD, sum.get(Constants.CODE_TD) + Math.pow(x.getTd() - completeStatistics.get("avg").get(Constants.CODE_TD), 2));
            sum.put(Constants.CODE_DEBT_RATIO, sum.get(Constants.CODE_DEBT_RATIO) + Math.pow(x.getDebt_ratio() - completeStatistics.get("avg").get(Constants.CODE_DEBT_RATIO), 2));
            sum.put(Constants.CODE_CLOC, sum.get(Constants.CODE_CLOC) + Math.pow(x.getCloc() - completeStatistics.get("avg").get(Constants.CODE_CLOC), 2));
            sum.put(Constants.CODE_LOC, sum.get(Constants.CODE_LOC) + Math.pow(x.getLoc() - completeStatistics.get("avg").get(Constants.CODE_LOC), 2));
            sum.put(Constants.CODE_NOC, sum.get(Constants.CODE_NOC) + Math.pow(x.getNoc() - completeStatistics.get("avg").get(Constants.CODE_NOC), 2));
            sum.put(Constants.CODE_NOF, sum.get(Constants.CODE_NOF) + Math.pow(x.getNof() - completeStatistics.get("avg").get(Constants.CODE_NOF), 2));
            sum.put(Constants.CODE_NOM, sum.get(Constants.CODE_NOM) + Math.pow(x.getNom() - completeStatistics.get("avg").get(Constants.CODE_NOM), 2));
            sum.put(Constants.CODE_STAT, sum.get(Constants.CODE_STAT) + Math.pow(x.getStat() - completeStatistics.get("avg").get(Constants.CODE_STAT), 2));
        });
        sum.forEach((k, v) -> {
            statistics.put(k, v / singleVersionDataArray.size());
        });
        return statistics;
    }

    /**
     * Function that calculate the average of the data
     *
     * @return the map { indexOfMetric (1-19) -> averageOfTheMetric }
     */
    public HashMap<Integer, Double> calculateAverage() {
        HashMap<Integer, Double> statistics = new HashMap<>();
        HashMap<Integer, Double> sum = initStatisticMap();

        singleVersionDataArray.forEach((x) -> {
            sum.put(Constants.CODE_NTC, sum.get(Constants.CODE_NTC) + x.getNtc());
            sum.put(Constants.CODE_TTL, sum.get(Constants.CODE_TTL) + x.getTtl());
            sum.put(Constants.CODE_TLR, sum.get(Constants.CODE_TLR) + x.getTlr());
            sum.put(Constants.CODE_MTLR, sum.get(Constants.CODE_MTLR) + x.getMtlr());
            sum.put(Constants.CODE_MRTL, sum.get(Constants.CODE_MRTL) + x.getMrtl());
            sum.put(Constants.CODE_MCR, sum.get(Constants.CODE_MCR) + x.getMcr());
            sum.put(Constants.CODE_MMR, sum.get(Constants.CODE_MMR) + x.getMmr());
            sum.put(Constants.CODE_MCMMR, sum.get(Constants.CODE_MCMMR) + x.getMcmmr());
            sum.put(Constants.CODE_CC, sum.get(Constants.CODE_CC) + x.getCc());
            sum.put(Constants.CODE_CODE_SMELLS, sum.get(Constants.CODE_CODE_SMELLS) + x.getCode_smells());
            sum.put(Constants.CODE_TD, sum.get(Constants.CODE_TD) + x.getTd());
            sum.put(Constants.CODE_DEBT_RATIO, sum.get(Constants.CODE_DEBT_RATIO) + x.getDebt_ratio());
            sum.put(Constants.CODE_CLOC, sum.get(Constants.CODE_CLOC) + x.getCloc());
            sum.put(Constants.CODE_LOC, sum.get(Constants.CODE_LOC) + x.getLoc());
            sum.put(Constants.CODE_NOC, sum.get(Constants.CODE_NOC) + x.getNoc());
            sum.put(Constants.CODE_NOF, sum.get(Constants.CODE_NOF) + x.getNof());
            sum.put(Constants.CODE_NOM, sum.get(Constants.CODE_NOM) + x.getNom());
            sum.put(Constants.CODE_STAT, sum.get(Constants.CODE_STAT) + x.getStat());
        });
        sum.forEach((k, v) -> statistics.put(k, v / singleVersionDataArray.size()));
        return statistics;
    }

    /**
     * Function that calculate the Standard Deviation of the data
     *
     * @return the map { indexOfMetric (1-19) -> stdOfTheMetric }
     */
    public HashMap<Integer, Double> calculateStandardDeviation(HashMap<Integer, Double> varianceMap) {
        HashMap<Integer, Double> statistics = new HashMap<>();

        varianceMap.forEach((k, v) -> statistics.put(k, Math.sqrt(v)));
        return statistics;
    }

    /**
     * Function that calculate the Maximun of the data
     *
     * @return the map { indexOfMetric (1-19) -> maxOfTheMetric }
     */
    public HashMap<Integer, Double> calculateMax() {
        HashMap<Integer, Double> sum = initStatisticMap();

        singleVersionDataArray.forEach((x) -> {
            if (sum.get(Constants.CODE_NTC) < x.getNtc())
                sum.put(Constants.CODE_NTC, x.getNtc());
            if (sum.get(Constants.CODE_TTL) < x.getTtl())
                sum.put(Constants.CODE_TTL, x.getTtl());
            if (sum.get(Constants.CODE_TLR) < x.getTlr())
                sum.put(Constants.CODE_TLR, x.getTlr());
            if (sum.get(Constants.CODE_MTLR) < x.getMtlr())
                sum.put(Constants.CODE_MTLR, x.getMtlr());
            if (sum.get(Constants.CODE_MRTL) < x.getMrtl())
                sum.put(Constants.CODE_MRTL, +x.getMrtl());
            if (sum.get(Constants.CODE_MCR) < x.getMcr())
                sum.put(Constants.CODE_MCR, +x.getMcr());
            if (sum.get(Constants.CODE_MMR) < x.getMmr())
                sum.put(Constants.CODE_MMR, +x.getMmr());
            if (sum.get(Constants.CODE_MCMMR) < x.getMcmmr())
                sum.put(Constants.CODE_MCMMR, +x.getMcmmr());
            if (sum.get(Constants.CODE_CC) < x.getCc())
                sum.put(Constants.CODE_CC, x.getCc());
            if (sum.get(Constants.CODE_CODE_SMELLS) < x.getCode_smells())
                sum.put(Constants.CODE_CODE_SMELLS, x.getCode_smells());
            if (sum.get(Constants.CODE_TD) < x.getTd())
                sum.put(Constants.CODE_TD, x.getTd());
            if (sum.get(Constants.CODE_DEBT_RATIO) < x.getDebt_ratio())
                sum.put(Constants.CODE_DEBT_RATIO, x.getDebt_ratio());
            if (sum.get(Constants.CODE_CLOC) < x.getCloc())
                sum.put(Constants.CODE_CLOC, x.getCloc());
            if (sum.get(Constants.CODE_LOC) < x.getLoc())
                sum.put(Constants.CODE_LOC, x.getLoc());
            if (sum.get(Constants.CODE_NOC) < x.getNoc())
                sum.put(Constants.CODE_NOC, x.getNoc());
            if (sum.get(Constants.CODE_NOF) < x.getNof())
                sum.put(Constants.CODE_NOF, x.getNof());
            if (sum.get(Constants.CODE_NOM) < x.getNom())
                sum.put(Constants.CODE_NOM, x.getNom());
            if (sum.get(Constants.CODE_STAT) < x.getStat())
                sum.put(Constants.CODE_STAT, x.getStat());
        });
        return sum;
    }

    /**
     * Function that calculate the Maximun of the data
     *
     * @return the map { indexOfMetric (1-19) -> minOfTheMetric }
     */
    public HashMap<Integer, Double> calculateMin() {
        HashMap<Integer, Double> sum = initMinStatisticMap();

        singleVersionDataArray.forEach((x) -> {
            if (sum.get(Constants.CODE_NTC) > x.getNtc())
                sum.put(Constants.CODE_NTC, x.getNtc());
            if (sum.get(Constants.CODE_TTL) > x.getTtl())
                sum.put(Constants.CODE_TTL, x.getTtl());
            if (sum.get(Constants.CODE_TLR) > x.getTlr())
                sum.put(Constants.CODE_TLR, x.getTlr());
            if (sum.get(Constants.CODE_MTLR) > x.getMtlr())
                sum.put(Constants.CODE_MTLR, x.getMtlr());
            if (sum.get(Constants.CODE_MRTL) > x.getMrtl())
                sum.put(Constants.CODE_MRTL, +x.getMrtl());
            if (sum.get(Constants.CODE_MCR) > x.getMcr())
                sum.put(Constants.CODE_MCR, +x.getMcr());
            if (sum.get(Constants.CODE_MMR) > x.getMmr())
                sum.put(Constants.CODE_MMR, +x.getMmr());
            if (sum.get(Constants.CODE_MCMMR) > x.getMcmmr())
                sum.put(Constants.CODE_MCMMR, +x.getMcmmr());
            if (sum.get(Constants.CODE_CC) > x.getCc())
                sum.put(Constants.CODE_CC, x.getCc());
            if (sum.get(Constants.CODE_CODE_SMELLS) > x.getCode_smells())
                sum.put(Constants.CODE_CODE_SMELLS, x.getCode_smells());
            if (sum.get(Constants.CODE_TD) > x.getTd())
                sum.put(Constants.CODE_TD, x.getTd());
            if (sum.get(Constants.CODE_DEBT_RATIO) > x.getDebt_ratio())
                sum.put(Constants.CODE_DEBT_RATIO, x.getDebt_ratio());
            if (sum.get(Constants.CODE_CLOC) > x.getCloc())
                sum.put(Constants.CODE_CLOC, x.getCloc());
            if (sum.get(Constants.CODE_LOC) > x.getLoc())
                sum.put(Constants.CODE_LOC, x.getLoc());
            if (sum.get(Constants.CODE_NOC) > x.getNoc())
                sum.put(Constants.CODE_NOC, x.getNoc());
            if (sum.get(Constants.CODE_NOF) > x.getNof())
                sum.put(Constants.CODE_NOF, x.getNof());
            if (sum.get(Constants.CODE_NOM) > x.getNom())
                sum.put(Constants.CODE_NOM, x.getNom());
            if (sum.get(Constants.CODE_STAT) > x.getStat())
                sum.put(Constants.CODE_STAT, x.getStat());
        });
        return sum;
    }

    /**
     * Function that initialize the Statistic Map
     *
     * @return the map { indexOfMetric (1-19) -> 0.0 }
     */
    public HashMap<Integer, Double> initStatisticMap() {
        HashMap<Integer, Double> sum = new HashMap<>();
        singleVersionDataArray.forEach((x) -> {
            sum.putIfAbsent(Constants.CODE_NTC, 0.0);
            sum.putIfAbsent(Constants.CODE_TTL, 0.0);
            sum.putIfAbsent(Constants.CODE_TLR, 0.0);
            sum.putIfAbsent(Constants.CODE_MTLR, 0.0);
            sum.putIfAbsent(Constants.CODE_MRTL, 0.0);
            sum.putIfAbsent(Constants.CODE_MCR, 0.0);
            sum.putIfAbsent(Constants.CODE_MMR, 0.0);
            sum.putIfAbsent(Constants.CODE_MCMMR, 0.0);
            sum.putIfAbsent(Constants.CODE_CC, 0.0);
            sum.putIfAbsent(Constants.CODE_CODE_SMELLS, 0.0);
            sum.putIfAbsent(Constants.CODE_TD, 0.0);
            sum.putIfAbsent(Constants.CODE_DEBT_RATIO, 0.0);
            sum.putIfAbsent(Constants.CODE_CLOC, 0.0);
            sum.putIfAbsent(Constants.CODE_LOC, 0.0);
            sum.putIfAbsent(Constants.CODE_NOC, 0.0);
            sum.putIfAbsent(Constants.CODE_NOF, 0.0);
            sum.putIfAbsent(Constants.CODE_NOM, 0.0);
            sum.putIfAbsent(Constants.CODE_STAT, 0.0);
        });
        return sum;
    }

    /**
     * Function that initialize the Statistic Map
     *
     * @return the map { indexOfMetric (1-19) -> Double.MAX_VALUE }
     */
    public HashMap<Integer, Double> initMinStatisticMap() {
        HashMap<Integer, Double> sum = new HashMap<>();
        singleVersionDataArray.forEach((x) -> {
            sum.putIfAbsent(Constants.CODE_NTC, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_TTL, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_TLR, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_MTLR, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_MRTL, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_MCR, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_MMR, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_MCMMR, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_CC, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_CODE_SMELLS, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_TD, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_DEBT_RATIO, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_CLOC, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_LOC, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_NOC, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_NOF, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_NOM, Double.MAX_VALUE);
            sum.putIfAbsent(Constants.CODE_STAT, Double.MAX_VALUE);
        });
        return sum;
    }

    public HashMap<String, HashMap<Integer, Double>> getCompleteStatistics() {
        return completeStatistics;
    }

    /**
     * Function that calculate all the statistics and set them in the map <br>
     * { nameOfTheStatistic ("avg","variance", "std", "max", "min") -> { indexOfMetric (1-19) -> valueOfTheStatistic }}
     */
    public void calculateStatistics() {
        completeStatistics.put(Constants.TEXT_AVERAGE, calculateAverage());
        completeStatistics.put(Constants.TEXT_VARIANCE, calculateVariance());
        completeStatistics.put(Constants.TEXT_STANDARD_DEVIATION, calculateStandardDeviation(completeStatistics.get(Constants.TEXT_VARIANCE)));
        completeStatistics.put(Constants.TEXT_MAX, calculateMax());
        completeStatistics.put(Constants.TEXT_MIN, calculateMin());
    }

    public HashMap<String, Integer> getClassChanges() {
        return classChanges;
    }

    public AllVersionsData() {
        this.singleVersionDataArray = new ArrayList<>();
        this.classChanges = new HashMap<>();
        completeStatistics = new HashMap<>();
        this.MRR = 0.0;
        this.TSV = 0.0;
        this.NTR = 0;
    }

    /**
     * Function that resets all the data of the versions
     */
    public void reset() {
        this.singleVersionDataArray.clear();
        this.classChanges.clear();
        this.repoName = "";
        this.completeStatistics.clear();
        this.MRR = 0.0;
        this.TSV = 0.0;
        this.NTR = 0;
    }
}
