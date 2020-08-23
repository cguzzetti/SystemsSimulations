package ar.edu.itba.grupo9.tp2;


import ar.edu.itba.grupo9.tp1.Particle;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import ar.edu.itba.grupo9.tp1.util.Config;
import ar.edu.itba.grupo9.tp1.util.files.FileParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.util.stream.Collectors;

import static ar.edu.itba.grupo9.tp1.util.files.FileParser.createLatticeExperimentFile;


public class Main {

    private static Config parseCLIArguments(String[] args){
        Options options = new Options();
        try{
            return Config.parseArguments(args, options);
        }catch(ParseException e){
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CIMSimulator", options);
            return null;
        }catch(IllegalArgumentException e){
            System.out.println(String.format("Error: %s", e.getMessage()));
            return null;
        }

    }

    private static LatticeInput parseInputFile(Config config){
        try {
            return FileParser.readLatticeInput(config.getInputFileName());

        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args){
        Config config = parseCLIArguments(args);
        if(config == null) System.exit(1);
        if(config.isExperiment()) createLatticeExperimentFile(config);

        LatticeInput input = parseInputFile(config);
        if(input == null) System.exit(1);


        int N = input.getNumberOfParticles();


        ArrayList<Particle> particles = input.getParticles();
        OffLatticeAutomata ola = new OffLatticeAutomata(
                250,
                2,
                1,
                N,
                (double)input.getAreaSideLength(),
                input.getOptimalM(config, input),
                particles,
                config.getRc(),
                input.getFirstMaxRadius(),
                input.getSecondMaxRadius(),
                config,
                input);


    }
}
