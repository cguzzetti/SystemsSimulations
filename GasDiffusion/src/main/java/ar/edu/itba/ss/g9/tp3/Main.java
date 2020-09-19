package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.commons.simulation.ParticleGeneration;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.util.Collection;

public class Main {
    public static String initializeVisualizationPath(){
        boolean rootIncluded = new File("GasDiffusion").exists();
        if(rootIncluded){
            return "GasDiffusion/src/main/visualization";
        }
        return "src/main/visualization";
    }

    public static void main( String[] args ) {
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

        Collection<Particle> particles = ParticleGeneration.generateWeightedParticles(
                config.getN(),
                config.getHeight(),
                config.getWidth()
        );

        particles.forEach(System.out::println);
   }
}
