package com.fragilityanalysis.data;

import javafx.scene.text.Font;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

import java.awt.*;
import java.text.DecimalFormat;

public interface Constants {

    Integer LANGUAGE_ITA_CODE = 0;
    Integer LANGUAGE_ENG_CODE = 1;

    Integer CODE_NTC = 1;
    Integer CODE_TTL = 2;
    Integer CODE_TLR = 3;
    Integer CODE_MTLR = 4;
    Integer CODE_MRTL = 5;
    Integer CODE_MCR = 6;
    Integer CODE_MMR = 7;
    Integer CODE_MCMMR = 8;
    Integer CODE_CC = 9;
    Integer CODE_CODE_SMELLS = 11;
    Integer CODE_TD = 12;
    Integer CODE_DEBT_RATIO = 13;
    Integer CODE_CLOC = 14;
    Integer CODE_LOC = 15;
    Integer CODE_NOC = 16;
    Integer CODE_NOF = 17;
    Integer CODE_NOM = 18;
    Integer CODE_STAT = 19;
    String TEXT_AVERAGE = "avg";
    String TEXT_VARIANCE = "variance";
    String TEXT_STANDARD_DEVIATION = "STDev";
    String TEXT_MAX = "max";
    String TEXT_MIN = "min";
    DecimalFormat df = new DecimalFormat("0.00");
    Font STD_TOOLTIP_FONT = new Font(16);

