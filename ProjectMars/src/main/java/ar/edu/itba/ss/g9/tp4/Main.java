package ar.edu.itba.ss.g9.tp4;

import static ar.edu.itba.ss.g9.tp4.Oscillator.EPSILON;

/**
 * Hello world!
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
        IntegralMethod  method = new GearMethodPredictor(deltaT, force);

        Oscillator oscillator = new Oscillator(force, IntegralMethods.GEAR_PREDICTOR_CORRECTOR, k, g, m, deltaT );
        oscillator.initializeEquationsTables();

        double currentTime = 0;

        int[][] rgb = {{1, 0, 0}, {0, 1, 0},{0, 0, 1}};

        while( currentTime < tf){
            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;
            if(shouldPrint){
                System.out.println(2);
                System.out.println("t " + Math.round(currentTime / deltaT2));
            }
            oscillator.particle =  method.moveParticle(oscillator.particle, currentTime);
            if(shouldPrint){
                System.out.println("0 " + oscillator.particle.getPositionX() + " " + 0.4 + " 0.01 0 0");
                System.out.println(
                        "1 " + oscillator.particle.getPositionX() + " " + 0.4 + " " + 0.01 + " " + 1 + " " + 1);
            }
            currentTime+=deltaT;




        }



    }
}
