package com.fragilityanalysis.gui;

import com.fragilityanalysis.data.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jfree.ui.RefineryUtilities;
import org.kordamp.bootstrapfx.BootstrapFX;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class MainGUI extends Application {

    public static Integer language = Constants.LANGUAGE_ENG_CODE;
    private LoadDataFromFile loadDataFromFile;
    private AllVersionsData allVersionsData;
    private ArrayList<Integer> listOfGraph;
    private Process pythonProcess;
    private ChangesListFiltered changesListFiltered;
    private BoxPlot boxPlot;
    private String repoText;
    private MenuItem buttonResetMenu;
    private MenuItem buttonChangeLanguage;
    private MenuItem buttonChangeLanguageItalian;
    private MenuItem buttonChangeLanguageEnglish;
    private TextField repoName;
    private TextField textFieldTestingToolName;
    private TabPane tabPane;
    private TableView<Statistics> tabella;
    private Tab tabInput;
    private Tab tabStatistiche;
    private Tab tabGrafici;
    private Tab tabLoadingPython;
    private Label labelPythonScript;
    private Label labelPythonTestingToolName;
    private Label labelFieldRepository;
    private Label labelVolatility;
    private Label labelPythonIndex;
    private Label labelPythonRepository;
    private Label labelPythonRepositoryLoading;
    private Label labelPythonLastXReleases;
    private Label labelCodeMetrics;
    private Label labelTestMetrics;
    private Label labelRatioMetrics;
    private Label labelFragilityMetrics;
    private FXMLLoader loader;
    private Button buttonShowBoxPlot;
    private Button buttonShowChanges;
    private Button buttonShowSelectedGraphs;
    private Button buttonStartPython;
    private Button buttonStartExplorer;
    private CheckBox checkBoxLOC;
    private CheckBox checkBoxTTL;
    private CheckBox checkBoxNTC;
    private CheckBox checkBoxTLR;
    private CheckBox checkBoxMTLR;
    private CheckBox checkBoxMRTL;
    private CheckBox checkBoxMCR;
    private CheckBox checkBoxMMR;
    private CheckBox checkBoxMCMMR;
    private CheckBox checkBoxCLOC;
    private CheckBox checkBoxCC;
    private CheckBox checkBoxCODE_SMELL;
    private CheckBox checkBoxTD;
    private CheckBox checkBoxDEBT_RATIO;
    private CheckBox checkBoxNOC;
    private CheckBox checkBoxNOF;
    private CheckBox checkBoxNOM;
    private CheckBox checkBoxSTAT;
    private CheckBox checkboxPythonLastRelease;
    private Spinner<Integer> spinnerNRelease;
    private boolean lastReleases = false;
    private Stage stage;
    private Alert alert;

    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        listOfGraph = new ArrayList<>();
        loader = new FXMLLoader(getClass().getClassLoader().getResource("mainGUI.fxml"));
        this.loadDataFromFile = new LoadDataFromFile();
        Runtime.getRuntime().addShutdownHook(new Thread((Runnable) pythonProcess) {
            @Override
            public void run() {
                if (pythonProcess != null && pythonProcess.isAlive()) {
                    pythonProcess.destroyForcibly();
                    System.out.println("Shutdown python process1");
                }
            }
        });
        Parent root = loader.load();
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(root, Math.round(screenBounds.getMaxX() * 2 / 3), Math.round(screenBounds.getMaxY() * 2 / 3));
        alert = new Alert(Alert.AlertType.ERROR);
        setupMainGUI();
        stage.setTitle(Constants.TITLE_FRAGILITY.getLanguage(language));
        stage.setScene(scene);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        labelVolatility.setTextFill(Color.rgb(150, 173, 230));
        stage.show();
    }

    /**
     * Function that loads all the GUI and setup all the events on the components.
     */
    private void setupMainGUI() {

        loadFXMLComponents();
        changeLanguage();
        setupCheckBoxes();
        setupButtonsEvents();
        tabGrafici.setDisable(true);
        tabStatistiche.setDisable(true);
        setupSpinner();
        loadBootstrap();
    }

    private void setupSpinner() {

        final SpinnerValueFactory.IntegerSpinnerValueFactory factory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        2, Integer.MAX_VALUE
                );
        spinnerNRelease.setValueFactory(factory);
    }

    /**
     * Function that setup the Statistic Table
     */
    private void initTabStat() {
        tabella.getItems().clear();
        tabella.getColumns().clear();
        tabella.getColumns().removeAll();
        tabella.getItems().removeAll();
        TableColumn colonnaNome = new TableColumn(Constants.TABLE_NAME_VAR.getLanguage(language));
        colonnaNome.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn colonnaMedia = new TableColumn(Constants.TABLE_NAME_MEDIA.getLanguage(language));
        colonnaMedia.setCellValueFactory(new PropertyValueFactory<>("average"));
        TableColumn colonnaVarianza = new TableColumn(Constants.TABLE_NAME_VARIANZA.getLanguage(language));
        colonnaVarianza.setCellValueFactory(new PropertyValueFactory<>("variance"));
        TableColumn colonnaStd = new TableColumn(Constants.TABLE_NAME_SCARTO.getLanguage(language));
        colonnaStd.setCellValueFactory(new PropertyValueFactory<>("std"));
        TableColumn colonnaMax = new TableColumn(Constants.TABLE_NAME_MAX.getLanguage(language));
        colonnaMax.setCellValueFactory(new PropertyValueFactory<>("max"));
        TableColumn colonnaMin = new TableColumn(Constants.TABLE_NAME_MIN.getLanguage(language));
        colonnaMin.setCellValueFactory(new PropertyValueFactory<>("min"));
        tabella.getColumns().addAll(colonnaNome, colonnaMedia, colonnaVarianza, colonnaStd, colonnaMax, colonnaMin);
    }

    /**
     * Function that update the values in the Statistics table with the ones loaded.
     */
    private void updateTabStat() {
        for (int i = 0; i <= 19; i++) {
            if (i != 0 && i != 10) {
                if (allVersionsData != null && allVersionsData.getSingleVersionDataArray().size() != 0) {
                    tabella.getItems().add(new Statistics(Constants.getNameOnIndex(i, language), (double) Math.round(allVersionsData.getCompleteStatistics().get(Constants.TEXT_AVERAGE).get(i) * 100) / 100, (double) Math.round(allVersionsData.getCompleteStatistics().get(Constants.TEXT_VARIANCE).get(i) * 100) / 100, (double) Math.round(allVersionsData.getCompleteStatistics().get(Constants.TEXT_STANDARD_DEVIATION).get(i) * 100) / 100, (double) Math.round(allVersionsData.getCompleteStatistics().get(Constants.TEXT_MAX).get(i) * 100) / 100, (double) Math.round(allVersionsData.getCompleteStatistics().get(Constants.TEXT_MIN).get(i) * 100) / 100));
                }
            }
        }
        autoResizeColumns(tabella);
    }

    /**
     * Function that given the TableView resizes every column based on the larger text inside it.
     *
     */
    private static void autoResizeColumns(TableView<?> table) {
        //Set the right policy
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().forEach((column) ->
        {
            //Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < table.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-widht with some extra space
            column.setPrefWidth(max + 10.0d);
        });
    }

    /**
     * Function that clear the TableView of statistics
     */
    private void clearTabStat() {
        tabella.getItems().clear();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroyForcibly();
            System.out.println("Shutdown python process");
        }
    }

    /**
     * Function that resets the program
     */
    private void reset() {
        if (allVersionsData != null) {
            allVersionsData.reset();
        }
        buttonStartPython.setDisable(false);
        tabInput.setDisable(false);
        tabPane.getSelectionModel().select(tabInput);
        tabGrafici.setDisable(true);
        tabStatistiche.setDisable(true);
        tabLoadingPython.setDisable(true);
        clearTabStat();
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroy();
            System.out.println("Shutdown python process");
        }
        boxPlot = null;
        changesListFiltered = null;
        if (pythonProcess != null && pythonProcess.isAlive()) {
            pythonProcess.destroyForcibly();
            System.out.println("Shutdown python process");
        }
        changeLanguage();
    }

    /**
     * Function that loads all the GUI components from the mainGUI.fxml file
     */
    private void loadFXMLComponents() {
        buttonResetMenu = (MenuItem) loader.getNamespace().get("resetMenu");
        buttonChangeLanguage = (MenuItem) loader.getNamespace().get("changeLanguage");
        buttonChangeLanguageItalian = (MenuItem) loader.getNamespace().get("languageItalian");
        buttonChangeLanguageEnglish = (MenuItem) loader.getNamespace().get("languageEnglish");
        buttonStartPython = (Button) loader.getNamespace().get("buttonStartPython");
        buttonStartExplorer = (Button) loader.getNamespace().get("buttonStartExplorer");
        buttonShowBoxPlot = (Button) loader.getNamespace().get("buttonShowBoxPlot");
        buttonShowChanges = (Button) loader.getNamespace().get("buttonShowChanges");
        buttonShowSelectedGraphs = (Button) loader.getNamespace().get("buttonShowGraphs");
        repoName = (TextField) loader.getNamespace().get("textFieldRepository");
        textFieldTestingToolName = (TextField) loader.getNamespace().get("textFieldTestingToolName");
        tabPane = (TabPane) loader.getNamespace().get("tabPane");
        tabInput = (Tab) loader.getNamespace().get("tabInputSelection");
        tabStatistiche = (Tab) loader.getNamespace().get("tabStatistiche");
        tabGrafici = (Tab) loader.getNamespace().get("tabGrafici");
        tabLoadingPython = (Tab) loader.getNamespace().get("tabExecutingPython");
        labelPythonScript = (Label) loader.getNamespace().get("textPythonLoading");
        labelPythonTestingToolName = (Label) loader.getNamespace().get("textPythonTestingToolName");
        labelPythonIndex = (Label) loader.getNamespace().get("textPythonIndex");
        labelCodeMetrics = (Label) loader.getNamespace().get("labelCodeMetrics");
        labelRatioMetrics = (Label) loader.getNamespace().get("labelRatioMetrics");
        labelFragilityMetrics = (Label) loader.getNamespace().get("labelFragilityMetrics");
        labelFieldRepository = (Label) loader.getNamespace().get("textPythonInputRepository");
        labelTestMetrics = (Label) loader.getNamespace().get("labelTestMetrics");
        labelVolatility = (Label) loader.getNamespace().get("textAreaVolatility");
        labelPythonLastXReleases = (Label) loader.getNamespace().get("textPythonlastReleases");
        labelPythonRepository = (Label) loader.getNamespace().get("textAreaRepository");
        labelPythonRepositoryLoading = (Label) loader.getNamespace().get("textPythonRepository");
        tabella = (TableView) loader.getNamespace().get("tableStatistiche");
        checkBoxLOC = (CheckBox) loader.getNamespace().get("checkboxLOC");
        checkBoxTTL = (CheckBox) loader.getNamespace().get("checkboxTTL");
        checkBoxNTC = (CheckBox) loader.getNamespace().get("checkboxNTC");
        checkBoxTLR = (CheckBox) loader.getNamespace().get("checkboxTLR");
        checkBoxMTLR = (CheckBox) loader.getNamespace().get("checkboxMTLR");
        checkBoxMRTL = (CheckBox) loader.getNamespace().get("checkboxMRTL");
        checkBoxMCR = (CheckBox) loader.getNamespace().get("checkboxMCR");
        checkBoxMMR = (CheckBox) loader.getNamespace().get("checkboxMMR");
        checkBoxMCMMR = (CheckBox) loader.getNamespace().get("checkboxMCMMR");
        checkBoxCLOC = (CheckBox) loader.getNamespace().get("checkboxCLOC");
        checkBoxCC = (CheckBox) loader.getNamespace().get("checkboxCC");
        checkBoxCODE_SMELL = (CheckBox) loader.getNamespace().get("checkboxCODE_SMELL");
        checkBoxTD = (CheckBox) loader.getNamespace().get("checkboxTD");
        checkBoxDEBT_RATIO = (CheckBox) loader.getNamespace().get("checkboxDEBT_RATIO");
        checkBoxNOC = (CheckBox) loader.getNamespace().get("checkboxNOC");
        checkBoxNOF = (CheckBox) loader.getNamespace().get("checkboxNOF");
        checkBoxNOM = (CheckBox) loader.getNamespace().get("checkboxNOM");
        checkBoxSTAT = (CheckBox) loader.getNamespace().get("checkboxSTAT");
        checkboxPythonLastRelease = (CheckBox) loader.getNamespace().get("checkboxPythonLast");
        spinnerNRelease = (Spinner<Integer>) loader.getNamespace().get("spinnerLastRelease");
    }

    private void loadBootstrap() {
        buttonStartPython.getStyleClass().addAll("btn", "btn-primary");
        buttonStartExplorer.getStyleClass().addAll("btn", "btn-primary");
        buttonShowChanges.getStyleClass().addAll("btn", "btn-primary");
        buttonShowSelectedGraphs.getStyleClass().addAll("btn", "btn-primary");
        buttonShowBoxPlot.getStyleClass().addAll("btn", "btn-primary");
        labelVolatility.getStyleClass().addAll("h2", "text-primary");
        labelPythonRepository.getStyleClass().addAll("h1", "strong");
        labelPythonTestingToolName.getStyleClass().addAll("h2", "text-primary", "strong");
        labelPythonRepositoryLoading.getStyleClass().addAll("h1", "text-primary", "strong");
        labelPythonScript.getStyleClass().addAll("h2", "text-primary", "strong");
        labelFieldRepository.getStyleClass().addAll("h2", "text-primary", "strong");
        labelPythonLastXReleases.getStyleClass().addAll("h3", "text-primary");
        labelPythonIndex.getStyleClass().addAll("h3", "strong");
        labelPythonIndex.setTextFill(Color.rgb(71, 225, 12));
        labelCodeMetrics.setTextFill(Color.rgb(71, 225, 12));
        labelRatioMetrics.setTextFill(Color.rgb(71, 225, 12));
        labelTestMetrics.setTextFill(Color.rgb(71, 225, 12));
        labelFragilityMetrics.setTextFill(Color.rgb(71, 225, 12));
        labelPythonRepository.setTextFill(Color.rgb(71, 225, 12));
        labelCodeMetrics.getStyleClass().addAll("h3", "strong");
        labelRatioMetrics.getStyleClass().addAll("h3", "strong");
        labelTestMetrics.getStyleClass().addAll("h3", "strong");
        labelFragilityMetrics.getStyleClass().addAll("h3", "strong");
        labelPythonLastXReleases.setVisible(lastReleases);
        spinnerNRelease.setVisible(lastReleases);
    }

    private void changeLanguage() {
        checkBoxNTC.setText(Constants.NAME_EXT_NTC.getLanguage(language));
        checkBoxTTL.setText(Constants.NAME_EXT_TTL.getLanguage(language));
        checkBoxTLR.setText(Constants.NAME_EXT_TLR.getLanguage(language));
        checkBoxMTLR.setText(Constants.NAME_EXT_MTLR.getLanguage(language));
        checkBoxMRTL.setText(Constants.NAME_EXT_MRTL.getLanguage(language));
        checkBoxMCR.setText(Constants.NAME_EXT_MCR.getLanguage(language));
        checkBoxMMR.setText(Constants.NAME_EXT_MMR.getLanguage(language));
        checkBoxMCMMR.setText(Constants.NAME_EXT_MCMMR.getLanguage(language));
        checkBoxCLOC.setText(Constants.NAME_EXT_CLOC.getLanguage(language));
        checkBoxLOC.setText(Constants.NAME_EXT_LOC.getLanguage(language));
        checkBoxCC.setText(Constants.NAME_EXT_CC.getLanguage(language));
        checkBoxCODE_SMELL.setText(Constants.NAME_EXT_CODE_SMELL.getLanguage(language));
        checkBoxTD.setText(Constants.NAME_EXT_TD.getLanguage(language));
        checkBoxDEBT_RATIO.setText(Constants.NAME_EXT_DEBT_RATIO.getLanguage(language));
        checkBoxNOC.setText(Constants.NAME_EXT_NOC.getLanguage(language));
        checkBoxNOF.setText(Constants.NAME_EXT_NOF.getLanguage(language));
        checkBoxNOM.setText(Constants.NAME_EXT_NOM.getLanguage(language));
        checkBoxSTAT.setText(Constants.NAME_EXT_STAT.getLanguage(language));

        checkBoxLOC.setTooltip(new Tooltip(Constants.TOOLTIP_LOC.getLanguage(language)));
        checkBoxTTL.setTooltip(new Tooltip(Constants.TOOLTIP_TTL.getLanguage(language)));
        checkBoxNTC.setTooltip(new Tooltip(Constants.TOOLTIP_NTC.getLanguage(language)));
        checkBoxTLR.setTooltip(new Tooltip(Constants.TOOLTIP_TLR.getLanguage(language)));
        checkBoxMTLR.setTooltip(new Tooltip(Constants.TOOLTIP_MTLR.getLanguage(language)));
        checkBoxMRTL.setTooltip(new Tooltip(Constants.TOOLTIP_MRTL.getLanguage(language)));
        checkBoxMCR.setTooltip(new Tooltip(Constants.TOOLTIP_MCR.getLanguage(language)));
        checkBoxMMR.setTooltip(new Tooltip(Constants.TOOLTIP_MMR.getLanguage(language)));
        checkBoxMCMMR.setTooltip(new Tooltip(Constants.TOOLTIP_MCMMR.getLanguage(language)));
        checkBoxCLOC.setTooltip(new Tooltip(Constants.TOOLTIP_CLOC.getLanguage(language)));
        checkBoxCC.setTooltip(new Tooltip(Constants.TOOLTIP_CC.getLanguage(language)));
        checkBoxCODE_SMELL.setTooltip(new Tooltip(Constants.TOOLTIP_CODE_SMELL.getLanguage(language)));
        checkBoxTD.setTooltip(new Tooltip(Constants.TOOLTIP_TD.getLanguage(language)));
        checkBoxDEBT_RATIO.setTooltip(new Tooltip(Constants.TOOLTIP_DEBT_RATIO.getLanguage(language)));
        checkBoxNOC.setTooltip(new Tooltip(Constants.TOOLTIP_NOC.getLanguage(language)));
        checkBoxNOF.setTooltip(new Tooltip(Constants.TOOLTIP_NOF.getLanguage(language)));
        checkBoxNOM.setTooltip(new Tooltip(Constants.TOOLTIP_NOM.getLanguage(language)));
        checkBoxSTAT.setTooltip(new Tooltip(Constants.TOOLTIP_STAT.getLanguage(language)));

        buttonStartPython.setText(Constants.BUTTON_NAME_START_PYTHON.getLanguage(language));
        buttonShowChanges.setText(Constants.BUTTON_NAME_CHANGES.getLanguage(language));
        buttonStartExplorer.setText(Constants.BUTTON_NAME_FINDER.getLanguage(language));
        buttonShowBoxPlot.setText(Constants.BUTTON_NAME_PLOT_BOX_PLOT.getLanguage(language));
        buttonShowSelectedGraphs.setText(Constants.BUTTON_NAME_PLOT_GRAPHS.getLanguage(language));

        tabGrafici.setText(Constants.TAB_NAME_STRUMENTO.getLanguage(language));
        tabStatistiche.setText(Constants.TAB_NAME_STAT.getLanguage(language));
        tabInput.setText(Constants.TAB_NAME_INPUT.getLanguage(language));
        tabLoadingPython.setText(Constants.TAB_NAME_EXECUTING.getLanguage(language));

        Platform.runLater(this::initTabStat);
        Platform.runLater(this::updateTabStat);

        stage.setTitle(Constants.TITLE_FRAGILITY.getLanguage(language));
        if (allVersionsData != null) {
            Platform.runLater(() -> labelVolatility.setText(Constants.LABEL_NTR.getLanguage(language) + ": " + allVersionsData.getNTR() + "\n" + Constants.LABEL_MRR.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getMRR()) + "\n" + Constants.LABEL_TSV.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getTSV())));
        }
        buttonChangeLanguage.setText(Constants.BUTTON_CHANGE_LANGUAGE.getLanguage(language));
        labelPythonLastXReleases.setText(Constants.LABEL_PYTHON_LAST_X_RELEASES.getLanguage(language));
        labelTestMetrics.setText(Constants.LABEL_TEST_METRICS.getLanguage(language));
        labelRatioMetrics.setText(Constants.LABEL_RATIO_METRICS.getLanguage(language));
        labelCodeMetrics.setText(Constants.LABEL_CODE_METRICS.getLanguage(language));
        labelFragilityMetrics.setText(Constants.LABEL_FRAGILITY_METRICS.getLanguage(language));
        labelPythonTestingToolName.setText(Constants.LABEL_TEXT_TESTING_TOOL_NAME.getLanguage(language));
        alert.setTitle(Constants.ERROR_ALERT_TITLE.getLanguage(language));
    }

    /**
     * Function that setup the checkboxes and the events on them
     */
    private void setupCheckBoxes() {

        checkBoxLOC.setOnAction(actionEvent -> {
            if (checkBoxLOC.isSelected()) {
                listOfGraph.add(Constants.CODE_LOC);
            } else {
                listOfGraph.remove(Constants.CODE_LOC);
            }
        });
        checkBoxTTL.setOnAction(actionEvent -> {
            if (checkBoxTTL.isSelected()) {
                listOfGraph.add(Constants.CODE_TTL);
            } else {
                listOfGraph.remove(Constants.CODE_TTL);
            }
        });
        checkBoxNTC.setOnAction(actionEvent -> {
            if (checkBoxNTC.isSelected()) {
                listOfGraph.add(Constants.CODE_NTC);
            } else {
                listOfGraph.remove(Constants.CODE_NTC);
            }
        });
        checkBoxTLR.setOnAction(actionEvent -> {
            if (checkBoxTLR.isSelected()) {
                listOfGraph.add(Constants.CODE_TLR);
            } else {
                listOfGraph.remove(Constants.CODE_TLR);
            }
        });
        checkBoxMTLR.setOnAction(actionEvent -> {
            if (checkBoxMTLR.isSelected()) {
                listOfGraph.add(Constants.CODE_MTLR);
            } else {
                listOfGraph.remove(Constants.CODE_MTLR);
            }
        });
        checkBoxMRTL.setOnAction(actionEvent -> {
            if (checkBoxMRTL.isSelected()) {
                listOfGraph.add(Constants.CODE_MRTL);
            } else {
                listOfGraph.remove(Constants.CODE_MRTL);
            }
        });
        checkBoxMCR.setOnAction(actionEvent -> {
            if (checkBoxMCR.isSelected()) {
                listOfGraph.add(Constants.CODE_MCR);
            } else {
                listOfGraph.remove(Constants.CODE_MCR);
            }
        });
        checkBoxMMR.setOnAction(actionEvent -> {
            if (checkBoxMMR.isSelected()) {
                listOfGraph.add(Constants.CODE_MMR);
            } else {
                listOfGraph.remove(Constants.CODE_MMR);
            }
        });
        checkBoxMCMMR.setOnAction(actionEvent -> {
            if (checkBoxMCMMR.isSelected()) {
                listOfGraph.add(Constants.CODE_MCMMR);
            } else {
                listOfGraph.remove(Constants.CODE_MCMMR);
            }
        });
        checkBoxCLOC.setOnAction(actionEvent -> {
            if (checkBoxCLOC.isSelected()) {
                listOfGraph.add(Constants.CODE_CLOC);
            } else {
                listOfGraph.remove(Constants.CODE_CLOC);
            }
        });
        checkBoxCC.setOnAction(actionEvent -> {
            if (checkBoxCC.isSelected()) {
                listOfGraph.add(Constants.CODE_CC);
            } else {
                listOfGraph.remove(Constants.CODE_CC);
            }
        });
        checkBoxCODE_SMELL.setOnAction(actionEvent -> {
            if (checkBoxCODE_SMELL.isSelected()) {
                listOfGraph.add(Constants.CODE_CODE_SMELLS);
            } else {
                listOfGraph.remove(Constants.CODE_CODE_SMELLS);
            }
        });
        checkBoxTD.setOnAction(actionEvent -> {
            if (checkBoxTD.isSelected()) {
                listOfGraph.add(Constants.CODE_TD);
            } else {
                listOfGraph.remove(Constants.CODE_TD);
            }
        });
        checkBoxDEBT_RATIO.setOnAction(actionEvent -> {
            if (checkBoxDEBT_RATIO.isSelected()) {
                listOfGraph.add(Constants.CODE_DEBT_RATIO);
            } else {
                listOfGraph.remove(Constants.CODE_DEBT_RATIO);
            }
        });
        checkBoxNOC.setOnAction(actionEvent -> {
            if (checkBoxNOC.isSelected()) {
                listOfGraph.add(Constants.CODE_NOC);
            } else {
                listOfGraph.remove(Constants.CODE_NOC);
            }
        });
        checkBoxNOF.setOnAction(actionEvent -> {
            if (checkBoxNOF.isSelected()) {
                listOfGraph.add(Constants.CODE_NOF);
            } else {
                listOfGraph.remove(Constants.CODE_NOF);
            }
        });
        checkBoxNOM.setOnAction(actionEvent -> {
            if (checkBoxNOM.isSelected()) {
                listOfGraph.add(Constants.CODE_NOM);
            } else {
                listOfGraph.remove(Constants.CODE_NOM);
            }
        });
        checkBoxSTAT.setOnAction(actionEvent -> {
            if (checkBoxSTAT.isSelected()) {
                listOfGraph.add(Constants.CODE_STAT);
            } else {
                listOfGraph.remove(Constants.CODE_STAT);
            }
        });
        checkboxPythonLastRelease.setOnAction(actionEvent -> {
            lastReleases = checkboxPythonLastRelease.isSelected();
            labelPythonLastXReleases.setVisible(lastReleases);
            spinnerNRelease.setVisible(lastReleases);
        });

        checkBoxNTC.getStyleClass().addAll("h5", "text-primary");
        checkBoxTTL.getStyleClass().addAll("h5", "text-primary");
        checkBoxTLR.getStyleClass().addAll("h5", "text-primary");
        checkBoxMTLR.getStyleClass().addAll("h5", "text-primary");
        checkBoxMRTL.getStyleClass().addAll("h5", "text-primary");
        checkBoxMCR.getStyleClass().addAll("h5", "text-primary");
        checkBoxMMR.getStyleClass().addAll("h5", "text-primary");
        checkBoxMCMMR.getStyleClass().addAll("h5", "text-primary");
        checkBoxCLOC.getStyleClass().addAll("h5", "text-primary");
        checkBoxLOC.getStyleClass().addAll("h5", "text-primary");
        checkBoxCC.getStyleClass().addAll("h5", "text-primary");
        checkBoxCODE_SMELL.getStyleClass().addAll("h5", "text-primary");
        checkBoxTD.getStyleClass().addAll("h5", "text-primary");
        checkBoxDEBT_RATIO.getStyleClass().addAll("h5", "text-primary");
        checkBoxNOC.getStyleClass().addAll("h5", "text-primary");
        checkBoxNOF.getStyleClass().addAll("h5", "text-primary");
        checkBoxNOM.getStyleClass().addAll("h5", "text-primary");
        checkBoxSTAT.getStyleClass().addAll("h5", "text-primary");
        checkboxPythonLastRelease.getStyleClass().addAll("h5", "text-primary");

        checkBoxLOC.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxTTL.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxNTC.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxTLR.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxMTLR.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxMRTL.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxMCR.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxMMR.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxMCMMR.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxCLOC.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxCC.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxCODE_SMELL.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxTD.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxDEBT_RATIO.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxNOC.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxNOF.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxNOM.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);
        checkBoxSTAT.getTooltip().setFont(Constants.STD_TOOLTIP_FONT);

    }

    /**
     * Function that setup the events of all the buttons of the GUI
     */
    private void setupButtonsEvents() {
        buttonStartPython.setOnAction(actionEvent -> EventQueue.invokeLater(() -> {
            String s;
            ArrayList<String> version, ntrList;
            int ntrCount = 1, ntr = 0;
            try {
                repoText = repoName.getText();
                if (!(repoText.equals("") && repoText.split("/").length != 2)) {

                    buttonStartPython.setDisable(true);
                    Platform.runLater(() -> {
                        labelPythonScript.setText(Constants.LABEL_TEXT_STARTING_PYTHON.getLanguage(language));
                        labelPythonIndex.setText(Constants.LABEL_TEXT_SEARCHING_RELEASES.getLanguage(language));
                        if (textFieldTestingToolName.getText().equals(""))
                            labelPythonRepositoryLoading.setText("Repository: " + repoText);
                        else
                            labelPythonRepositoryLoading.setText("Repository: " + repoText + "\n Test Tool Name: " + textFieldTestingToolName.getText());
                    });
                    FileUtils.deleteDirectory(new File("./input/ProgettoDaAnalizzare"));
                    FileUtils.deleteDirectory(new File("./input/ProgettoDaAnalizzareVtag1"));
                    FileUtils.deleteDirectory(new File("./input/ProgettoDaAnalizzareVtag2"));

                    if (lastReleases) {
                        if (spinnerNRelease.getValue() >= 2 && spinnerNRelease.getValue() < Integer.MAX_VALUE)
                            if (textFieldTestingToolName.getText().equals("")) {
                                pythonProcess = Runtime.getRuntime().exec("py ./input/continuityPython.py " + repoName.getText() + " " + spinnerNRelease.getValue());
                            } else {
                                pythonProcess = Runtime.getRuntime().exec("py ./input/continuityPython.py " + repoName.getText() + " " + spinnerNRelease.getValue() + " " + textFieldTestingToolName.getText());
                            }
                    } else {
                        if (textFieldTestingToolName.getText().equals("")) {
                            pythonProcess = Runtime.getRuntime().exec("py ./input/main.py " + repoName.getText());
                        } else {
                            pythonProcess = Runtime.getRuntime().exec("py ./input/main.py " + repoName.getText() + " " + textFieldTestingToolName.getText());
                        }
                    }

                    BufferedReader stdInput = new BufferedReader(new InputStreamReader(pythonProcess.getInputStream()));
                    BufferedReader stdError = new BufferedReader(new InputStreamReader(pythonProcess.getErrorStream()));

                    tabInput.setDisable(true);
                    tabLoadingPython.setDisable(false);
                    tabPane.getSelectionModel().select(tabLoadingPython);

                    while ((s = stdInput.readLine()) != null) {
                        System.out.println(s);
                        if (s.contains("NTR")) {
                            ntrList = new ArrayList<>(Arrays.asList(s.split(" ")));
                            ntr = Integer.parseInt(ntrList.get(ntrList.size() - 1).trim());
                            if (lastReleases) {
                                ntrCount = ntr - spinnerNRelease.getValue() + 1;
                            }
                        }
                        version = new ArrayList<>(Arrays.asList(s.split("->-")));
                        //System.out.println("Splitted String: "+version);
                        if (version.size() > 1) {
                            //System.out.println("List size: "+version.size());
                            if (version.get(0).contains("Metriche fragilita test (versioni")) {
                                ArrayList<String> finalVersion = version;
                                int finalNtr = ntr;
                                ntrCount++;
                                int finalNtrCount = ntrCount;
                                Platform.runLater(() -> {
                                    labelPythonScript.setText(Constants.LABEL_TEXT_COMPUTING_0.getLanguage(language) + "[" + finalVersion.get(1).split("\\)")[0].trim() + "]" + Constants.LABEL_TEXT_COMPUTING_1.getLanguage(language) + "[" + finalVersion.get(0).split(" ")[finalVersion.get(0).split(" ").length - 1].trim() + "]");
                                    labelPythonIndex.setText("Release [" + (finalNtrCount) + "/" + finalNtr + "]");
                                });
                            }
                        }
                    }

                    System.out.println("Here is the standard error of the command (if any):\n");
                    while ((s = stdError.readLine()) != null) {
                        System.out.println(s);
                        if (s.contains("Repository not found.")) {
                            Platform.runLater(this::reset);
                            Platform.runLater(() -> {
                                alert.setHeaderText(Constants.ERROR_REPOSITORY_WRONG_NAME.getLanguage(language));
                                alert.show();
                            });
                        }
                    }

                    pythonProcess.waitFor();
                    String filename = "./input/ScannerPythonTesi_" + repoText.replace("/", "") + ".csv";
                    if (new File(filename).exists()) {
                        allVersionsData = loadDataFromFile.load(filename);
                        if (allVersionsData == null) {
                            Platform.runLater(this::reset);
                            Platform.runLater(() -> {
                                alert.setHeaderText(Constants.ERROR_FILE_LOADING.getLanguage(language));
                                alert.show();
                            });
                        } else {
                            allVersionsData.setRepoName(repoText);
                            allVersionsData.calculateStatistics();
                            tabGrafici.setDisable(false);
                            tabStatistiche.setDisable(false);
                            tabPane.getSelectionModel().select(tabGrafici);
                            tabLoadingPython.setDisable(true);
                            tabInput.setDisable(true);
                            if (pythonProcess.waitFor() == 0) {
                                Platform.runLater(() -> {
                                    updateTabStat();
                                    tabGrafici.setDisable(false);
                                    tabStatistiche.setDisable(false);
                                    tabInput.setDisable(true);
                                    tabLoadingPython.setDisable(true);
                                    tabPane.getSelectionModel().select(tabGrafici);
                                    if (textFieldTestingToolName.getText().equals("")) {
                                        labelPythonRepository.setText("Repository: " + allVersionsData.getRepoName());
                                    } else {
                                        labelPythonRepository.setText("Repository: " + allVersionsData.getRepoName() + "\nTest Suite: " + textFieldTestingToolName.getText());
                                    }
                                    labelVolatility.setText(Constants.LABEL_NTR.getLanguage(language) + ": " + allVersionsData.getNTR() + "\n" + Constants.LABEL_MRR.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getMRR()) + "\n" + Constants.LABEL_TSV.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getTSV()));
                                });
                            } else {
                                Platform.runLater(this::reset);
                                Platform.runLater(() -> {
                                    alert.setHeaderText(Constants.ERROR_FILE_CORRUPTED.getLanguage(language));
                                    alert.setContentText(Constants.ERROR_REPOSITORY_NAME_EMPTY_DESCRIPTION.getLanguage(language));
                                    alert.show();
                                });
                            }
                        }
                    } else {
                        Platform.runLater(this::reset);
                        Platform.runLater(() -> {
                            alert.setHeaderText(Constants.ERROR_REPOSITORY_WRONG_NAME.getLanguage(language));
                            alert.setContentText(Constants.ERROR_REPOSITORY_NAME_EMPTY_DESCRIPTION.getLanguage(language));
                            alert.show();
                        });

                    }
                } else {
                    Platform.runLater(this::reset);
                    Platform.runLater(() -> {
                        alert.setHeaderText(Constants.ERROR_REPOSITORY_NAME_EMPTY.getLanguage(language));
                        alert.setContentText(Constants.ERROR_REPOSITORY_NAME_EMPTY_DESCRIPTION.getLanguage(language));
                        alert.show();
                    });
                }
            } catch (IOException | InterruptedException e) {
                System.out.println("exception happened - here's what I know: ");
                e.printStackTrace();
            }
        }));
        buttonStartExplorer.setOnAction(actionEvent -> {
            final JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("./"));
            fc.showOpenDialog(fc.getParent());
            File file = fc.getSelectedFile();
            if (file != null) {
                String fileName = file.getPath();
                allVersionsData = loadDataFromFile.load(fileName);
                if (allVersionsData == null) {
                    Platform.runLater(this::reset);
                    Platform.runLater(() -> {
                        alert.setHeaderText(Constants.ERROR_FILE_CORRUPTED.getLanguage(language));
                        alert.show();
                    });
                } else {
                    allVersionsData.setRepoName(file.getName());
                    allVersionsData.calculateStatistics();
                    tabGrafici.setDisable(false);
                    tabStatistiche.setDisable(false);
                    tabInput.setDisable(true);
                    tabLoadingPython.setDisable(true);
                    tabPane.getSelectionModel().select(tabGrafici);
                    clearTabStat();
                    updateTabStat();
                    labelPythonRepository.setText("Input file: " + allVersionsData.getRepoName());
                    labelVolatility.setText(Constants.LABEL_NTR.getLanguage(language) + ": " + allVersionsData.getNTR() + "\n" + Constants.LABEL_MRR.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getMRR()) + "\n" + Constants.LABEL_TSV.getLanguage(language) + ": " + Constants.df.format(allVersionsData.getTSV()));
                }
            }
        });
        buttonResetMenu.setOnAction(actionEvent -> reset());
        buttonShowSelectedGraphs.setOnAction(actionEvent -> {
            if (allVersionsData != null && allVersionsData.getSingleVersionDataArray().size() != 0) {
                new MetricsChart(allVersionsData, listOfGraph).setVisible(true);
            }
        });
        buttonShowChanges.setOnAction(actionEvent -> {
            if (changesListFiltered == null) {
                changesListFiltered = new ChangesListFiltered(allVersionsData.getClassChanges(), allVersionsData);
            } else {
                changesListFiltered.getMainFrame().setVisible(true);
            }
        });
        buttonShowBoxPlot.setOnAction(actionEvent -> {
            if (boxPlot == null) {
                boxPlot = new BoxPlot(allVersionsData);
                boxPlot.pack();
                RefineryUtilities.centerFrameOnScreen(boxPlot);
            }
            boxPlot.setVisible(true);
        });
        buttonChangeLanguageItalian.setOnAction(actionEvent -> {
            language = Constants.LANGUAGE_ITA_CODE;
            changeLanguage();
        });
        buttonChangeLanguageEnglish.setOnAction(actionEvent -> {
            language = Constants.LANGUAGE_ENG_CODE;
            changeLanguage();
        });
        textFieldTestingToolName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                buttonStartPython.getOnAction().handle(null);
            }
        });
        repoName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                buttonStartPython.getOnAction().handle(null);
            }
        });
    }

}
