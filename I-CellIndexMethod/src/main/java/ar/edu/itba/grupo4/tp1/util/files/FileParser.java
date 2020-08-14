package ar.edu.itba.grupo4.tp1.util.files;

import ar.edu.itba.grupo4.tp1.Particle;
import ar.edu.itba.grupo4.tp1.util.Config;
import ar.edu.itba.grupo4.tp1.util.files.models.DynamicFile;
import ar.edu.itba.grupo4.tp1.util.files.models.StaticFile;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;


public class FileParser {

    private void createParticle(StaticFile inputFile, String particleLine, int lineNum){
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

    private void createParticle(DynamicFile dynamicFile, String particleLine, int lineNum){
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

    private BufferedReader bufferedReaderFromFilename(final String fileName) throws IOException{
        File file = new File(fileName);

        return new BufferedReader(new FileReader(file));
    }

    public StaticFile readStaticInput(final String fileName) throws IOException {
        BufferedReader br = this.bufferedReaderFromFilename(fileName);

        String st;
        int linesRead = 0;
        StaticFile inputFile = new StaticFile();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead <=1){
                if(linesRead == 0)
                    inputFile.setNumberOfParticles(Integer.parseInt(st));
                else
                    inputFile.setAreaSideLength(Long.parseLong(st));
            }else{
                this.createParticle(inputFile, st, linesRead);
            }
            linesRead++;
        }

        return inputFile;
    }

    public DynamicFile readDynamicInput(final String filename) throws IOException{
        BufferedReader br = this.bufferedReaderFromFilename(filename);
        String st;
        int linesRead = 0;
        DynamicFile dynamicFile = new DynamicFile();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead<=1){
                if(linesRead == 0)
                    dynamicFile.setNumberOfParticles(Integer.parseInt(st));
                else
                    dynamicFile.setAreaSideLength(Long.parseLong(st));
            }else{
                this.createParticle(dynamicFile, st, linesRead);
            }
            linesRead++;
        }

        return dynamicFile;
    }

    public void printInputFileContent(StaticFile file){
        System.out.println(String.format("Number of particles: %s", file.getNumberOfParticles()));
        System.out.println(String.format("Length of area side: %s", file.getAreaSideLength()));
        System.out.println("Particles:");
        file.getParticles().forEach(System.out::println);
    }

    public void printInputFileContent(DynamicFile file){
        System.out.println(String.format("Number of particles: %s", file.getNumberOfParticles()));
        System.out.println(String.format("Length of area side: %s", file.getAreaSideLength()));
        System.out.println("Particles:");
        file.getParticles().forEach(particles -> {
            System.out.println(Arrays.toString(particles));
            System.out.println(" ");
        });
    }

    public void printOutputToFile(DynamicFile file, final String filename) throws IOException{
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

    public void printOutputToFile(StaticFile file, final Config config) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(config.getOutputFileName()));
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

    public void createExperimentFile(final Config config) throws IOException{
        BufferedWriter writer = new BufferedWriter(new FileWriter(config.getInputFileName()));
        long numberOfParticles = config.getNumberOfParticles();
        double sideLength = config.getSideAreaLength();
        writer.write(String.format("%d\n", numberOfParticles));
        writer.write(String.format("%d\n", (int)sideLength));

        StringBuilder result = Stream.iterate(0, n -> n+1).limit(numberOfParticles).parallel().collect(StringBuilder::new, (sb, i)-> {
            // Every call to ThreadLocalRandom.current() is going to call
            // localInit which will generate a new seed.
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0, sideLength);
            double y = rand.nextDouble(0, sideLength);
            // double radius = rand.nextDouble(0, 1);
            double radius = 0.25;
            sb.append(String.format("%.2f %.2f %.2f\n", x, y, radius));
        }, StringBuilder::append);

        writer.write(result.toString());
        writer.close();
    }
}
