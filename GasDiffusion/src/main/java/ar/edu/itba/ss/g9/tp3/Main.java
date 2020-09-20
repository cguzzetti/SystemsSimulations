package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.commons.simulation.ParticleGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Set;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static String initializeVisualizationPath(){
        boolean rootIncluded = new File("GasDiffusion").exists();
        if(rootIncluded){
            return "GasDiffusion/src/main/visualization";
        }
        return "src/main/visualization";
    }
    public static String VISUALIZATION_PATH;
    public static void main( String[] args ) {
        VISUALIZATION_PATH = initializeVisualizationPath();
        Options options = new Options();
        GasDifussionConfig config = null;
        try {
            config = GasDifussionConfig.parseArguments(args, options);
        }catch (ParseException parseEx){
            System.out.println(parseEx.getMessage());
            HelpFormatter helper = new HelpFormatter();
            helper.printHelp("gasDifussion", options);
            System.exit(1);
        }catch (IllegalArgumentException ex){
            ex.printStackTrace();
            System.exit(1);
        }

        System.out.println(config.toString());

        Set<Particle> particles = ParticleGeneration.generateWeightedParticles(
                config.getN(),
                config.getHeight(),
                config.getWidth()
        );


        GasDiffusion gas = new GasDiffusion(config, particles);
        GasDiffusionFileParser parser = null;

        try{
            parser = new GasDiffusionFileParser(String.format("%s/%s", VISUALIZATION_PATH, config.getInputFileName()));
        }catch (IOException ex){
            logger.error("There was an error while creating the FileParser. %s\n", ex.getMessage());
            ex.printStackTrace();
            System.exit(1);
        }

//        gas.simulate(parser);
   }
}
