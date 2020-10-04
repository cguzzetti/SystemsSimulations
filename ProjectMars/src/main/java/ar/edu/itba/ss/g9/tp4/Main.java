package ar.edu.itba.ss.g9.tp4;

import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.ss.g9.tp4.Oscillator.generateSimulationForVisualization;

/**
 * Main program excution
 *
 */
public class Main {
    public static void main( String[] args ) {
        double k = Math.pow(10, 4);
        double g = 100;
        double m = 70;
        double tf = 5;
        RunMode mode = RunMode.valueOf(args[0]);
        double deltaT = Double.parseDouble(args[1]);
        double deltaT2;
        if(args.length == 2)
            deltaT2 = 0.02;
        else
            deltaT2 = Double.parseDouble(args[2]);

        Force force = new OscillatorForce(k, g);
        Oscillator oscillator;
        List<Oscillator> oscillators = new LinkedList<>();
        IntegralMethods[] methods = new IntegralMethods[]{
                IntegralMethods.GEAR_PREDICTOR_CORRECTOR, IntegralMethods.VERLET_ORIGINAL,
                IntegralMethods.BEEMAN, IntegralMethods.ANALITICAL
        };
        for(int i = 0 ; i< 4; i++){
            oscillator = new Oscillator(i, force, methods[i], k, g, m, deltaT );
            if(methods[i] == IntegralMethods.GEAR_PREDICTOR_CORRECTOR)
                oscillator.initializeEquationsTables();
            else if(methods[i] != IntegralMethods.ANALITICAL)
                oscillator.initializePreviousValues();
            oscillators.add(oscillator);
        }
        switch (mode){
            case OVITO:
                generateSimulationForVisualization(deltaT2, tf, oscillators);
                break;
            case SOLUTION:
                System.out.println("IN SOLUTION MODE");
                break;
            case ERROR:
                System.out.println("IN ERROR MODE");
                break;
        }


    }
}
