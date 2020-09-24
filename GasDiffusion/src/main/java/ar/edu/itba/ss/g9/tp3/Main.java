package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.GasParticle;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.commons.simulation.ParticleGeneration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static ar.edu.itba.ss.g9.tp3.GasDiffusionFileParser.getFilePath;

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

        boolean gasExperiment = true;
        if(gasExperiment) {
            GasMetricsEngine metricsEngineGAS = new GasMetricsEngine(
                    ExperimentType.GAS, getFilePath("gasExperiment.txt")
            );

            for(double speed = 0.01; speed < 0.03 ; speed += 0.002) {
                GasDiffusionFileParser parser = null;
                config.setParticleSpeed(speed);

                try{
                    parser = new GasDiffusionFileParser(getFilePath(config.getInputFileName()), getFilePath(config.getOutputFileName()));
                }catch (IOException ex){
                    logger.error("There was an error while creating the FileParser. %s\n", ex.getMessage());
                    ex.printStackTrace();
                    System.exit(1);
                }

                boolean readingFromFile = false;
                Set<GasParticle> particles;
                if (!readingFromFile) {
                    particles = ParticleGeneration.generateGasParticles(
                            config.getN(),
                            config.getHeight(),
                            config.getWidth(),
                            config.getParticleRadius(),
                            config.getParticleMass(),
                            config.getparticleSpeed()
                    );
                    parser.saveInitialState(particles);
                } else {
                    particles = parser.readParticlesFromFile();
                }

                GasDiffusion gas = new GasDiffusion(config, particles);
                gas.setMetricsEngineGAS(metricsEngineGAS);
                gas.simulate(parser);
            }

            metricsEngineGAS.finalizeExperiment();
        }
        else {
            GasDiffusionFileParser parser = null;

            try{
                parser = new GasDiffusionFileParser(getFilePath(config.getInputFileName()), getFilePath(config.getOutputFileName()));
            }catch (IOException ex){
                logger.error("There was an error while creating the FileParser. %s\n", ex.getMessage());
                ex.printStackTrace();
                System.exit(1);
            }

            boolean readingFromFile = false;
            Set<GasParticle> particles;
            if (!readingFromFile) {
                particles = ParticleGeneration.generateGasParticles(
                        config.getN(),
                        config.getHeight(),
                        config.getWidth(),
                        config.getParticleRadius(),
                        config.getParticleMass(),
                        config.getparticleSpeed()
                );
                parser.saveInitialState(particles);
            } else {
                particles = parser.readParticlesFromFile();
            }

            GasDiffusion gas = new GasDiffusion(config, particles);
            gas.simulate(parser);
        }
   }
}