    enum TOOLTIP_NTC {
        ITA {
            public String toString() {
                return """
                        (NTC) è definito come il numero di classi presenti in una
                        versione di un progetto, con codice relativo a uno strumento specifico (Le
                        classi sono associate a un determinato strumento di test se contengono
                        importazioni o chiamate di metodo specifiche per lo strumento).""";
            }
        },
        ENG {
            public String toString() {
                return "Number of tool classes (NTC) \nis the number of classes featured by a release of a project, \nfeaturing code relative to a specific tool.";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_TTL {
        ITA {
            public String toString() {
                return """
                        è il numero di righe di codice appartenenti a
                        classi che possono essere attribuite a uno specifico strumento di test in
                        una versione di un progetto.""";
            }
        },
        ENG {
            public String toString() {
                return "Total tool LOCs (TTL) \n is the number of lines of code belonging to classes that can be attributed to a specific testing tool \nin a release of a project.";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_TLR {
        ITA {
            public String toString() {
                return """
                        (TLR) è la quantità totale di Linee di Codice di produzione per la
                        release i. Questa metrica, che si trova nell’intervallo [0, 1], ci consente
                        di quantificare la rilevanza del codice di test associato a uno strumento
                        specifico.""";
            }
        },
        ENG {
            public String toString() {
                return "Tool LOCs ratio (TLR)\nis defined as the ratio between TTL for release i and the total amount of production LOCs in the same release. \nThis metric, lying in the [0, 1] interval, allows us to quantify\n the relevance of the testing code associated with a specific tool.";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_MTLR {
        ITA {
            public String toString() {
                return """
                        (MTLR) è la quantità di LOC aggiunte, eliminate o modificate in
                        classi che possono essere associate a uno strumento specifico tra le versioni
                        con tag i - 1 e i. Questo dà un valore alla quantità di modifiche eseguite
                        su LOC esistenti che possono essere associate a un determinato strumento
                        per una versione specifica di un progetto. Un valore superiore a 1 di questa
                        metrica significa che più righe vengono aggiunte, modificate o rimosse
                        nelle classi di test nella transizione tra due versioni con tag consecutive
                        rispetto al numero di righe già presenti da esse.""";
            }
        },
        ENG {
            public String toString() {
                return """
                        (MTLR) Modified tool LOCs ratio\s
                        is defined as the ratio between Tdiffiᵢ and Tlocsᵢ₋₁
                        where Tdiffᵢ is the amount of added, deleted, or modified LOCs in classes that can be associated with a specific tool between tagged releases i − 1 and i.\s
                        This quantifies the amount of changes performed on existing LOCs that can be associated with a given tool for a specific release of a project.
                        A value higher than 1 of this metric means that more lines are added, modified, or removed in test classes in the transition between two consecutive tagged releases\s
                        than the number of lines already featured by them.""";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_MRTL {
        ITA {
            public String toString() {
                return """
                        (MRTL) è il rapporto tra la quantità di strumenti e LOC di produzione aggiunte, cancellate o modificate nella transizione tra le
                        versioni i - 1 e i. Viene calcolato solo per le versioni con codice associato a un determinato strumento di test. Questa metrica si trova nell’intervallo
                        [0, 1], valori vicini a 1 implicano che una parte significativa del tasso di abbandono totale del codice durante l’evoluzione dell’applicazione è
                        necessaria per mantenere aggiornati i test case scritti con uno strumento specifico.""";
            }
        },
        ENG {
            public String toString() {
                return """
                        MRTL (Modified Relative Test LOCs)
                         is defined as the ratio between  Tdiffᵢ and  Pdiffᵢ
                        where Tdiffᵢ and Pdiffᵢ are, respectively, the amount of added, deleted, or modified tool and production LOCs in the transition between releases i − 1 and i.
                        It is computed only for releases featuring code associated with a given testing tool (i.e., TRLi > 0).
                        This metric lies in the [0, 1] range, and values close to 1 imply that a significant portion of the total code churn during the evolution of the application
                        is needed to keep the test cases written with a specific tool up to date.""";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_MCR {
        ITA {
            public String toString() {
                return """
                        (MCR) è il rapporto tra il numero di classi associate a un dato strumento di test che
                        vengono modificate nella transizione tra le versioni i-1 e i ed il numero di classi associate allo strumento nella versione i-1.
                        La metrica si trova nell’intervallo [0, 1]: più grandi sono i valori di MCR, meno le classi sono stabili durante l’evoluzione dell’app.""";
            }
        },
        ENG {
            public String toString() {
                return """
                        Modified tool Classes Ratio (MCR)
                        is defined as the ratio between MCᵢ and NTCᵢ₋₁
                        where MCᵢ is the number of classes associated with a given testing tool that are modified in the transition between releases i − 1 and i,
                        and NTCᵢ₋₁ is the number of classes associated with the tool in release i − 1 (the metric is not defined when NTCᵢ₋₁ = 0).
                        The metric lies in the [0, 1] range: the larger the values of MCR, the less the classes are stable during the evolution of the app.""";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_MMR {
        ITA {
            public String toString() {
                return """
                        (MMR) è il rapporto tra il numero di metodi in classi associate a un dato strumento
                        che vengono modificati tra le versioni i-1 e i, e il numero totale di
                        metodi nelle classi associate allo strumento nella versione i-1.
                        La metrica si trova nell’intervallo [0, 1]: maggiori sono i valori di MMR, meno i metodi sono stabili durante
                        l’evoluzione dell’app che testano.""";
            }
        },
        ENG {
            public String toString() {
                return """
                        Modified tool Methods Ratio (MMR)
                        is defined as the ratio between MMᵢ and TMᵢ₋₁
                        where MMᵢ is the number of methods in classes associated with a given tool that are modified between releases i − 1 and i,
                        and TMᵢ₋₁ is the total number of methods in classes associated with the tool in release i − 1 (the metric is not defined when TMᵢ₋₁ = 0).
                        The metric lies in the [0, 1] range: the larger the values of MMR, the less the methods are stable during the evolution of the app they test.""";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_MCMMR {
        ITA {
            public String toString() {
                return """
                        (MCMMR) è calcolato come il rapporto tra il numero di classi associate a un dato strumento di test
                        che vengono modificate e che presentano almeno un metodo modificato tra le release i-1 e 1
                        e la quantità totale di versioni con tag con classi di test associate a tale
                        strumento. Questa metrica si trova nell’intervallo [0, 1].""";
            }
        },
        ENG {
            public String toString() {
                return """
                        MCMMR (Modified Classes with Modified Methods Ratio)
                        is defined as the ratio between MCMMᵢ and NTCᵢ₋₁
                        where MCMMᵢ is the number of classes associated with a given testing tool that are modified, and that feature at least one modified method between releases i − 1 and 1.
                        This metric represents an estimate of the percentage of fragile classes associated with the tool upon the entire set of test classes featured by a tagged release of the project.""";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_CC {
        ITA {
            public String toString() {
                return "La complessità ciclomatica, o complessità condizionale, è una metrica software utilizzata per misurare la complessità di un programma.\n" +
                        "Misura direttamente il numero di cammini linearmente indipendenti attraverso il grafo di controllo di flusso.";
            }
        },
        ENG {
            public String toString() {
                return """
                        CC (McCabe’s Cyclomatic Complexity)
                        Cyclomatic complexity is a software metric used to indicate the complexity of a program. It is a quantitative measure of the number of linearly independent paths through a program's source code.
                        Cyclomatic complexity is computed using the control-flow graph of the program:
                        the nodes of the graph correspond to indivisible groups of commands of a program, and a directed edge connects two nodes if the second command might be executed immediately after the first command.
                        """;
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_CODE_SMELL {
        ITA {
            public String toString() {
                return """
                        L'espressione code smell\s
                        viene usata per indicare una serie di caratteristiche che il codice sorgente può avere e che sono generalmente riconosciute
                        come probabili indicazioni di un difetto di programmazione. I code smell non sono bug, cioè veri e propri errori, bensì debolezze
                        di progettazione che riducono la qualità del software, a prescindere dall'effettiva correttezza del suo funzionamento.
                        Il code smell spesso è correlato alla presenza di debito tecnico e la sua individuazione è un comune metodo euristico usato dai programmatori come guida per l'attività di refactoring,
                        ovvero l'esecuzione di azioni di ristrutturazione del codice volte a migliorarne la struttura, abbassandone la complessità senza modificarne le funzionalità.""";
            }
        },
        ENG {
            public String toString() {
                return "In computer programming, a code smell is any characteristic in the source code of a program that possibly indicates a deeper problem.\n";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_TD {
        ITA {
            public String toString() {
                return "Il debito tecnico è una metrica per descrivere le possibili complicazioni che subentrano in un progetto,\n" +
                        "tipicamente di sviluppo software, qualora non venissero adottate adeguate azioni volte a mantenerne bassa la complessità.";
            }
        },
        ENG {
            public String toString() {
                return "TD (Technical Debt)\n" +
                        "Technical debt is a concept that reflects the implied cost of additional rework caused by choosing an easy (limited) solution now instead of using a better approach that would take longer.";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_DEBT_RATIO {
        ITA {
            public String toString() {
                return "TDR (Technical Debt Ratio) è il rapporto tra il costo di riparazione e il costo di sviluppo.\n" +
                        "In genere nessuno vuole un Technical Debt Ratio elevato, alcuni team preferiscono valori inferiori o uguali al 5%. I punteggi TDR elevati riflettono il software che è in uno stato di qualità davvero scadente.";
            }
        },
        ENG {
            public String toString() {
                return """
                        TDR (Technical Debt Ratio)
                        is the ratio of remediation cost to development cost.
                        Generally, no one wants a high Technical Debt Ratio, some teams favour values less than or equal to 5%. High TDR scores reflect software that’s in a really poor state of quality.
                        """;
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_CLOC {
        ITA {
            public String toString() {
                return "Numero di linee di commento.";
            }
        },
        ENG {
            public String toString() {
                return "Total Number of Commented Lines of Code in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_LOC {
        ITA {
            public String toString() {
                return "Numero di linee di codice.";
            }
        },
        ENG {
            public String toString() {
                return "Total Number of Lines of Code in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_NOC {
        ITA {
            public String toString() {
                return "Numero di classi";
            }
        },
        ENG {
            public String toString() {
                return "Total Number of Classes in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_NOF {
        ITA {
            public String toString() {
                return "Numero di File";
            }
        },
        ENG {
            public String toString() {
                return "Total Number Files in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_NOM {
        ITA {
            public String toString() {
                return "Numero di metodi";
            }
        },
        ENG {
            public String toString() {
                return "Total Number of Methods in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TOOLTIP_STAT {
        ITA {
            public String toString() {
                return "Numero di dichiarazioni.";
            }
        },
        ENG {
            public String toString() {
                return "Total Number of Statements in the Project";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum NAME_EXT_NTC {
        ITA {
            public String toString() {
                return "NTC (Numero di Classi di Test)";
            }
        },
        ENG {
            public String toString() {
                return "NTC (Number of Test Classes)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_TTL {
        ITA {
            public String toString() {
                return "TTL (Numero Totale di LOC di test)";
            }
        },
        ENG {
            public String toString() {
                return "TTL (Total Test LOC)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_TLR {
        ITA {
            public String toString() {
                return "TLR (Rapporto Total LOC)";
            }
        },
        ENG {
            public String toString() {
                return "TLR (Total LOCs Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_MTLR {
        ITA {
            public String toString() {
                return "MTLR (LOC Test modificate/LOC Test totali)";
            }
        },
        ENG {
            public String toString() {
                return "MTLR (Modified Test LOCs Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_MRTL {
        ITA {
            public String toString() {
                return "MRTL (LOC di Test Modificate/Totali)";
            }
        },
        ENG {
            public String toString() {
                return "MRTL (Modified Relative Test LOCs)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_MCR {
        ITA {
            public String toString() {
                return "MCR (Rapporto Classi Test Modificate/Classi Test Totali)";
            }
        },
        ENG {
            public String toString() {
                return "MCR (Modified Test Classes Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_MMR {
        ITA {
            public String toString() {
                return "MMR (Metodi di Test Modificati/Totale)";
            }
        },
        ENG {
            public String toString() {
                return "MMR (Modified Test Methods Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_MCMMR {
        ITA {
            public String toString() {
                return "MCMMR (Rapporto Classi con Metodi Modificati/Totale)";
            }
        },
        ENG {
            public String toString() {
                return "MCMMR (Modified Classes with Modified Methods Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_CC {
        ITA {
            public String toString() {
                return "CC (Complessità Ciclomatica di McCabe)";
            }
        },
        ENG {
            public String toString() {
                return "CC (McCabe's Cyclomatic Complexity)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_CODE_SMELL {
        ITA {
            public String toString() {
                return "CODE SMELLS";
            }
        },
        ENG {
            public String toString() {
                return "CODE SMELLS";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_TD {
        ITA {
            public String toString() {
                return "TD (Debito Tecnico)";
            }
        },
        ENG {
            public String toString() {
                return "TD (Technical Debt)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_DEBT_RATIO {
        ITA {
            public String toString() {
                return "DEBT RATIO (Rapporto Debito Tecnico)";
            }
        },
        ENG {
            public String toString() {
                return "DEBT RATIO";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_CLOC {
        ITA {
            public String toString() {
                return "CLOC (Linee di Commenti)";
            }
        },
        ENG {
            public String toString() {
                return "CLOC (Comment Lines of Code)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_LOC {
        ITA {
            public String toString() {
                return "LOC (Linee di Codice)";
            }
        },
        ENG {
            public String toString() {
                return "LOC (Lines of Code)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_NOC {
        ITA {
            public String toString() {
                return "NOC (Numero di Classi)";
            }
        },
        ENG {
            public String toString() {
                return "NOC (Number of Classes)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_NOF {
        ITA {
            public String toString() {
                return "NOF (Numero di File)";
            }
        },
        ENG {
            public String toString() {
                return "NOF (Number of Files)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_NOM {
        ITA {
            public String toString() {
                return "NOM (Numero di Metodi)";
            }
        },
        ENG {
            public String toString() {
                return "NOM (Number of Methods)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum NAME_EXT_STAT {
        ITA {
            public String toString() {
                return "STAT (Numero di Dichiarazioni)";
            }
        },
        ENG {
            public String toString() {
                return "STAT (Number of Statements)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum BUTTON_NAME_FINDER {
        ITA {
            public String toString() {
                return "Apri Finder per selezionare il file";
            }
        },
        ENG {
            public String toString() {
                return "Open finder to select file";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum BUTTON_NAME_START_PYTHON {
        ITA {
            public String toString() {
                return "Avvia l'analisi del repository inserito";
            }
        },
        ENG {
            public String toString() {
                return "Start Repository analysis";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum BUTTON_NAME_PLOT_GRAPHS {
        ITA {
            public String toString() {
                return "Visualizza i grafici selezionati";
            }
        },
        ENG {
            public String toString() {
                return "Show selected graphs";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum BUTTON_NAME_PLOT_BOX_PLOT {
        ITA {
            public String toString() {
                return "Visualizza il BoxPlot";
            }
        },
        ENG {
            public String toString() {
                return "Show BoxPlot";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum BUTTON_NAME_CHANGES {
        ITA {
            public String toString() {
                return "Visualizza i cambiamenti di ogni classe";
            }
        },
        ENG {
            public String toString() {
                return "Show changes of classes";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum TABLE_NAME_VAR {
        ITA {
            public String toString() {
                return "Metrica";
            }
        },
        ENG {
            public String toString() {
                return "Metric";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TABLE_NAME_MEDIA {
        ITA {
            public String toString() {
                return "Media";
            }
        },
        ENG {
            public String toString() {
                return "Average";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TABLE_NAME_VARIANZA {
        ITA {
            public String toString() {
                return "Varianza";
            }
        },
        ENG {
            public String toString() {
                return "Variance";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TABLE_NAME_SCARTO {
        ITA {
            public String toString() {
                return "Deviazione Standard";
            }
        },
        ENG {
            public String toString() {
                return "Standard Deviation";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TABLE_NAME_MAX {
        ITA {
            public String toString() {
                return "Valore Massimo";
            }
        },
        ENG {
            public String toString() {
                return "Max Value";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TABLE_NAME_MIN {
        ITA {
            public String toString() {
                return "Valore Minimo";
            }
        },
        ENG {
            public String toString() {
                return "Min Value";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum TAB_NAME_INPUT {
        ITA {
            public String toString() {
                return "Selezione Input";
            }
        },
        ENG {
            public String toString() {
                return "Input Selection";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TAB_NAME_EXECUTING {
        ITA {
            public String toString() {
                return "Esecuzione Script";
            }
        },
        ENG {
            public String toString() {
                return "Script Execution";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TAB_NAME_STRUMENTO {
        ITA {
            public String toString() {
                return "Strumento Analisi Fragilità";
            }
        },
        ENG {
            public String toString() {
                return "Fragility Tools";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum TAB_NAME_STAT {
        ITA {
            public String toString() {
                return "Statistiche";
            }
        },
        ENG {
            public String toString() {
                return "Statistics";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum LABEL_TEXT_STARTING_PYTHON {
        ITA {
            public String toString() {
                return "Avviando lo script";
            }
        },
        ENG {
            public String toString() {
                return "Starting Script";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_TEXT_TESTING_TOOL_NAME {
        ITA {
            public String toString() {
                return "Nome Strumento Testing: ";
            }
        },
        ENG {
            public String toString() {
                return "Testing Tool Name: ";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_TEXT_SEARCHING_RELEASES {
        ITA {
            public String toString() {
                return "Cercando il numero di rlease";
            }
        },
        ENG {
            public String toString() {
                return "Searching the number of releases";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_TEXT_COMPUTING_0 {
        ITA {
            public String toString() {
                return "Analizzando la versione ";
            }
        },
        ENG {
            public String toString() {
                return "Analyzing version ";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_TEXT_COMPUTING_1 {
        ITA {
            public String toString() {
                return " con la ";
            }
        },
        ENG {
            public String toString() {
                return " to ";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_PYTHON_LAST_X_RELEASES {
        ITA {
            public String toString() {
                return "Inserire quante ultime release: ";
            }
        },
        ENG {
            public String toString() {
                return "Insert how many last releases: ";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum LABEL_CODE_METRICS {
        ITA {
            public String toString() {
                return "Metriche Codice";
            }
        },
        ENG {
            public String toString() {
                return "Code Metrics";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_TEST_METRICS {
        ITA {
            public String toString() {
                return "Metriche dei Test";
            }
        },
        ENG {
            public String toString() {
                return "Test Metrics";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_RATIO_METRICS {
        ITA {
            public String toString() {
                return "Metriche dei Rapporti";
            }
        },
        ENG {
            public String toString() {
                return "Ratio Metrics";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_FRAGILITY_METRICS {
        ITA {
            public String toString() {
                return "Metriche di Fragilita";
            }
        },
        ENG {
            public String toString() {
                return "Fragility Metrics";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum LABEL_TSV {
        ITA {
            public String toString() {
                return "TSV (Volatilità della Suite di Test)";
            }
        },
        ENG {
            public String toString() {
                return "TSV (Test Suite Volatility)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_NTR {
        ITA {
            public String toString() {
                return "NTR (Numero Di Release)";
            }
        },
        ENG {
            public String toString() {
                return "NTR (Number of Tagged Releases)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum LABEL_MRR {
        ITA {
            public String toString() {
                return "MRR (Rapporto Numero Modifiche)";
            }
        },
        ENG {
            public String toString() {
                return "MRR (Modified Releases Ratio)";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum TITLE_FRAGILITY {
        ITA {
            public String toString() {
                return "Strumento Analisi Fragilità";
            }
        },
        ENG {
            public String toString() {
                return "Fragility Tool";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum ERROR_REPOSITORY_WRONG_NAME {
        ITA {
            public String toString() {
                return "Repository Inesistente";
            }
        },
        ENG {
            public String toString() {
                return "Repository not Found";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum ERROR_REPOSITORY_NAME_EMPTY {
        ITA {
            public String toString() {
                return "Inserire il nome del repository";
            }
        },
        ENG {
            public String toString() {
                return "Insert Repository name";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum ERROR_REPOSITORY_NAME_EMPTY_DESCRIPTION {
        ITA {
            public String toString() {
                return "Formato: GitHubUsername/NomeRepository";
            }
        },
        ENG {
            public String toString() {
                return "Format: GitHubUsername/RepositoryName";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum ERROR_FILE_CORRUPTED {
        ITA {
            public String toString() {
                return "Input File Generato Corrotto";
            }
        },
        ENG {
            public String toString() {
                return "Corrupted Input File";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum ERROR_FILE_LOADING {
        ITA {
            public String toString() {
                return "Errore nel caricamento dei dati dal file";
            }
        },
        ENG {
            public String toString() {
                return "File Loading problem";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }
    enum ERROR_ALERT_TITLE {
        ITA {
            public String toString() {
                return "Errore";
            }
        },
        ENG {
            public String toString() {
                return "Error";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    enum BUTTON_CHANGE_LANGUAGE {
        ITA {
            public String toString() {
                return "Lingua";
            }
        },
        ENG {
            public String toString() {
                return "Language";
            }
        };

        public static String getLanguage(Integer x) {
            if (x == 0) {
                return ITA.toString();
            } else {
                return ENG.toString();
            }
        }
    }

    /**
     * Function that given the index as Integer, returns the string with the advanced ame of the metric.2
     *
     * @param code,language index of the metric and language desired
     * @return the name of the metric
     */
    static String getNameOnIndex(Integer code, Integer language) {
        return switch (code) {
            case 1 -> NAME_EXT_NTC.getLanguage(language);
            case 2 -> NAME_EXT_TTL.getLanguage(language);
            case 3 -> NAME_EXT_TLR.getLanguage(language);
            case 4 -> NAME_EXT_MTLR.getLanguage(language);
            case 5 -> NAME_EXT_MRTL.getLanguage(language);
            case 6 -> NAME_EXT_MCR.getLanguage(language);
            case 7 -> NAME_EXT_MMR.getLanguage(language);
            case 8 -> NAME_EXT_MCMMR.getLanguage(language);
            case 9 -> NAME_EXT_CC.getLanguage(language);
            case 11 -> NAME_EXT_CODE_SMELL.getLanguage(language);
            case 12 -> NAME_EXT_TD.getLanguage(language);
            case 13 -> NAME_EXT_DEBT_RATIO.getLanguage(language);
            case 14 -> NAME_EXT_CLOC.getLanguage(language);
            case 15 -> NAME_EXT_LOC.getLanguage(language);
            case 16 -> NAME_EXT_NOC.getLanguage(language);
            case 17 -> NAME_EXT_NOF.getLanguage(language);
            case 18 -> NAME_EXT_NOM.getLanguage(language);
            case 19 -> NAME_EXT_STAT.getLanguage(language);
            default -> "";
        };
    }

    static void setupChartPlot(JFreeChart chart, XYPlot plot, XYLineAndShapeRenderer renderer) {
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        plot.setRenderer(renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        chart.getLegend().setFrame(BlockBorder.NONE);
    }

}
