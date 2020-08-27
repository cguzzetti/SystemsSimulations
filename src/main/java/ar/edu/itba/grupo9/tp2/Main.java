package ar.edu.itba.grupo9.tp2;


import ar.edu.itba.grupo9.tp1.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
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

    // -t static -f latticeInput.txt -N 400 -L 20 -e -p -m CIM -rc 1
    public static Config initializeConfig(){
        String inputType = "STATIC";
        String inputFileName = "latticeInput.txt";
        String outputFileName = "CIMOutput.txt";
        boolean isPeriodic = true;
        String mode = "CIM";
        String rc = "1";
        String L = "20";
        String N = "400";
        String M = "13";

        return new Config(
                inputType,
                inputFileName,
                outputFileName,
                isPeriodic,
                mode,
                rc,
                L,
                N,
                M
        );
    }

    private static void runBenchmarkMode(Integer repetitions) throws IOException{
        double expectedOutput = 0;
        int numberOfRepetitions = 3;
        Config config = initializeConfig();
        Integer timeLapse = 300;
        Double eta = 0.1;
        BufferedWriter resultWriter = new BufferedWriter(new FileWriter("experimentResult.txt"));
        resultWriter.write(String.format("Running %d times with %s as variable varying %s", numberOfRepetitions, "Va", "eta"));
        while(numberOfRepetitions >=0){
            createLatticeExperimentFile(config);

            LatticeInput input = parseInputFile(config);
            if(input == null) System.exit(1);

            int N = input.getNumberOfParticles();

            System.out.println(input.getOptimalM(config, input));

            ArrayList<Particle> particles = input.getParticles();
            OffLatticeAutomata ola = new OffLatticeAutomata(resultWriter);
            ola.runSimulation(
                    timeLapse,
                    eta,
                    1,
                    N,
                    (double)input.getAreaSideLength(),
                    input.getOptimalM(config, input),
                    0.03,
                    particles,
                    config.getRc(),
                    input.getFirstMaxRadius(),
                    input.getSecondMaxRadius(),
                    config,
                    input);

            numberOfRepetitions--;
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

        ola.runSimulation(500,
                2,
                1,
                N,
                (double)input.getAreaSideLength(),
                input.getOptimalM(config, input),
                0.03,
                particles,
                config.getRc(),
                input.getFirstMaxRadius(),
                input.getSecondMaxRadius(),
                config,
                input
        );


    }
    public static void main(String[] args) {
        boolean BENCHMARK_MODE = false;
        Integer repsForBenchmark = 10;
        try {
            if (BENCHMARK_MODE) {
                runBenchmarkMode(repsForBenchmark);
            } else {
                runNormalMode(args);
            }
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            System.out.println("Exiting program...");
        }

    }
}
