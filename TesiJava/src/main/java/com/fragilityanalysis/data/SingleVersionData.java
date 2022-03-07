package com.fragilityanalysis.data;

import java.util.HashMap;
import java.util.Map;

public class SingleVersionData {

    private String versionName;
    private Double ntc;
    private Double ttl;
    private Double tlr;
    private Double mtrl;
    private Double mrtl;
    private Double mcr;
    private Double mmr;
    private Double mcmmr;
    private Double cc;
    private final Map<String, Integer> change;
    private Double code_smells;
    private Double td;
    private Double debt_ratio;
    private Double cloc;
    private Double loc;
    private Double noc;
    private Double nof;
    private Double nom;
    private Double stat;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public Double getNtc() {
        return ntc;
    }

    public void setNtc(Double ntc) {
        this.ntc = ntc;
    }

    public Double getTtl() {
        return ttl;
    }

    public void setTtl(Double ttl) {
        this.ttl = ttl;
    }

    public Double getTlr() {
        return tlr;
    }

    public void setTlr(Double tlr) {
        this.tlr = tlr;
    }

    public Double getMtlr() {
        return mtrl;
    }

    public void setMtrl(Double mtrl) {
        this.mtrl = mtrl;
    }

    public Double getMrtl() {
        return mrtl;
    }

    public void setMrtl(Double mrtl) {
        this.mrtl = mrtl;
    }

    public Double getMcr() {
        return mcr;
    }

    public void setMcr(Double mcr) {
        this.mcr = mcr;
    }

    public Double getMmr() {
        return mmr;
    }

    public void setMmr(Double mmr) {
        this.mmr = mmr;
    }

    public Double getMcmmr() {
        return mcmmr;
    }

    public void setMcmmr(Double mcmmr) {
        this.mcmmr = mcmmr;
    }

    public Double getCc() {
        return cc;
    }

    public void setCc(Double cc) {
        this.cc = cc;
    }

    public Map<String, Integer> getChange() {
        return change;
    }

    public Double getCode_smells() {
        return code_smells;
    }

    public void setCode_smells(Double code_smells) {
        this.code_smells = code_smells;
    }

    public Double getTd() {
        return td;
    }

    public void setTd(Double td) {
        this.td = td;
    }

    public Double getDebt_ratio() {
        return debt_ratio;
    }

    public void setDebt_ratio(Double debt_ratio) {
        this.debt_ratio = debt_ratio;
    }

    public Double getCloc() {
        return cloc;
    }

    public void setCloc(Double cloc) {
        this.cloc = cloc;
    }

    public Double getLoc() {
        return loc;
    }

    public void setLoc(Double loc) {
        this.loc = loc;
    }

    public Double getNoc() {
        return noc;
    }

    public void setNoc(Double noc) {
        this.noc = noc;
    }

    public Double getNof() {
        return nof;
    }

    public void setNof(Double nof) {
        this.nof = nof;
    }

    public Double getNom() {
        return nom;
    }

    public void setNom(Double nom) {
        this.nom = nom;
    }

    public Double getStat() {
        return stat;
    }

    public void setStat(Double stat) {
        this.stat = stat;
    }

    public SingleVersionData(String versionName) {
        this.versionName = versionName;
        this.change = new HashMap<>();
    }

}
