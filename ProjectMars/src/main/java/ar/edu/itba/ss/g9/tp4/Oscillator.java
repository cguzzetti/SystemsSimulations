package ar.edu.itba.ss.g9.tp4;

import java.awt.geom.Point2D;
import static java.lang.Math.pow;

public class Oscillator {
    Force force;
    IntegralMethods method;
    double k;
    double g;
    double mass;
    double deltaT;

    final static double EPSILON = Math.pow(10, -6);

    AcceleratedParticle particle;

    public Oscillator(Force force, IntegralMethods method, double k, double g, double m, double deltaT) {
        this.force = force;
        this.method = method;
        this.k = k;
        this.g = g;
        this.mass = m;
        this.deltaT = deltaT;
        Point2D.Double position = new Point2D.Double(1, 0);
        Point2D.Double velocity = new Point2D.Double(-(g/ (2 * mass)), 0);
        particle = new AcceleratedParticle(1, position, velocity, 0, mass);
    }

    public void initializePreviousValues(){
        double prevX =
                particle.getPositionX()
                        - deltaT * particle.getVelocityY()
                        + pow(deltaT, 2) * force.getForceX(0, particle) / (2*particle.getMass());
        double prevY = 0;
        particle.setPosition(new Point2D.Double(prevX, prevY));

        particle.setVelocity(new Point2D.Double(
                particle.getVelocityX() - (deltaT / particle.getMass()) * force.getForceX(0, particle),
                0
        ));
    }

    public void initializeEquationsTables() {
        particle.rListX[0] = particle.getPositionX();
        particle.rListX[1] = particle.getVelocityX();
        particle.rListX[2] = (force.getForceX(0, particle)) / particle.getMass();
        particle.rListX[3] = -(k / mass) * particle.rListX[1] - (g / mass) * particle.rListX[2];
        particle.rListX[4] = -(k / mass) * particle.rListX[2] - (g / mass) * particle.rListX[3];
        particle.rListX[5] = -(k / mass) * particle.rListX[3] - (g / mass) * particle.rListX[4];
    }

    public void generateSimulationForVisualization(double deltaT2, double tf) {
        double currentTime = 0;

        while( currentTime < tf){
            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;
            if(shouldPrint){
                System.out.println(5);
                System.out.println("t " + Math.round(currentTime / deltaT2));
            }
            IntegralMethod  integralMethod = new GearMethodPredictor(deltaT, force);
            this.particle =  integralMethod.moveParticle(this.particle, currentTime);
            if(shouldPrint){
                System.out.println("0 " + this.particle.getPositionX() + " " + 0 + " " + 0.4);
                printLimits();
            }
            currentTime+=deltaT;
        }

    }

    private void printLimits(){
        System.out.println("1 -1.5 3 0");
        System.out.println("2 1.5 3 0");
        System.out.println("3 -1.5 -5 0");
        System.out.println("4 1.5 -5 0");
    }
}
