package com.fragilityanalysis.data;

public class Statistics {
    private String name;
    private Double average;
    private Double variance;
    private Double std;
    private Double max;
    private Double min;

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }

    public Double getStd() {
        return std;
    }

    public void setStd(Double std) {
        this.std = std;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Statistics(String name, Double average, Double variance, Double std, Double max, Double min) {
        this.name = name;
        this.average = average;
        this.variance = variance;
        this.std = std;
        this.max = max;
        this.min = min;
    }
}
