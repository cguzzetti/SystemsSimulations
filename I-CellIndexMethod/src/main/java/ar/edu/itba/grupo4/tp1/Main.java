package ar.edu.itba.grupo4.tp1;




import ar.edu.itba.grupo4.tp1.util.CellIndexMethod;
//import ar.edu.itba.grupo4.tp1.util.files.FileParser;
import ar.edu.itba.grupo4.tp1.util.files.models.DynamicFile;
import ar.edu.itba.grupo4.tp1.util.files.models.InputFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hello world!
 *
 */
public class Main 
{
    public static void main( String[] args ) {
        System.out.println( "Hello World!" );

        Particle p0 = new Particle(0.4, 0.3, 0, 0,"p0");
        Particle p1 = new Particle(0.6, 0.6, 0, 1,"p1");
        Particle p2 = new Particle(0.1, 0.4, 0, 2, "p2");
        Particle p3 = new Particle(0.7, 0.7, 0, 3, "p3");
        Particle p4 = new Particle(0.3, 0.1, 0, 4, "p4");
        Particle p5 = new Particle(0.8, 0.1, 0, 5, "p5");
        Particle p6 = new Particle(0.8, 0.3, 0, 6, "p6");
        Particle p7 = new Particle(0.9, 0.9, 0, 7, "p7");

        CellIndexMethod cim = new CellIndexMethod();

        cim.generateLists(8, (double) 1, 2, Stream.of(p0,p1,p2,p3,p4,p5,p6,p7).collect(Collectors.toList()));
        cim.calculateNeighbors(8, (double) 1, 2, Stream.of(p0,p1,p2,p3,p4,p5,p6,p7).collect(Collectors.toList()), false, 0.4);
//        FileParser fp = new FileParser();
//
//        try{
//            InputFile file = fp.readStaticInput("readme.txt");
//            fp.printInputFileContent((DynamicFile) file);
//        } catch (IOException notFound){
//            System.out.println(String.format("Error: %s", notFound.getMessage()));
//        }
    }
}
