package com.fragilityanalysis.gui;

import com.fragilityanalysis.data.AllVersionsData;
import com.fragilityanalysis.data.Constants;
import com.fragilityanalysis.gui.MainGUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.RectangleAnchor;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MetricsChart extends JFrame {

    private final AllVersionsData allVersionsData;
    private final ArrayList<Integer> selectedGraphs;
    private final ArrayList<Map<Integer, String>> mapSeries_Versions = new ArrayList<>();

    public MetricsChart(AllVersionsData allVersionsData, ArrayList<Integer> selectedGraphs) {
        this.allVersionsData = allVersionsData;
        this.selectedGraphs = selectedGraphs;
        initUI();
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    private void initUI() {

        XYDataset dataset = createDataset();
        org.jfree.chart.JFreeChart chart = createChart(dataset);
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        chartPanel.setMouseWheelEnabled(true);
        add(chartPanel);
        pack();
        setTitle("Line chart");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private XYDataset createDataset() {
        ArrayList<XYSeries> series = new ArrayList<>();
        for (Integer selectedGraph : selectedGraphs) {
            series.add(new XYSeries(Constants.getNameOnIndex(selectedGraph, MainGUI.language)));
            mapSeries_Versions.add(new HashMap<>());
        }
        for (int i = 0; i < allVersionsData.getSingleVersionDataArray().size(); i++) {
            for (int j = 0; j < selectedGraphs.size(); j++) {
                series.get(j).add(i, getValueOnIndex(i, selectedGraphs.get(j)));
                //System.out.println("Added to the map the couple: [ " + i + " ] -> [ " + allVersionData.getSingleVersionDataArray().get(i).getVersionName() + " ]");
                mapSeries_Versions.get(j).put(i, allVersionsData.getSingleVersionDataArray().get(i).getVersionName());
            }
        }

        var dataset = new XYSeriesCollection();
        series.forEach(dataset::addSeries);
        return dataset;
    }

    private Double getValueOnIndex(Integer index, Integer code) {
        return switch (code) {
            case 1 -> allVersionsData.getSingleVersionDataArray().get(index).getNtc();
            case 2 -> allVersionsData.getSingleVersionDataArray().get(index).getTtl();
            case 3 -> allVersionsData.getSingleVersionDataArray().get(index).getTlr();
            case 4 -> allVersionsData.getSingleVersionDataArray().get(index).getMtlr();
            case 5 -> allVersionsData.getSingleVersionDataArray().get(index).getMrtl();
            case 6 -> allVersionsData.getSingleVersionDataArray().get(index).getMcr();
            case 7 -> allVersionsData.getSingleVersionDataArray().get(index).getMmr();
            case 8 -> allVersionsData.getSingleVersionDataArray().get(index).getMcmmr();
            case 9 -> allVersionsData.getSingleVersionDataArray().get(index).getCc();
            case 11 -> allVersionsData.getSingleVersionDataArray().get(index).getCode_smells();
            case 12 -> allVersionsData.getSingleVersionDataArray().get(index).getTd();
            case 13 -> allVersionsData.getSingleVersionDataArray().get(index).getDebt_ratio();
            case 14 -> allVersionsData.getSingleVersionDataArray().get(index).getCloc();
            case 15 -> allVersionsData.getSingleVersionDataArray().get(index).getLoc();
            case 16 -> allVersionsData.getSingleVersionDataArray().get(index).getNoc();
            case 17 -> allVersionsData.getSingleVersionDataArray().get(index).getNof();
            case 18 -> allVersionsData.getSingleVersionDataArray().get(index).getNom();
            case 19 -> allVersionsData.getSingleVersionDataArray().get(index).getStat();
            default -> null;
        };
    }

    private org.jfree.chart.JFreeChart createChart(final XYDataset dataset) {

        org.jfree.chart.JFreeChart chart = ChartFactory.createXYLineChart(
                "Chart",
                "Number of Release",
                "Selected Values",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        XYPlot plot = chart.getXYPlot();

        var renderer = new XYLineAndShapeRenderer();

        for (int j = 0; j < selectedGraphs.size(); j++) {
            renderer.setSeriesPaint(j, getColor(j));
            //renderer.setSeriesShapesVisible(j, false);
        }
        Constants.setupChartPlot(chart, plot, renderer);
        XYToolTipGenerator xyToolTipGenerator = (dataset1, series, item) -> {
            Number x1 = dataset1.getX(series, item);
            Number y1 = dataset1.getY(series, item);
            return "<html>" +
                    Constants.getNameOnIndex(selectedGraphs.get(series), MainGUI.language) + ": " + Constants.df.format(y1) + "<br>" +
                    "Release: " + mapSeries_Versions.get(series).get(x1.intValue()) +
                    "</html>";
        };

        final ArrayList<Marker> start = new ArrayList<>();
        XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            render.setSeriesToolTipGenerator(i, xyToolTipGenerator);
            //System.out.println("Added tooltip number: "+i);
            if (allVersionsData.getCompleteStatistics().get(Constants.TEXT_AVERAGE) != null) {
                start.add(i, new ValueMarker(allVersionsData.getCompleteStatistics().get(Constants.TEXT_AVERAGE).get(selectedGraphs.get(i))));
                start.get(i).setPaint(getColor(i));
                start.get(i).setLabel("Average - [ " + Constants.df.format(allVersionsData.getCompleteStatistics().get(Constants.TEXT_AVERAGE).get(selectedGraphs.get(i))) + " ]");
                start.get(i).setLabelAnchor(RectangleAnchor.BOTTOM_LEFT);
                start.get(i).setLabelTextAnchor(TextAnchor.TOP_LEFT);
                plot.addRangeMarker(start.get(i));
            }
        }
        return chart;
    }

    private Color getColor(int i) {
        return switch (i % 10) {
            case 0 -> Color.RED;
            case 1 -> Color.BLUE;
            case 2 -> Color.BLACK;
            case 3 -> Color.CYAN;
            case 4 -> Color.GREEN;
            case 5 -> Color.MAGENTA;
            case 6 -> Color.ORANGE;
            case 7 -> Color.YELLOW;
            case 8 -> Color.PINK;
            case 9 -> Color.GRAY;
            default -> null;
        };
    }
}
