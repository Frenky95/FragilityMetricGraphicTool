package com.fragilityanalysis.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class LoadDataFromFile {

    private final AllVersionsData allVersionsData;

    public LoadDataFromFile() {
        this.allVersionsData = new AllVersionsData();
    }

    /**
     * Method that managed the load of metrics from the file given as input
     * @param fileName path of the file to read
     * @return the AllVersionsData object that stores all the data of all the versions
     */
    public AllVersionsData load(String fileName) {
        try {
            String line;
            List<String> splitLine;
            int count = 0;
            Scanner sc = new Scanner(new File(fileName));
            sc.useDelimiter("\n");
            while (sc.hasNext()) {
                line = sc.next();
                splitLine = Arrays.asList(line.split(","));
                //Skip the first line (that contains the metrics names
                if (count != 0) {
                    //Read all the metrics for each version
                    if (!(splitLine.size() == 1)) {
                        //If all the metrics are correctly present into the file, read the line and insert it into the version array
                        if (splitLine.size() == 20) {
                            SingleVersionData singleVersionData = new SingleVersionData(splitLine.get(0));
                            //System.out.println(singleVersionData.getVersionName());
                            singleVersionData.setNtc(Double.parseDouble(splitLine.get(1)));
                            singleVersionData.setTtl(Double.parseDouble(splitLine.get(2)));
                            singleVersionData.setTlr(Double.parseDouble(splitLine.get(3)));
                            singleVersionData.setMtrl(Double.parseDouble(splitLine.get(4)));
                            singleVersionData.setMrtl(Double.parseDouble(splitLine.get(5)));
                            singleVersionData.setMcr(Double.parseDouble(splitLine.get(6)));
                            singleVersionData.setMmr(Double.parseDouble(splitLine.get(7)));
                            singleVersionData.setMcmmr(Double.parseDouble(splitLine.get(8)));
                            singleVersionData.setCc(Double.parseDouble(splitLine.get(9)));
                            //Loads the changes list (if present)
                            if (!(splitLine.get(10) == null || splitLine.get(10).equals("") || splitLine.get(10).length() == 0)) {
                                String change = splitLine.get(10);
                                StringBuilder builder = new StringBuilder();
                                for (int j = 0; j < change.length(); j++) {
                                    //Skip the graphs (useless to store it)
                                    switch (change.charAt(j)) {
                                        case '{':
                                            while (change.charAt(j) != '}') {
                                                j++;
                                            }
                                            break;
                                        case '(':
                                            while (change.charAt(j) != ')') {
                                                j++;
                                            }
                                            break;
                                        default:
                                            while (change.charAt(j) != ' ') {
                                                if (change.charAt(j) == '{') {
                                                    while (change.charAt(j) != '}') {
                                                        j++;
                                                    }
                                                    j++;
                                                    if (change.charAt(j) == '/') {
                                                        j++;
                                                    }
                                                }
                                                builder.append(change.charAt(j));
                                                j++;
                                            }
                                            if (change.charAt(j - 1) == ')') {
                                                //If the change row has been correctly read, put it into the map with changes
                                                if (Integer.parseInt(builder.toString().split("\\(")[1].split("\\)")[0]) != 0)
                                                    singleVersionData.getChange().put(builder.toString().split("\\(")[0], Integer.parseInt(builder.toString().split("\\(")[1].split("\\)")[0]));
                                            }
                                            builder.setLength(0);
                                            break;
                                    }
                                }
                            }

                            singleVersionData.setCode_smells(Double.parseDouble(splitLine.get(11)));
                            singleVersionData.setTd(Double.parseDouble(splitLine.get(12)));
                            singleVersionData.setDebt_ratio(Double.parseDouble(splitLine.get(13)));
                            singleVersionData.setCloc(Double.parseDouble(splitLine.get(14)));
                            singleVersionData.setLoc(Double.parseDouble(splitLine.get(15)));
                            singleVersionData.setNoc(Double.parseDouble(splitLine.get(16)));
                            singleVersionData.setNof(Double.parseDouble(splitLine.get(17)));
                            singleVersionData.setNom(Double.parseDouble(splitLine.get(18)));
                            singleVersionData.setStat(Double.parseDouble(splitLine.get(19).trim()));
                            this.allVersionsData.getSingleVersionDataArray().add(singleVersionData);

                            //Update the changes map summing all the occurrences of the same class
                            singleVersionData.getChange().forEach((k, v) -> {
                                if (this.allVersionsData.getClassChanges().get(k) != null) {
                                    Integer changes = this.allVersionsData.getClassChanges().get(k);
                                    changes += v;
                                    this.allVersionsData.getClassChanges().replace(k, changes);
                                } else {
                                    this.allVersionsData.getClassChanges().put(k, v);
                                }
                            });
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                    //System.out.println("Loaded data of the release: [" + singleVersionData.getVersionName() + "]");
                }
                count++;
            }
            sc.close();
            if (count == 1 && allVersionsData.getSingleVersionDataArray() != null && allVersionsData.getSingleVersionDataArray().size() == 0) {
                return null;
            }
            count = 0;

            //Read the Volatility metrics from file "name_2.csv"
            String filename_volatility = fileName.split("\\.csv")[0].concat("_2.csv");
            if (new File(filename_volatility).exists()) {
                sc = new Scanner(new File(filename_volatility));
                sc.useDelimiter("\n");
                while (sc.hasNext()) {
                    line = sc.next();
                    splitLine = Arrays.asList(line.split(","));
                    if (count != 0) {
                        if (!(splitLine.size() == 1)) {
                            allVersionsData.setNTR(Integer.parseInt(splitLine.get(0)));
                            allVersionsData.setMRR(Double.parseDouble(splitLine.get(1)));
                            allVersionsData.setTSV(Double.parseDouble(splitLine.get(2)));
                            //System.out.println("NTR, MRR, TSV: " + allVersionData.getNTR() + ", " + allVersionData.getMRR() + ", " + allVersionData.getTSV());
                        }
                    }
                    count++;
                }
                sc.close();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return this.allVersionsData;
    }
}
