package ar.edu.itba.ss.g9.tp4;


import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;

public class Oscillator {
    private final static int NUMBER_OF_PARTICLES = 8;
    Force force;
    IntegralMethod method;
    double k;
    double g;
    double mass;
    double deltaT;
    private int id;

    final static double EPSILON = Math.pow(10, -6);

    AcceleratedParticle particle;

    public Oscillator(int id ,Force force, IntegralMethods method, double k, double g, double m, double deltaT) {
        this.id = id;
        this.force = force;
        this.k = k;
        this.g = g;
        this.mass = m;
        this.deltaT = deltaT;
        this.method = initializeMethod(method);
        Point2D.Double position = new Point2D.Double(1, id);
        Point2D.Double velocity = new Point2D.Double(-(g/ (2 * mass)), 0);
        particle = new AcceleratedParticle(id, position, velocity, 0, mass);
    }

    public void initializePreviousValues(){
        double prevX =
                particle.getPositionX()
                        - deltaT * particle.getVelocityY()
                        + pow(deltaT, 2) * force.getForceX(0, particle) / (2*particle.getMass());
        double prevY = 0;
        particle.setPrevPosition(new Point2D.Double(prevX, prevY));

        particle.setPrevVelocity(new Point2D.Double(
                particle.getVelocityX() - (deltaT / particle.getMass()) * force.getForceX(0, particle),
                0
        ));
    }

    public void initializeEquationsTables() {
        particle.equationListX[0] = particle.getPositionX();
        particle.equationListX[1] = particle.getVelocityX();
        particle.equationListX[2] = (force.getForceX(0, particle)) / particle.getMass();
        particle.equationListX[3] = -(k / mass) * particle.equationListX[1] - (g / mass) * particle.equationListX[2];
        particle.equationListX[4] = -(k / mass) * particle.equationListX[2] - (g / mass) * particle.equationListX[3];
        particle.equationListX[5] = -(k / mass) * particle.equationListX[3] - (g / mass) * particle.equationListX[4];
    }

    private IntegralMethod initializeMethod(IntegralMethods method){
        switch (method){
            case GEAR_PREDICTOR_CORRECTOR:
                return new GearMethodPredictor(this.deltaT, this.force);
            case BEEMAN:
                return new BeemanMethod(this.force, this.deltaT);
            case VERLET_ORIGINAL:
                return new VerletMethod(this.force, this.deltaT);
            case ANALITICAL:
                return new AnaliticalMethod(this.k, this.g);
            default:
                System.out.println("Invalid initialization method");
                return null;
        }
    }

    private static List<IntegralMethod> initializeIntegralMethods(double deltaT, Force force){
        List<IntegralMethod> ret = new LinkedList<>();
        ret.add(new GearMethodPredictor(deltaT, force));
        ret.add(new VerletMethod(force, deltaT));
        ret.add(new BeemanMethod(force, deltaT));

        return ret;
    }
    public static void generateSimulationForVisualization(double deltaT2, double tf, List<Oscillator> oscillators) {
        double currentTime = 0;
        double systemDeltaT = 0;
        while( currentTime < tf){
            // if (t/deltat2 ~= round(t/deltat2) ==> should print
            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;
            if(shouldPrint){
                System.out.println(NUMBER_OF_PARTICLES);
                System.out.println("t " + Math.round(currentTime / deltaT2));
            }
            for( Oscillator o: oscillators) {
                o.particle = o.method.moveParticle(o.particle, currentTime);
                if (shouldPrint) {
                    System.out.println(String.format(
                            "%d %f %d %f", o.id, o.particle.getPositionX(), o.id, 0.4
                    ));
                }
                systemDeltaT = o.deltaT;
            }
            if(shouldPrint)
                printLimits();
            currentTime += systemDeltaT;
        }
    }

    private static void printLimits(){
        System.out.println("1 -1.5 3 0");
        System.out.println("2 1.5 3 0");
        System.out.println("3 -1.5 -5 0");
        System.out.println("4 1.5 -5 0");
    }

    public static void generateSimulationForSolution(double deltaT, double tf, List<Oscillator> oscillators) {
        double currentTime = 0;
        while( currentTime < tf){
            StringBuilder sb = new StringBuilder();
            sb.append(currentTime);
            sb.append(" ");
            for( Oscillator o: oscillators) {
                o.particle = o.method.moveParticle(o.particle, currentTime);
                sb.append(String.format("%f ", o.particle.getPositionX()));
            }
            System.out.println(sb.toString());
            currentTime += deltaT;
        }
    }

    /**
     * Compare the results from the numerical methods with the analytical method
     * @param deltaT
     * @param tf
     * @param oscillators
     * @param analyticalOscillator
     */
    public static void generateSimulationForErrors(
            double deltaT, double tf, List<Oscillator> oscillators, Oscillator analyticalOscillator) {
        double currentTime = 0;

        double[] errorValues = new double[3];

        while(currentTime < tf){
            analyticalOscillator.particle = analyticalOscillator.method.moveParticle(analyticalOscillator.particle, currentTime);
            int counter = 0;
            for(Oscillator o : oscillators){
                o.particle = o.method.moveParticle(o.particle, currentTime);
                errorValues[counter++]+= pow(analyticalOscillator.particle.getPositionX() - o.particle.getPositionX(), 2);
            }
            currentTime+=deltaT;
        }

        for(int i = 0; i< errorValues.length; i++){
            errorValues[i] /= (tf / deltaT);
            System.out.println(String.format("%s;%.4e;%.4e", oscillators.get(i).method.getMethod().description, errorValues[i], deltaT));
        }
    }
}
