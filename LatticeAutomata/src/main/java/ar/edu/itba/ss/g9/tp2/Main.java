package ar.edu.itba.ss.g9.tp2;

import ar.edu.itba.ss.g9.commons.Config;
import ar.edu.itba.ss.g9.commons.enums.ExperimentType;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static ar.edu.itba.ss.g9.tp2.LatticeFileParser.*;
import static ar.edu.itba.ss.g9.commons.enums.ExperimentType.DENSITY;
import static ar.edu.itba.ss.g9.commons.enums.ExperimentType.NOISE;

public class Main {
    public static final String VISUALIZATION_PATH = "LatticeAutomata/src/main/visualization";
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
            return readLatticeInput(config.getInputFileName());

        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private static VaOutput parseVaOutput(Config config, Integer repetitionNumber){
        try {
            return readVaInput(config.getOutputFileName(VISUALIZATION_PATH)+"Va"+repetitionNumber+".txt");
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private static void cratateOffLaticeAutomataAndRunSimulation(Config config, LatticeInput input, Integer N, Double L, Double eta, Integer repetitionNumber) throws IOException {
        ArrayList<Particle> particles = input.getParticles();
        OffLatticeAutomata ola = new OffLatticeAutomata();
        ola.runSimulation(
                config.getTimeLapse(),
                eta,
                1,
                N,
                L,
                input.getOptimalM(config, input),
                config.getV(),
                particles,
                config.getRc(),
                input.getFirstMaxRadius(),
                input.getSecondMaxRadius(),
                config,
                input,
                repetitionNumber);
    }

    private static void runBenchmarkMode(Integer numberOfRepetitions, String[] args, ExperimentType experimentType) throws IOException{
        Config config = parseCLIArguments(args);
        Integer timeLapse = config.getTimeLapse();
        Integer totalRepetitions = numberOfRepetitions;

        BufferedWriter resultWriter = new BufferedWriter(new FileWriter("experimentResult.txt"));
        resultWriter.write(String.format("%d %d %f %f %d\n", timeLapse, numberOfRepetitions, config.getSideAreaLength(), config.getEta(), config.getNumberOfParticles()));

        if(experimentType == NOISE) {
            createLatticeExperimentFile(config);
            Double eta = 0.0;
            while(numberOfRepetitions >=0){
                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);
                cratateOffLaticeAutomataAndRunSimulation(config, input, input.getNumberOfParticles(), input.getAreaSideLength(), eta, numberOfRepetitions);
                eta+=0.5;
                numberOfRepetitions--;
            }
        }
        else if(experimentType == DENSITY) {
            while(numberOfRepetitions >=0){
                int N = numberOfRepetitions == 0? 40:(int) (numberOfRepetitions/2.0*20*20);
                double L = 20.0;
                createLatticeExperimentFile(config,N);
                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);
                cratateOffLaticeAutomataAndRunSimulation(config, input, N, L, config.getEta(), numberOfRepetitions);
                numberOfRepetitions--;
            }
        }else {
            createLatticeExperimentFile(config);
            while(numberOfRepetitions >=0){
                LatticeInput input = parseInputFile(config);
                if(input == null) System.exit(1);
                cratateOffLaticeAutomataAndRunSimulation(config, input, input.getNumberOfParticles(), input.getAreaSideLength(), config.getEta(), numberOfRepetitions);
                numberOfRepetitions--;
            }
        }

        MetricsEngine metricsEngine = new MetricsEngine(resultWriter, experimentType);
        for(int i = totalRepetitions; i >=0 ; i--) {
            VaOutput vaOutput = parseVaOutput(config,i);
            metricsEngine.printStatisticsToExperimentFile(vaOutput);
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
                input,
                0
        );


    }
    public static void main(String[] args) {
        boolean BENCHMARK_MODE = true;
        Integer repsForBenchmark = 2;
        ExperimentType experimentType = NOISE;
        long start = System.nanoTime();
        try {
            if (BENCHMARK_MODE) {
                runBenchmarkMode(repsForBenchmark, args, experimentType);
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