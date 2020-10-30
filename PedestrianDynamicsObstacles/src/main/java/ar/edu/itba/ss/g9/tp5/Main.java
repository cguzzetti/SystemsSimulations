package ar.edu.itba.ss.g9.tp5;

import java.util.Arrays;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) {
        System.out.println(String.format("Running with args: %s", Arrays.toString(args)));
        final int obstaclesNum  = Integer.parseInt(args[0]);
        final double deltaT     = Double.parseDouble(args[1]);
        final double deltaT2    = Double.parseDouble(args[2]);
        System.out.println(obstaclesNum);
        System.out.println(deltaT);
        System.out.println(deltaT2);
        System.out.println("Done!");

    }
}
