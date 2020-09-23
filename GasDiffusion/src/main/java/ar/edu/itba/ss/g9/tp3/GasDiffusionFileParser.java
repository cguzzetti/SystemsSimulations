package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.FileParser;
import ar.edu.itba.ss.g9.commons.simulation.GasParticle;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static ar.edu.itba.ss.g9.tp3.Main.VISUALIZATION_PATH;


public class GasDiffusionFileParser extends FileParser {
    private static Logger logger = LoggerFactory.getLogger(GasDiffusionFileParser.class);

    private final BufferedWriter outputWriter;
    private final BufferedWriter inputWriter;
    private final BufferedReader inputReader;
    public GasDiffusionFileParser(String inputFilename, String outputFilename) throws IOException{
        this.outputWriter = new BufferedWriter(new FileWriter(outputFilename));
        this.inputWriter = new BufferedWriter(new FileWriter(inputFilename));
        this.inputReader = new BufferedReader(new FileReader(String.format("%s/input_read.txt", VISUALIZATION_PATH)));
    }

    public void writeStateToOutput(Collection<GasParticle> particles, int iteration){
        try {
            outputWriter.write(String.format("%d\n%d\n",particles.size(), iteration));
        }catch (IOException ex){
            logger.error("There was an error while writing the t for the file. %s\n", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        // Might want to see [this](https://www.baeldung.com/java-lambda-exceptions) for this part
        printParticlesToWriter(particles, this.outputWriter);
    }

    public void saveInitialState(Collection<GasParticle> particles){
        printParticlesToWriter(particles, this.inputWriter);
    }

    private void printParticlesToWriter(Collection<GasParticle> particles, BufferedWriter writer){
        particles.forEach(particle -> {
            try {
                writer.write(String.format("%s\n", particle.toString()));
            }catch (IOException ex){
                logger.error("There was an error while writing the particles to the file writer\n" +
                        " Particle: %s %s", particle.toString(), ex.getMessage());
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }

    public Set<GasParticle> readParticlesFromFile(){
        Set<GasParticle> particles = new HashSet<>();
        this.inputReader.lines().forEach( line -> {
            String[] myLine = line.split(" ");
            particles.add(new GasParticle(
                    Double.parseDouble(myLine[1]), Double.parseDouble(myLine[2]), Double.parseDouble(myLine[3]),
                    Double.parseDouble(myLine[4]), Integer.parseInt(myLine[0]), Double.parseDouble(myLine[5]), Double.parseDouble(myLine[6]), Double.parseDouble(myLine[7])
            ));
        });

        return particles;
    }

    public static String getFilePath(String filename){
        return String.format("%s/%s", VISUALIZATION_PATH, filename);
    }


    public void finish(){
        try{
            this.outputWriter.close();
            this.inputWriter.close();
        }catch (IOException ex){
            logger.error("There was an error while closing the fileWriter: %s", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

    }

    public BufferedWriter getInputWriter() {
        return inputWriter;
    }

    public BufferedReader getInputReader() {
        return inputReader;
    }
}
