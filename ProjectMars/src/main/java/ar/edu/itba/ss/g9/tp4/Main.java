package ar.edu.itba.ss.g9.tp4;

import ar.edu.itba.ss.g9.tp4.Mars.Gravity;
import ar.edu.itba.ss.g9.tp4.Mars.SolarSystem;

import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.ss.g9.tp4.Oscillator.*;

/**
 * Main program execution
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

        List<Oscillator> oscillators;

        switch (mode){
            case OVITO:
                oscillators = generateOscillators(force, k, g, m, deltaT, false);
                generateSimulationForVisualization(deltaT2, tf, oscillators);
                break;
            case SOLUTION:
                oscillators = generateOscillators(force, k, g, m, deltaT, false);
                generateSimulationForSolution(deltaT, tf, oscillators);
                break;
            case ERROR:
                double[] deltas = new double[]{0.0000001, 0.000001, 0.00001, 0.0001, 0.001};
                for (double delta : deltas) {
                    oscillators = generateOscillators(force, k, g, m, delta, true);
                    Oscillator analyticalOscillator = new Oscillator(3, force, IntegralMethods.ANALITICAL, k, g, m, delta);
                    generateSimulationForErrors(delta, tf, oscillators, analyticalOscillator);
                }
                break;
            case MARS:
                tf = 3 * 365 * 24 * 60 * 60;
                SolarSystem solarSystem = new SolarSystem(new Gravity(), IntegralMethods.BEEMAN, deltaT);
                solarSystem.simulate(deltaT2, tf);
                break;
        }
    }

    private static List<Oscillator> generateOscillators(Force force, double k, double g, double m, double deltaT, boolean isError){
        List<Oscillator> ret = new LinkedList<>();
        IntegralMethods[] methods = new IntegralMethods[]{
                IntegralMethods.GEAR_PREDICTOR_CORRECTOR, IntegralMethods.VERLET_ORIGINAL,
                IntegralMethods.BEEMAN
        };
        Oscillator oscillator;
        int i;
        for(i = 0 ; i< 3; i++){
            oscillator = new Oscillator(i, force, methods[i], k, g, m, deltaT );
            if(methods[i] == IntegralMethods.GEAR_PREDICTOR_CORRECTOR)
                oscillator.initializeEquationsTables();

            oscillator.initializePreviousValues();
            ret.add(oscillator);
        }

        // We don't add the Analytical oscillator when calculating errors
        if(!isError){
            ret.add(new Oscillator(i, force, IntegralMethods.ANALITICAL,  k, g, m, deltaT));
        }
        return ret;
    }
}
