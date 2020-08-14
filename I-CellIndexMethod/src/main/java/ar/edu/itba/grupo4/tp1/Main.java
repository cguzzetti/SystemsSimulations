package ar.edu.itba.grupo4.tp1;

import ar.edu.itba.grupo4.tp1.util.*;

import java.io.IOException;

import ar.edu.itba.grupo4.tp1.util.files.FileParser;
import ar.edu.itba.grupo4.tp1.util.files.models.DynamicFile;
import ar.edu.itba.grupo4.tp1.util.files.models.StaticFile;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class Main 
{
    public static void main( String[] args ) {
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

        System.out.println(config.toString());
        System.out.println("\n");

        FileParser fp = new FileParser();

        try{
            if(config.getInputType().equals(InputType.STATIC)) {
                if (config.isExperiment())
                    fp.createExperimentFile(config);

                StaticFile file = fp.readStaticInput(config.getInputFileName());
                final Integer optimalM = file.getOptimalM(config, file);
                System.out.println(String.format("Optimal M: %d", optimalM));
                long currentTime;
                if(config.getRunMode().equals(RunMode.CIM)) {
                    CellIndexMethod cim = new CellIndexMethod(file.getNumberOfParticles(), (double) file.getAreaSideLength(), optimalM, file.getParticles(), config.isPeriodic(), config.getRc(), file.getFirstMaxRadius(), file.getSecondMaxRadius());
                }else {
                    System.out.println("Running in Brute Force Mode");
                    BruteForce bf = new BruteForce(file.getParticles(), 1);
                }
                fp.printInputFileContent(file);
                fp.printOutputToFile(file, config);

            }else {
                // How do things vary with Dynamic File?
                DynamicFile file = fp.readDynamicInput("readme_dynamic.txt");
            }
        } catch (IOException notFound){
            System.out.println(String.format("Error: %s", notFound.getMessage()));
        }
    }


}
