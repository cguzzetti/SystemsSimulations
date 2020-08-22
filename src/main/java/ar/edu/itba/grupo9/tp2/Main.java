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


public class Main {

    private static Config parseCLIArguments(String[] args){
        Options options = new Options();
        Config config = null;
        try{
            config = Config.parseArguments(args, options);
        }catch(ParseException e){
            System.out.println(e.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CIMSimulator", options);
            System.exit(1);
        }catch(IllegalArgumentException e){
            System.out.println(String.format("Error: %s", e.getMessage()));
            System.exit(1);
        }

        return config;

    }

    private static LatticeInput parseInputFile(Config config){
        try {
            LatticeInput input = FileParser.readLatticeInput(config.getInputFileName());
            System.out.println(input.getParticles());
           return input;

        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }
    public static void main(String[] args){
        Config config = parseCLIArguments(args);

        LatticeInput input = parseInputFile(config);


        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i < 10; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, 10),
                    rnd.nextDouble(0, 10),
                    0,
                    i,
                    String.format("p%d", i)
            ));
        }

        OffLatticeAutomata ola = new OffLatticeAutomata(5,10,(double) 10,5, particles,1,0,0);

    }
}
