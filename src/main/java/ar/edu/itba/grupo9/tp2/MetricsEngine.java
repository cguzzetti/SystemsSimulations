package ar.edu.itba.grupo9.tp2;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.grupo9.tp2.ExperimentType.*;

public class MetricsEngine {
    private final BufferedWriter experimentWriter;
    private final ExperimentType experimentType;
    private List<Double> vaDataAcumulator;
    private Integer timeLapse;
    private Integer N;
    private Double L;
    private Double eta;

    public MetricsEngine(BufferedWriter writer, ExperimentType type) throws IOException {
        this.experimentWriter = writer;
        this.vaDataAcumulator = new ArrayList<>();
        this.experimentType = type;
    }

    public void printStatisticsToExperimentFile(VaOutput vaOutput) throws IOException {
        if(experimentType == NONE)
            return;

        this.N = vaOutput.getN();
        this.L = vaOutput.getL();
        this.eta = vaOutput.getEta();
        this.vaDataAcumulator = vaOutput.getEtaValues();

        if(experimentType == NOISE){
            this.vaDataAcumulator = this.vaDataAcumulator.subList(1800,this.vaDataAcumulator.size());
        }
        else if(experimentType == DENSITY) {
            this.vaDataAcumulator = this.vaDataAcumulator.subList(300,this.vaDataAcumulator.size());
        }

        System.out.println(this.N + ": " + this.vaDataAcumulator.size() + ": " +this.vaDataAcumulator);

        double average = this.vaDataAcumulator.stream().mapToDouble(Double::doubleValue).average().orElse(-1);
        if(average == -1){
            System.out.println("Oops, average == -1");
            return;
        }

        double variance = this.vaDataAcumulator.stream().map(i -> i - average).map(i -> i*i).mapToDouble(Double::doubleValue).average().orElse(-1);

        if(average == -1){
            System.out.println("Oops, variance == -1");
            return;
        }
        if(experimentType == NOISE){
            this.experimentWriter.write(String.format(
                    "%.2f %.3f %.3f\n", this.eta, average, Math.sqrt(variance)
            ));
        }else if(experimentType == DENSITY){
            this.experimentWriter.write(String.format(
                    "%.2f %.3f %.3f\n", this.N/(this.L*this.L), average, Math.sqrt(variance)
            ));
        }
    }
}
