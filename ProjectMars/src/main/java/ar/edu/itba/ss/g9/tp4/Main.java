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
    final static double HOUR = 60 * 60;
    final static double DAY = 60 * 60 * 24;
    final static double MONTH = 30 * DAY;
    final static double YEAR = DAY * 365;

    final static double MARS_BEST_LAUNCH_TIME = 1035198 * 60; // most specific time found in FIND_LAUNCH
    final static double JUPITER_BEST_LAUNCH_TIME = 616 * DAY; // most specific time found in FIND_LAUNCH
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

        double launchSpeed = 8 * 1000; // m/s
        double launchAngle = 0;
        SolarSystem solarSystem;

        boolean jupiterAsDestiny = true;

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
                tf = 20 * YEAR;
                deltaT2 = 360 * 300;
                solarSystem = new SolarSystem(new Gravity(), deltaT, jupiterAsDestiny);
                double launchTime = MARS_BEST_LAUNCH_TIME;
                if(jupiterAsDestiny)
                  launchTime = JUPITER_BEST_LAUNCH_TIME;
                solarSystem.simulate(deltaT2, tf, launchTime, launchSpeed, launchAngle, mode);
                break;
            case FIND_LAUNCH:
                tf = 7 * YEAR;
                System.out.println("minimum distance (m), days since launch, launch day, arrival speed (km/s) (in case of arrival)");
                for (double i = 0; i < 2 * YEAR && i < tf; i += DAY) {
                    solarSystem = new SolarSystem(new Gravity(), deltaT, jupiterAsDestiny);
                    solarSystem.simulate(deltaT2, tf, i, launchSpeed, launchAngle, mode);
                }
                break;
            case SHIP_VELOCITY:
                tf = 3 * YEAR;
                deltaT2 = 360 * 400;
                double assuredArrivalLaunchTime = MARS_BEST_LAUNCH_TIME;
                if(jupiterAsDestiny)
                    assuredArrivalLaunchTime = JUPITER_BEST_LAUNCH_TIME;
                solarSystem = new SolarSystem(new Gravity(), deltaT, jupiterAsDestiny);
                solarSystem.simulate(deltaT2, tf, assuredArrivalLaunchTime, launchSpeed, launchAngle, mode);
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
