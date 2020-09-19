package ar.edu.itba.ss.g9.commons.simulation;

import ar.edu.itba.ss.g9.commons.DynamicFile;
import ar.edu.itba.ss.g9.commons.InputFile;
import ar.edu.itba.ss.g9.commons.Config;
import ar.edu.itba.ss.g9.commons.StaticFile;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import static ar.edu.itba.ss.g9.commons.simulation.ParticleGeneration.generateRandomParticles;

public class FileParser {

    private static void createParticle(StaticFile inputFile, String particleLine, int lineNum){
        int particleIndex = (int) ((lineNum - 2) % (inputFile.getNumberOfParticles()+1));
        String[] particleArr = particleLine.replaceAll(" +", " ").split(" ");
        double radius = Double.parseDouble(particleArr[2]);
        inputFile.updateMaxRadius(radius);
        Particle particle = new Particle(
                Double.parseDouble(particleArr[0]),
                Double.parseDouble(particleArr[1]),
                radius,
                particleIndex);

        inputFile.addParticle(particle);
    }

    private static void createParticle(DynamicFile dynamicFile, String particleLine, int lineNum){
        int particleIndex = (int) ((lineNum - 2) % (dynamicFile.getNumberOfParticles()+1));

        if(particleIndex == 0){
            dynamicFile.addParticle(null, particleIndex);
        }else{
            String[] particleArr = particleLine.replaceAll(" +", " ").split(" ");
            Particle particle = new Particle(
                    Double.parseDouble(particleArr[0]),
                    Double.parseDouble(particleArr[1]),
                    Double.parseDouble(particleArr[2]),
                    Double.parseDouble(particleArr[3]),
                    Integer.parseInt(particleArr[4]),
                    particleIndex
            );

            dynamicFile.addParticle(particle, particleIndex);
        }

    }

    private static BufferedReader bufferedReaderFromFilename(final String fileName) throws IOException {
        File file = new File(fileName);

        return new BufferedReader(new FileReader(file));
    }

    public static StaticFile readStaticInput(final String fileName) throws IOException {
        BufferedReader br = bufferedReaderFromFilename(fileName);

        String st;
        int linesRead = 0;
        StaticFile inputFile = new StaticFile();
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

    public static DynamicFile readDynamicInput(final String filename) throws IOException{
        BufferedReader br = bufferedReaderFromFilename(filename);
        String st;
        int linesRead = 0;
        DynamicFile dynamicFile = new DynamicFile();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead<=1){
                if(linesRead == 0)
                    dynamicFile.setNumberOfParticles(Integer.parseInt(st));
                else
                    dynamicFile.setAreaSideLength(Double.parseDouble(st));
            }else{
                createParticle(dynamicFile, st, linesRead);
            }
            linesRead++;
        }

        return dynamicFile;
    }


    public static void printInputFileContent(StaticFile file){
        System.out.println(String.format("Number of particles: %s", file.getNumberOfParticles()));
        System.out.println(String.format("Length of area side: %s", file.getAreaSideLength()));
        System.out.println("Particles:");
        file.getParticles().forEach(System.out::println);
    }

    public static void printInputFileContent(DynamicFile file){
        System.out.println(String.format("Number of particles: %s", file.getNumberOfParticles()));
        System.out.println(String.format("Length of area side: %s", file.getAreaSideLength()));
        System.out.println("Particles:");
        file.getParticles().forEach(particles -> {
            System.out.println(Arrays.toString(particles));
            System.out.println(" ");
        });
    }

    public static void printOutputToFile(DynamicFile file, final String filename) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(String.format("Number of particles: %s\n", file.getNumberOfParticles()));
        writer.write(String.format("Length of area side: %s\n", file.getAreaSideLength()));
        writer.write("Particles:\n");

        for(Particle[] particles : file.getParticles()){
            writer.write(Arrays.toString(particles));
            writer.write(" \n");
        }
        writer.close();
    }

    public static void printOutputToFile(StaticFile file, final Config config, final String visualizationPath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(config.getOutputFileName(visualizationPath)));
        writer.write(String.format("%s %s %s %s %s %s\n",
                file.getNumberOfParticles(),
                file.getAreaSideLength(),
                config.getRc(),
                config.isPeriodic(),
                file.getFirstMaxRadius(),
                file.getSecondMaxRadius())
        );
        for(Particle particle: file.getParticles()){
            writer.write(String.format("%s\n",particle.toString()));
        }
        writer.close();
    }

    public static void createExperimentFile(final Config config) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(config.getInputFileName()));
        long numberOfParticles = config.getNumberOfParticles();
        double sideLength = config.getSideAreaLength();
        writer.write(String.format("%d\n", numberOfParticles));
        writer.write(String.format("%d\n", (int)sideLength));

        StringBuilder result = generateRandomParticles(numberOfParticles, sideLength);

        writer.write(result.toString());
        writer.close();
    }

}

