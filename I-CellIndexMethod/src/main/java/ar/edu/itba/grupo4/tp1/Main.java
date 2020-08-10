package ar.edu.itba.grupo4.tp1;

import ar.edu.itba.grupo4.tp1.util.BruteForce;

import java.io.IOException;

import ar.edu.itba.grupo4.tp1.util.CellIndexMethod;
import ar.edu.itba.grupo4.tp1.util.Config;
import ar.edu.itba.grupo4.tp1.util.InputType;
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

        //TODO: Refactor L to double. Why is it double tho?
        try{
            if(config.getInputType().equals(InputType.STATIC)) {
                StaticFile file = fp.readStaticInput("readme_static.txt");
//                CellIndexMethod cim = new CellIndexMethod(file.getNumberOfParticles(), (double) file.getAreaSideLength(), 2, file.getParticles(), config.getPeriodic(), 0.4);
                BruteForce bf = new BruteForce(file.getParticles(), 0.4);
                fp.printInputFileContent(file);
                fp.printOutputToFile(file,"CIMOutput.txt");

            }else {
                // How do things vary with Dynamic File?
                DynamicFile file = fp.readDynamicInput("readme_dynamic.txt");
            }
        } catch (IOException notFound){
            System.out.println(String.format("Error: %s", notFound.getMessage()));
        }
    }


}
