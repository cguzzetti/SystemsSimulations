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
    private static int THREE_YEARS = 3 * 365 * 24 * 60 * 60;
    private static int ONE_MONTH   = 31 * 24 * 60 * 60;
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
                oscillators = generateOscillators(force, k, g, m, deltaT);
                generateSimulationForVisualization(deltaT2, tf, oscillators);
                break;
            case SOLUTION:
                oscillators = generateOscillators(force, k, g, m, deltaT);
                generateSimulationForSolution(deltaT, tf, oscillators);
                break;
            case MARS:
                tf = THREE_YEARS;
                deltaT2 = ONE_MONTH/4.0;
                double launchTime = deltaT2 * 5;
                double launchSpeed = 10; // km/s
                double launchAngle = 90;
                SolarSystem solarSystem = new SolarSystem(new Gravity(), IntegralMethods.BEEMAN, deltaT);
                solarSystem.simulate(deltaT2, tf, launchTime, launchSpeed, launchAngle);
                break;
        }
    }

    private static List<Oscillator> generateOscillators(Force force, double k, double g, double m, double deltaT){
        List<Oscillator> ret = new LinkedList<>();
        IntegralMethods[] methods = new IntegralMethods[]{
                IntegralMethods.GEAR_PREDICTOR_CORRECTOR, IntegralMethods.VERLET_ORIGINAL,
                IntegralMethods.BEEMAN, IntegralMethods.ANALITICAL
        };
        Oscillator oscillator;
        int i;
        for(i = 0 ; i< 4; i++){
            oscillator = new Oscillator(i, force, methods[i], k, g, m, deltaT );
            if(methods[i] == IntegralMethods.GEAR_PREDICTOR_CORRECTOR)
                oscillator.initializeEquationsTables();
            else if(methods[i] != IntegralMethods.ANALITICAL)
                oscillator.initializePreviousValues();

            ret.add(oscillator);
        }

        return ret;
    }
}
