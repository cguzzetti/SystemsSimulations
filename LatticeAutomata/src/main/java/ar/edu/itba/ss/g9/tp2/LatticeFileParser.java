package ar.edu.itba.ss.g9.tp2;

import ar.edu.itba.ss.g9.commons.Config;
import ar.edu.itba.ss.g9.commons.simulation.FileParser;
import ar.edu.itba.ss.g9.commons.simulation.Particle;

import java.io.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class LatticeFileParser extends FileParser {
    private static void createParticle(LatticeInput inputFile, String particleLine, int lineNum){
        int particleIndex = (int) ((lineNum - 2) % (inputFile.getNumberOfParticles()+1));
        String[] particleArr = particleLine.replaceAll(" +", " ").split(" ");

        double velocity = Double.parseDouble(particleArr[2]);
        double direction = Double.parseDouble(particleArr[3]);
        double radius = Double.parseDouble(particleArr[4]);
        inputFile.updateMaxRadius(radius);
        Particle particle = new Particle(
                Double.parseDouble(particleArr[0]),
                Double.parseDouble(particleArr[1]),
                velocity,
                direction,
                radius,
                particleIndex);

        inputFile.addParticle(particle);
    }

    private static void parseVaOutputHeaders(VaOutput vaOutput, String line){
        String[] lineArr = line.replaceAll(" +", " ").split(" ");

        int timeLapse = Integer.parseInt((lineArr[0]));
        int N = Integer.parseInt(lineArr[1]);
        double L = Double.parseDouble(lineArr[2]);
        double eta = Double.parseDouble(lineArr[3]);

        vaOutput.setTimeLapse(timeLapse);
        vaOutput.setN(N);
        vaOutput.setL(L);
        vaOutput.setEta(eta);
    }

    private static BufferedReader bufferedReaderFromFilename(final String fileName) throws IOException {
        File file = new File(fileName);

        return new BufferedReader(new FileReader(file));
    }

    public static LatticeInput readLatticeInput(final String fileName) throws IOException {
        BufferedReader br = bufferedReaderFromFilename(fileName);

        String st;
        int linesRead = 0;
        LatticeInput inputFile = new LatticeInput();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead <=1){
                if(linesRead == 0)
                    inputFile.setNumberOfParticles(Integer.parseInt(st));
                else
                    inputFile.setAreaSideLength(Double.parseDouble(st));
            }else{
                createParticle(inputFile, st, linesRead);
            }
            linesRead++;
        }

        return inputFile;
    }

    public static VaOutput readVaInput(final String fileName) throws IOException {
        BufferedReader br = bufferedReaderFromFilename(fileName);

        String st;
        int linesRead = 0;
        VaOutput vaOutput = new VaOutput();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead == 0){
                parseVaOutputHeaders(vaOutput,st);
            }else{
                vaOutput.addEtaValue(Double.parseDouble(st));
            }
            linesRead++;
        }
        return vaOutput;
    }
    public static void printHeadertoFile(LatticeInput file, final Config config, BufferedWriter writer, Integer timelapse){
        try {
            writer.write(String.format("%s\n",
                    file.getNumberOfParticles())
            );
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void printHeadertoVaFile(BufferedWriter writer, Integer timelapse, Integer N, Double L, Double eta){
        try {
            writer.write(String.format("%d %d %f %f\n", timelapse, N, L, eta)
            );
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void printParticlesInTimeToFile(LatticeInput file, final Config config, int time, BufferedWriter writer) throws IOException{
        StringBuilder builder = new StringBuilder();;
        for (Particle particle : file.getParticles()) {
            builder.append(String.format("%s\n", particle.toString()));
        }
        writer.write(String.format("%d\n%s", time, builder.toString()));
    }

    public static void printVaInTimeToFile(Double va, BufferedWriter writer) throws IOException{
        writer.write(String.format("%f\n", va));
    }

    public static void createLatticeExperimentFile(final Config config){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config.getInputFileName()));
            long numberOfParticles = config.getNumberOfParticles();
            double sideLength = config.getSideAreaLength();
            writer.write(String.format("%d\n", numberOfParticles));
            writer.write(String.format("%f\n", sideLength));

            StringBuilder result = Stream.iterate(0, n -> n + 1).limit(numberOfParticles).parallel().collect(StringBuilder::new, (sb, i) -> {
                // Every call to ThreadLocalRandom.current() is going to call
                // localInit which will generate a new seed.
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                double x = rand.nextDouble(0, sideLength);
                double y = rand.nextDouble(0, sideLength);
                double velocity = 0.03;
                double direction = rand.nextDouble(-Math.PI, Math.PI);
                double radius = 0;
                sb.append(String.format("%f %f %f %f %f\n", x, y, velocity, direction, radius));
            }, StringBuilder::append);

            writer.write(result.toString());
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public static void createLatticeExperimentFile(final Config config, int N){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(config.getInputFileName()));
            long numberOfParticles = N;
            double sideLength = config.getSideAreaLength();
            writer.write(String.format("%d\n", numberOfParticles));
            writer.write(String.format("%d\n", (int) sideLength));

            StringBuilder result = Stream.iterate(0, n -> n + 1).limit(numberOfParticles).parallel().collect(StringBuilder::new, (sb, i) -> {
                // Every call to ThreadLocalRandom.current() is going to call
                // localInit which will generate a new seed.
                ThreadLocalRandom rand = ThreadLocalRandom.current();
                double x = rand.nextDouble(0, sideLength);
                double y = rand.nextDouble(0, sideLength);
                double velocity = 0.03;
                double direction = rand.nextDouble(-Math.PI, Math.PI);
                double radius = 0;
                sb.append(String.format("%f %f %f %f %f\n", x, y, velocity, direction, radius));
            }, StringBuilder::append);

            writer.write(result.toString());
            writer.close();
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }


}
