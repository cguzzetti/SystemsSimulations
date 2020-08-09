package ar.edu.itba.grupo4.tp1;




import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.itba.grupo4.tp1.util.CellIndexMethod;
import ar.edu.itba.grupo4.tp1.util.Config;
import ar.edu.itba.grupo4.tp1.util.files.FileParser;
import ar.edu.itba.grupo4.tp1.util.files.models.StaticFile;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


/**
 * Hello world!
 *
 */
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

        Particle p0 = new Particle(0.4, 0.3, 0, 0,"p0");
        Particle p1 = new Particle(0.6, 0.6, 0, 1,"p1");
        Particle p2 = new Particle(0.1, 0.4, 0, 2, "p2");
        Particle p3 = new Particle(0.7, 0.7, 0, 3, "p3");
        Particle p4 = new Particle(0.3, 0.1, 0, 4, "p4");
        Particle p5 = new Particle(0.8, 0.1, 0, 5, "p5");
        Particle p6 = new Particle(0.8, 0.3, 0, 6, "p6");
        Particle p7 = new Particle(0.9, 0.9, 0, 7, "p7");

        CellIndexMethod cim = new CellIndexMethod(8, (double) 1, 2, Stream.of(p0,p1,p2,p3,p4,p5,p6,p7).collect(Collectors.toList()), false, 0.4);

        FileParser fp = new FileParser();

        try{
            StaticFile file = fp.readStaticInput("readme_static.txt");
            //DynamicFile file = fp.readDynamicInput("readme_dynamic.txt");
            fp.printInputFileContent(file);
            fp.printOutputToFile(file,"CIMOutput.txt");
        } catch (IOException notFound){
            System.out.println(String.format("Error: %s", notFound.getMessage()));
        }
    }
}
