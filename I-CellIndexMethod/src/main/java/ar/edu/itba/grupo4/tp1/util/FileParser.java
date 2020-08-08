package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class FileParser {

    private void createParticle(InputFile inputFile, String particleLine){
        String[] particleArr = particleLine.replaceAll(" +", " ").split(" ");
        Particle particle = new Particle(
                Double.parseDouble(particleArr[0]),
                Double.parseDouble(particleArr[1]),
                Integer.parseInt(particleArr[2]));

        inputFile.addParticle(particle);
    }
    public InputFile readStaticInput(final String fileName) throws IOException {
        File file = new File(fileName);

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        int linesRead = 0;
        InputFile inputFile = new InputFile();
        while((st = br.readLine()) != null){
            st = st.trim();
            if(linesRead <=1){
                if(linesRead == 0)
                    inputFile.setNumberOfParticles(Long.parseLong(st));
                else
                    inputFile.setAreaSideLength(Long.parseLong(st));
            }else{
                this.createParticle(inputFile, st);
            }
            linesRead++;
        }

        return inputFile;
    }

    public void printInputFileContent(InputFile file){
        System.out.println(String.format("Number of particles: %s", file.getNumberOfParticles()));
        System.out.println(String.format("Length of area side: %s", file.getAreaSideLength()));
        System.out.println("Particles:");
        file.getParticles().forEach(System.out::println);
    }
}
