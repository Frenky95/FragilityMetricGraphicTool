package com.fragilityanalysis.gui;

import com.fragilityanalysis.data.AllVersionsData;
import com.fragilityanalysis.data.Constants;
import com.fragilityanalysis.gui.MainGUI;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ClassChangesChart extends JFrame {

    private final AllVersionsData allVersionsData;
    private final String classPath;
    private final Map<Integer, String> mapSeries_Versions = new HashMap<>();


    public ClassChangesChart(AllVersionsData allVersionsData, String classPath) {
        this.allVersionsData = allVersionsData;
        this.classPath = classPath;
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

        setTitle(this.classPath + " - Changes history");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private XYDataset createDataset() {
        XYSeries series = new XYSeries(classPath);
        Integer change;

        for (int i = 0; i < allVersionsData.getSingleVersionDataArray().size(); i++) {
            mapSeries_Versions.put(i, allVersionsData.getSingleVersionDataArray().get(i).getVersionName());
        }
        for (int i = 0; i < allVersionsData.getSingleVersionDataArray().size(); i++) {
            change = allVersionsData.getSingleVersionDataArray().get(i).getChange().get(classPath);
            if (change == null) {
                series.add(i, 0);
            } else {
                series.add(i, change);
            }
        }
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private org.jfree.chart.JFreeChart createChart(final XYDataset dataset) {

        org.jfree.chart.JFreeChart chart = ChartFactory.createXYLineChart(
                classPath + " Changes every release",
                "Releases",
                "LOCs changed",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = chart.getXYPlot();
        var renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.RED);
        Constants.setupChartPlot(chart, plot, renderer);
        XYToolTipGenerator xyToolTipGenerator = (dataset1, series, item) -> {
            Number x1 = dataset1.getX(series, item);
            Number y1 = dataset1.getY(series, item);
            return "<html>" +
                    Constants.getNameOnIndex(15, MainGUI.language) + ": " + y1.toString() + "<br>" +
                    "Release: " + mapSeries_Versions.get(x1.intValue()) +
                    "</html>";
        };

        XYLineAndShapeRenderer render = (XYLineAndShapeRenderer) plot.getRenderer();
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            render.setSeriesToolTipGenerator(i, xyToolTipGenerator);
        }
        return chart;
    }
}
