package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.FileParser;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;


public class GasDiffusionFileParser extends FileParser {
    private static Logger logger = LoggerFactory.getLogger(GasDiffusionFileParser.class);

    private final BufferedWriter outputWriter;
    public GasDiffusionFileParser(String inputFilename) throws IOException{
        this.outputWriter = new BufferedWriter(new FileWriter(new File(inputFilename)));
    }

    public void writeStateToOutput(Collection<Particle> particles, int iteration){
        try {
            outputWriter.write(String.format("%d\n", iteration));
        }catch (IOException ex){
            logger.error("There was an error while writing the t for the file. %s\n", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

        // Might want to see [this](https://www.baeldung.com/java-lambda-exceptions) for this part
        particles.forEach(particle -> {
            try {
                outputWriter.write(String.format("%s\n", particle.toString()));
            }catch (IOException ex){
                logger.error("There was an error while writing the particles to the file writer\n" +
                        " Particle: %s. %s", particle.toString(), ex.getMessage());
                ex.printStackTrace();
                System.exit(1);
            }
        });
    }


    public void finish(){
        try{
            this.outputWriter.close();
        }catch (IOException ex){
            logger.error("There was an error while closing the fileWriter: %s", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

    }
}
