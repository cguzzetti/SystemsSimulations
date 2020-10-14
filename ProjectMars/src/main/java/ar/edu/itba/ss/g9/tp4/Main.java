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
    final static double DAY = 60 * 60 * 24;
    final static double MONTH = 30 * DAY;
    final static double YEAR = DAY * 365;
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

        if(mode == RunMode.MARS_PLANETS)
            deltaT2 = 720;
        double launchTime = deltaT2 * 5;
        double launchSpeed = 8 * 1000; // m/s
        double launchAngle = 0;
        SolarSystem solarSystem;

        switch (mode){
            case OVITO:
                oscillators = generateOscillators(force, k, g, m, deltaT);
                generateSimulationForVisualization(deltaT2, tf, oscillators);
                break;
            case SOLUTION:
                oscillators = generateOscillators(force, k, g, m, deltaT);
                generateSimulationForSolution(deltaT, tf, oscillators);
                break;
            case MARS_PLANETS:
                tf = 3 * YEAR;
                solarSystem = new SolarSystem(new Gravity(), deltaT);
                solarSystem.simulate(deltaT2, tf, 459 * DAY, launchSpeed, launchAngle);
                break;
            case MARS_FIND_LAUNCH:
                tf = 3 * YEAR;
                solarSystem = new SolarSystem(new Gravity(), deltaT);
                solarSystem.runExperimentSimulations( tf, launchSpeed, launchAngle);
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
