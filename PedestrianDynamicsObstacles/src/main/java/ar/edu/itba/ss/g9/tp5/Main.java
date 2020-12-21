package ar.edu.itba.ss.g9.tp5;

import java.util.Arrays;
import java.util.Optional;

/**
 * Hello world!
 *
 */
public class Main
{
    public static void main( String[] args ) {
//        System.out.println(String.format("Running with args: %s", Arrays.toString(args)));
        final int obstaclesNum  = Integer.parseInt(args[0]);
        final double deltaT     = Double.parseDouble(args[1]);
        final double deltaT2    = Double.parseDouble(args[2]);
        Optional<Double> dmin = Optional.empty();
        Optional<Double> talpha = Optional.empty();
        Optional<Double> radius = Optional.empty();
        Optional<Integer> col_num = Optional.empty();
        if(args.length == 5) {
            final String observable = args[3];
            switch (observable) {
                case "DMIN":
                    dmin = Optional.of(Double.parseDouble(args[4]));
                    break;
                case "TALPHA":
                    talpha = Optional.of(Double.parseDouble(args[4]));
                    break;
                case "RAD":
                    radius = Optional.of(Double.parseDouble(args[4]));
                    break;
                case "COL":
                    col_num = Optional.of(Integer.parseInt(args[4]));
                    break;

            }
        }

        CollisionAvoidanceSimulation simulation = new CollisionAvoidanceSimulation(obstaclesNum, deltaT, deltaT2, dmin, talpha, radius, col_num);
        simulation.simulate();

    }
}
