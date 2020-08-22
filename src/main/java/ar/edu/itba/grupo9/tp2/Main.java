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

        ArrayList<Particle> particles = new ArrayList<>();

        int N= 100;
        int L = 20;
        int M = 13;
        for(int i =0; i < N; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, L),
                    rnd.nextDouble(0, L),
                    0.03,
                    rnd.nextDouble(0, 2*Math.PI),
                    0,
                    i
            ));
        }

        OffLatticeAutomata ola = new OffLatticeAutomata(5, 2, 1, N,(double) L, M, particles,1,0,0);

    }
}
