package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.*;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;

public class SolarSystem {

    Force force;
    IntegralMethod method;
    double deltaT;

    AcceleratedParticle sun;
    AcceleratedParticle earth;
    AcceleratedParticle mars;

    List<AcceleratedParticle> earthParticles;
    List<AcceleratedParticle> marsParticles;
    List<AcceleratedParticle> sunParticles;

    final static double EPSILON = Math.pow(10, -6);


    public SolarSystem(Force force, IntegralMethods method, double deltaT) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = new BeemanMethod(force, deltaT);

        initializeBodies();
    }

    private void initializeBodies() {
        // id, position (m), velocity (m/s), radius (m), mass (kg)
        this.sun = new AcceleratedParticle(0, new Point2D.Double(0,0)
                , new Point2D.Double(0,0), 695700E3, 1988500E24);

        this.earth = new AcceleratedParticle(1, new Point2D.Double(1.493188929636662E+11, 1.318936357931255E+10)
                , new Point2D.Double(-3.113279917782445E+3, 2.955205189256462E+4), 6371E3, 5.97219E24);

        this.mars = new AcceleratedParticle(2, new Point2D.Double(2.059448551842169E+11, 4.023977946528339E+10)
                , new Point2D.Double(-3.717406842095575E+3, 2.584914078301731E+4), 3389E3, 6.4171E23);
    }

    public void simulate(double deltaT2, double tf) {
        updateParticlesLists();

        initializePreviousValues(sun, sunParticles);
        initializePreviousValues(mars, marsParticles);
        initializePreviousValues(earth, earthParticles);

        double currentTime = 0;

        while(currentTime < tf) {
            updateParticlesLists();

            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

            if(shouldPrint) {
                System.out.println(3);
                System.out.println("t " + Math.round(currentTime / deltaT2));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", sun.getId(), sun.getPositionX(), sun.getPositionY(), sun.getMass(), sun.getRadius(), 1, 1, 0
                ));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", mars.getId(), mars.getPositionX(), mars.getPositionY(), mars.getMass(), mars.getRadius(), 1, 0, 0
                ));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", earth.getId(), earth.getPositionX(), earth.getPositionY(), earth.getMass(), earth.getRadius(), 0, 0, 1
                ));
            }

            sun = method.moveParticle(sun, sunParticles);
            earth = method.moveParticle(earth, earthParticles);
            mars = method.moveParticle(mars, marsParticles);

            currentTime += deltaT;
        }

    }

    private void initializePreviousValues(AcceleratedParticle particle, List<AcceleratedParticle> otherParticles){
        double prevX = particle.getPositionX()
                - deltaT * particle.getVelocityY()
                + pow(deltaT, 2) * force.getForceX(otherParticles, particle) / (2 * particle.getMass());
        double prevY = particle.getPositionY()
                - deltaT *particle.getVelocityY()
                + pow(deltaT, 2) * force.getForceY(otherParticles, particle) / (2 * particle.getMass());

        particle.setPrevPosition(new Point2D.Double(prevX, prevY));
        particle.setPrevVelocity(new Point2D.Double(
                particle.getVelocityX() - (deltaT / particle.getMass()) * force.getForceX(otherParticles, particle),
                particle.getVelocityY() - (deltaT / particle.getMass()) * force.getForceY(otherParticles, particle)
        ));
    }

    private void updateParticlesLists() {
        this.earthParticles = new LinkedList<>();
        this.earthParticles.add(mars);
        this.earthParticles.add(sun);

        this.marsParticles = new LinkedList<>();
        this.marsParticles.add(earth);
        this.marsParticles.add(sun);

        this.sunParticles = new LinkedList<>();
        this.sunParticles.add(mars);
        this.sunParticles.add(earth);

    }
}
