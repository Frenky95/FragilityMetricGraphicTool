package com.fragilityanalysis.gui;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import com.fragilityanalysis.data.AllVersionsData;
import com.fragilityanalysis.data.Constants;
import com.fragilityanalysis.gui.MainGUI;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import javax.swing.*;

public class BoxPlot extends JFrame {

    private final AllVersionsData allVersionsData;

    public BoxPlot(AllVersionsData allVersionsData) {

        super("BoxPlot");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.allVersionsData = allVersionsData;
        final BoxAndWhiskerCategoryDataset dataset = createSampleDataset();

        final CategoryAxis xAxis = new CategoryAxis("Metric");
        final NumberAxis yAxis = new NumberAxis("Value");
        yAxis.setAutoRangeIncludesZero(true);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setMaxOutlierVisible(true);
        renderer.setMinOutlierVisible(true);
        renderer.setFillBox(true);
        renderer.setDefaultToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        //this.windowClosing();
        final JFreeChart chart = new JFreeChart(
                "BoxPlot",
                new Font("SansSerif", Font.BOLD, 14),
                plot,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(850, 600));
        setContentPane(chartPanel);
    }

    private BoxAndWhiskerCategoryDataset createSampleDataset() {

        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

        for (int i = 3; i < 9; i++) {
            List<Double> list = new ArrayList<>();
            for (int j = 1; j < allVersionsData.getSingleVersionDataArray().size(); j++) {
                switch (i) {
                    case 3 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getTlr());
                    case 4 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getMtlr());
                    case 5 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getMrtl());
                    case 6 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getMcr());
                    case 7 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getMmr());
                    case 8 -> list.add(allVersionsData.getSingleVersionDataArray().get(j).getMcmmr());
                }
            }
            dataset.add(list, i, Constants.getNameOnIndex(i, MainGUI.language));
        }
        return dataset;
    }

}
