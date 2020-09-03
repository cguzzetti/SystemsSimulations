package ar.edu.itba.ss.g9.tp2;

import java.util.LinkedList;
import java.util.List;

public class VaOutput {
    private Integer timeLapse;
    private Integer N;
    private Double L;
    private Double eta;
    private List<Double> etaValues;

    public VaOutput() {
        this.etaValues = new LinkedList<>();
    }

    public Double getL() {
        return L;
    }

    public Double getEta() {
        return eta;
    }

    public Integer getN() {
        return N;
    }

    public Integer getTimeLapse() {
        return timeLapse;
    }

    public List<Double> getEtaValues() {
        return etaValues;
    }

    public void setEta(Double eta) {
        this.eta = eta;
    }

    public void setL(Double l) {
        L = l;
    }

    public void setN(Integer n) {
        N = n;
    }

    public void setTimeLapse(Integer timeLapse) {
        this.timeLapse = timeLapse;
    }

    public void addEtaValue(Double eta) {
        etaValues.add(eta);
    }
}
