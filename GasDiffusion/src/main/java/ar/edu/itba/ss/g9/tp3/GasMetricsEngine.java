package ar.edu.itba.ss.g9.tp3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class GasMetricsEngine {
    public static Logger logger = LoggerFactory.getLogger(GasMetricsEngine.class);
    private int currentIteration;
    private final ExperimentType type;
    private BufferedWriter writer;

    GasMetricsEngine(ExperimentType type, String filename){
        this.currentIteration = 0;
        this.type = type;
        try {
            this.writer = new BufferedWriter(new FileWriter(filename));
        }catch (IOException ex){
            logger.error(String.format(
                    "There was an error while creating the experiment file writer (%s)",
                    ex.getMessage())
            );
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void writeIterationHeader(){
        try{
            this.writer.write(String.format("%d\n", currentIteration));
            this.currentIteration++;
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void writeFP(double fp, double time){
        try{
            this.writer.write(String.format(
                    "%f %f\n", fp, time
            ));
        }catch (IOException ex){
            logger.error(String.format(
                    "There was an error while writing FP to experiment file (%s)",
                    ex.getMessage()
            ));
            ex.printStackTrace();
        }
    }

    public void writeGas(double pressure, double energy) {
        try{
            this.writer.write(String.format(
                    "%f %f\n", pressure, energy
            ));
        }catch (IOException ex){
            logger.error(String.format(
                    "There was an error while writing GAS to experiment file (%s)",
                    ex.getMessage()
            ));
            ex.printStackTrace();
        }
    }

    public void finalizeExperiment(){
        try {
            this.writer.close();
        }catch (IOException ex){
            logger.error(String.format(
                    "There was an error while closing the experiment file writer (%s)",
                    ex.getMessage())
            );
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public ExperimentType getType() {
        return type;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }
}
