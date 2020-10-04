package ar.edu.itba.ss.g9.tp4;

import static ar.edu.itba.ss.g9.tp4.Oscillator.EPSILON;

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
        double deltaT = 0.001;
        double deltaT2 = 0.02;

        Force force = new OscillatorForce(k, g);

        Oscillator oscillator = new Oscillator(force, IntegralMethods.GEAR_PREDICTOR_CORRECTOR, k, g, m, deltaT );
        oscillator.initializeEquationsTables();

        oscillator.generateSimulationForVisualization(deltaT2, tf);

    }
}
