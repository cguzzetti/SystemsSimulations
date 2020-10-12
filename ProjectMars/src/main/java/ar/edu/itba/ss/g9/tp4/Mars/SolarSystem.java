package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.*;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.ss.g9.tp4.Mars.CelestialBodies.*;
import static java.lang.Math.pow;

public class SolarSystem {

    Force force;
    IntegralMethod method;
    double deltaT;
    final static double LAUNCH_DISTANCE = 1500 * 1000;
    AcceleratedParticle sun;
    AcceleratedParticle earth;
    AcceleratedParticle mars;
    AcceleratedParticle ship;

    List<AcceleratedParticle> earthParticles;
    List<AcceleratedParticle> marsParticles;
    List<AcceleratedParticle> sunParticles;
    List<AcceleratedParticle> shipParticles;

    private double launchTime;
    private double launchSpeed;
    private double launchAngle;
    private boolean shipLaunched;

    final static double EPSILON = Math.pow(10, -6);


    public SolarSystem(Force force, IntegralMethods method, double deltaT) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = new BeemanMethod(force, deltaT);

        initializeBodies();
    }

    private void initializeBodies() {
        // id, position (m), velocity (m/s), radius (m), mass (kg)
        this.sun = new AcceleratedParticle(SUN.getId(), new Point2D.Double(0,0)
                , new Point2D.Double(0,0), 695700E3, 1988500E24);

        this.earth = new AcceleratedParticle(EARTH.getId(), new Point2D.Double(1.493188929636662E+11, 1.318936357931255E+10)
                , new Point2D.Double(-3.113279917782445E+3, 2.955205189256462E+4), 6371E3, 5.97219E24);

        this.mars = new AcceleratedParticle(MARS.getId(), new Point2D.Double(2.059448551842169E+11, 4.023977946528339E+10)
                , new Point2D.Double(-3.717406842095575E+3, 2.584914078301731E+4), 3389E3, 6.4171E23);
    }

    public void simulate(double deltaT2, double tf, double launchTime, double launchSpeed, double launchAngle) {
        this.launchAngle = launchAngle;
        this.launchSpeed = launchSpeed;
        this.launchTime  = launchTime;
        this.shipLaunched = false;

        updateParticlesLists();

        initializePreviousValues(sun, sunParticles);
        initializePreviousValues(mars, marsParticles);
        initializePreviousValues(earth, earthParticles);

        double currentTime = 0;

        while(currentTime < tf) {
            updateParticlesLists();
            if (!this.shipLaunched && Math.abs(currentTime - this.launchTime) < EPSILON) {
                this.shipLaunched = true;
                locateShip();
            }

            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

            if(shouldPrint) {

                if(this.shipLaunched)
                    System.out.println(4);
                else
                    System.out.println(3);

                System.out.println("t " + Math.round(currentTime / deltaT2));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", sun.getId(), sun.getPositionX(), sun.getPositionY(), sun.getMass(), sun.getRadius() * 10, 1, 1, 0
                ));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", mars.getId(), mars.getPositionX(), mars.getPositionY(), mars.getMass(), mars.getRadius() * 500, 1, 0, 0
                ));
                System.out.println(String.format(
                        "%d %f %f %f %f %d %d %d", earth.getId(), earth.getPositionX(), earth.getPositionY(), earth.getMass(), earth.getRadius() * 500, 0, 0, 1
                ));
                if(this.shipLaunched) {
                    System.out.println(String.format(
                            "%d %f %f %f %f %d %d %d", ship.getId(), ship.getPositionX(), ship.getPositionY(), ship.getMass(), ship.getRadius() * 500, 1, 0, 1
                    ));
                }
            }

            sun = method.moveParticle(sun, sunParticles);
            earth = method.moveParticle(earth, earthParticles);
            mars = method.moveParticle(mars, marsParticles);
            if(this.shipLaunched)
                this.ship = method.moveParticle(this.ship, this.shipParticles);

            currentTime += deltaT;
        }

    }


    private void locateShip(){
        double diffX = this.earth.getPositionX() - this.sun.getPositionX();
        double diffY = this.earth.getPositionY() - this.sun.getPositionY();
        double shipRadius = 6371E3;
        this.ship = new AcceleratedParticle(SPACE_SHIP.getId(), shipRadius, 2E5);
        double angle = Math.atan2(diffY, diffX) + Math.toRadians(this.launchAngle);

        double x = earth.getPositionX() + ((earth.getRadius() + shipRadius + LAUNCH_DISTANCE) * Math.cos(angle));
        double y = earth.getPositionY() + ((earth.getRadius() + shipRadius + LAUNCH_DISTANCE) * Math.sin(angle));

        double speed = launchSpeed * 1000; // m/s
        double speedX = this.earth.getVelocityX() + speed * Math.cos((Math.PI / 2) + angle);
        double speedY = this.earth.getVelocityY() + speed * Math.sin((Math.PI / 2) + angle);

        ship.setPosition(new Point2D.Double(x, y));
        ship.setVelocity(new Point2D.Double(speedX, speedY));

        initializePreviousValues(ship, this.shipParticles);
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

        this.shipParticles = new LinkedList<>();
        this.shipParticles.add(mars);
        this.shipParticles.add(sun);
        this.shipParticles.add(earth);

    }
}
