package com.fragilityanalysis.gui;

import com.fragilityanalysis.data.AllVersionsData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ChangesListFiltered {

    private JList<String> changesList;
    private final HashMap<String, Integer> classChanges;
    private final AllVersionsData allVersionsData;
    private JTextField searchText;
    private Map<String, Integer> sortedmap;
    private JFrame mainFrame;

    public ChangesListFiltered(HashMap<String, Integer> classChanges, AllVersionsData allVersionsData) {
        this.classChanges = classChanges;
        this.allVersionsData = allVersionsData;
        initComponents();
    }

    public JFrame getMainFrame() {
        return mainFrame;
    }

    /**
     * Function that initializes all the components of the GUI
     */
    private void initComponents() {
        changesList = new JList<>();
        mainFrame = new JFrame();

        JComponent mainPanel = new JPanel(new BorderLayout(4, 4));
        mainPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
        searchText = new JTextField();
        searchText.setUI(new HintTextFieldUI("Search", true));
        mainPanel.add(searchText, BorderLayout.NORTH);
        mainPanel.add(changesList, BorderLayout.CENTER);

        DefaultListModel<String> model = new DefaultListModel<>();
        mainFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        sortedmap = new LinkedHashMap<>();
        this.classChanges.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedmap.put(x.getKey(), x.getValue()));


        changesList.setModel(model);

        sortedmap.forEach((k, v) -> model.add(model.getSize(), k + "  [" + v + "]"));


        mainFrame.setPreferredSize(new Dimension(1200, 800));
        mainFrame.setMinimumSize(new Dimension(800, 600));
        JScrollPane scrollFrame = new JScrollPane(mainPanel);
        scrollFrame.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        mainFrame.add(scrollFrame);

        searchText.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                if (searchText.getText() != null && !searchText.getText().equals("")) {
                    filter(searchText.getText());
                }
            }

            public void removeUpdate(DocumentEvent e) {
                if (searchText.getText() != null && !searchText.getText().equals("")) {
                    filter(searchText.getText());
                }
            }

            public void insertUpdate(DocumentEvent e) {
                if (searchText.getText() != null && !searchText.getText().equals("")) {
                    filter(searchText.getText());
                }
            }
        });

        changesList.addListSelectionListener(this::changesListValueChanged);
        mainFrame.setVisible(true);
    }

    /**
     * Function that adds the listener on the click (to generate the class changes graph) of a class on the changes list
     *
     * @param evt listener event
     */
    private void changesListValueChanged(ListSelectionEvent evt) {
        if (!evt.getValueIsAdjusting() && changesList.getSelectedValue() != null) {
            String s = changesList.getSelectedValue();
            ClassChangesChart chart = new ClassChangesChart(allVersionsData, s.split(" ")[0]);
            chart.setVisible(true);
        }
    }

    /**
     * Function that filters the changes listed class matching the search field
     *
     * @param s (string of the search)
     */
    private void filter(String s) {
        DefaultListModel<String> modelNew = new DefaultListModel<>();
        searchText.setEditable(false);
        sortedmap.forEach((k, v) -> {
            if (k.contains(s)) {
                modelNew.add(modelNew.getSize(), k + " [" + v + "]");
            }
        });
        changesList.setModel(modelNew);
        searchText.setEditable(true);
    }
}