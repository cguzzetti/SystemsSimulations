package ar.edu.itba.grupo9.tp2;


import ar.edu.itba.grupo9.tp1.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import ar.edu.itba.grupo9.tp1.util.Config;
import ar.edu.itba.grupo9.tp1.util.files.FileParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

import static ar.edu.itba.grupo9.tp1.util.files.FileParser.createLatticeExperimentFile;
import static ar.edu.itba.grupo9.tp2.ExperimentType.*;


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

    private static void runBenchmarkMode(Integer numberOfRepetitions, String[] args) throws IOException{
        ExperimentType experimentType = TIME;
        Config config = parseCLIArguments(args);
        Integer timeLapse = config.getTimeLapse();
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter("experimentResult.txt"));
        resultWriter.write(String.format("%d %d %f %f %d\n", timeLapse, numberOfRepetitions, config.getSideAreaLength(), config.getEta(), config.getNumberOfParticles()));
        if(experimentType == NOISE) {
            createLatticeExperimentFile(config);
            Double eta = 0.0;
            while(numberOfRepetitions >=0){

                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);

                int N = input.getNumberOfParticles();

                ArrayList<Particle> particles = input.getParticles();
                OffLatticeAutomata ola = new OffLatticeAutomata(resultWriter, NOISE);
                ola.runSimulation(
                        timeLapse,
                        eta,
                        1,
                        N,
                        (double)input.getAreaSideLength(),
                        input.getOptimalM(config, input),
                        config.getV(),
                        particles,
                        config.getRc(),
                        input.getFirstMaxRadius(),
                        input.getSecondMaxRadius(),
                        config,
                        input);
                eta+=0.5;
                numberOfRepetitions--;
            }
        }
        else if(experimentType == DENSITY) {
            while(numberOfRepetitions >=0){
                int N = numberOfRepetitions == 0? 40:(int) (numberOfRepetitions/2.0*20*20);
                createLatticeExperimentFile(config,N);
                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);
                ArrayList<Particle> particles = input.getParticles();
                OffLatticeAutomata ola = new OffLatticeAutomata(resultWriter, DENSITY);
                ola.runSimulation(
                        timeLapse,
                        config.getEta(),
                        1,
                        N,
                        20.0,
                        input.getOptimalM(config, input),
                        config.getV(),
                        particles,
                        config.getRc(),
                        input.getFirstMaxRadius(),
                        input.getSecondMaxRadius(),
                        config,
                        input);
                numberOfRepetitions--;
            }
        }else {
            createLatticeExperimentFile(config);
            while(numberOfRepetitions >=0){

                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);

                int N = input.getNumberOfParticles();

                ArrayList<Particle> particles = input.getParticles();
                OffLatticeAutomata ola = new OffLatticeAutomata(resultWriter, TIME);
                ola.runSimulation(
                        timeLapse,
                        config.getEta(),
                        1,
                        N,
                        (double)input.getAreaSideLength(),
                        input.getOptimalM(config, input),
                        config.getV(),
                        particles,
                        config.getRc(),
                        input.getFirstMaxRadius(),
                        input.getSecondMaxRadius(),
                        config,
                        input);
                numberOfRepetitions--;
            }
        }
        resultWriter.close();
    }

    private static void runNormalMode(String[] args)throws IOException{
        Config config = parseCLIArguments(args);
        if(config == null) System.exit(1);
        if(config.isExperiment()) createLatticeExperimentFile(config);

        LatticeInput input = parseInputFile(config);
        if(input == null) System.exit(1);


        int N = input.getNumberOfParticles();

        System.out.println(input.getOptimalM(config, input));

        ArrayList<Particle> particles = input.getParticles();

        OffLatticeAutomata ola = new OffLatticeAutomata();

        ola.runSimulation(config.getTimeLapse(),
                config.getEta(),
                1,
                N,
                (double)input.getAreaSideLength(),
                input.getOptimalM(config, input),
                config.getV(),
                particles,
                config.getRc(),
                input.getFirstMaxRadius(),
                input.getSecondMaxRadius(),
                config,
                input
        );


    }
    public static void main(String[] args) {
        boolean BENCHMARK_MODE = true;
        Integer repsForBenchmark = 0;
        long start = System.nanoTime();
        try {
            if (BENCHMARK_MODE) {
                runBenchmarkMode(repsForBenchmark, args);
            } else {
                runNormalMode(args);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            System.out.println("Exiting program...");
            System.out.println(System.nanoTime()-start);
        }

    }
}
