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
    final static double STATION_SPEED = 7.12 * 1000;
    final static double LAUNCH_DISTANCE = 1500 * 1000;
    final static double ARRIVE_DISTANCE = 1000 * 1000;
    final static double DAY = 60 * 60 * 24;
    final static double MONTH = 30 * DAY;
    final static double YEAR = DAY * 365;
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

    public void runExperimentSimulations(double tf, double launchSpeed, double launchAngle) {
        System.out.println("minimum distance (m), days since launch, launch day");

        for (double i = 0; i < 3*YEAR && i < tf; i += DAY) {

            updateParticlesLists();

            initializePreviousValues(sun, sunParticles);
            initializePreviousValues(mars, marsParticles);
            initializePreviousValues(earth, earthParticles);

            this.launchAngle = launchAngle;
            this.launchSpeed = launchSpeed;
            this.launchTime = i;
            this.shipLaunched = false;

            simulateWithDistance(tf);
        }
    }

    private void simulateWithDistance(double tf) {
        double currentTime = 0;

        double minDistance = Double.MAX_VALUE;
        double minTime = 0;

        while(currentTime < tf) {
            updateParticlesLists();
            if (!this.shipLaunched && Math.abs(currentTime - this.launchTime) < EPSILON) {
                this.shipLaunched = true;
                locateShip();
            }

            sun = method.moveParticle(sun, sunParticles);
            earth = method.moveParticle(earth, earthParticles);
            mars = method.moveParticle(mars, marsParticles);
            if(this.shipLaunched) {
                this.ship = method.moveParticle(this.ship, this.shipParticles);

                double distance = force.getDistance(mars, ship);
                if (minDistance > distance) {
                    minDistance = distance;
                    minTime = currentTime;
                }
                if (arrived()) {
                    System.out.println(String.format("%f, %d, %d", minDistance, (int) ((minTime - launchTime)/ DAY), (int) (launchTime / DAY)));
                    double speed = Math.sqrt(Math.pow(ship.getVelocityX() / 1000, 2) + Math.pow(ship.getVelocityY() / 1000, 2));
                    System.out.println(String.format("Arrived at speed: %f km/s", speed));
                    return;
                }

            }
            currentTime += deltaT;
        }
        System.out.println(String.format("%f, %d, %d", minDistance, (int) ((minTime - launchTime)/ DAY), (int) (launchTime / DAY)));
    }

    private boolean arrived() {
        return force.getDistance(mars, ship) - mars.getRadius() - ship.getRadius() < ARRIVE_DISTANCE;
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
                            "%d %f %f %f %f %d %d %d", ship.getId(), ship.getPositionX(), ship.getPositionY(), ship.getMass(), ship.getRadius() * 1E7*2, 1, 0, 1
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
        double shipRadius = 50;
        this.ship = new AcceleratedParticle(SPACE_SHIP.getId(), shipRadius, 5E5);
        double angle = Math.atan2(diffY, diffX) + Math.toRadians(this.launchAngle);

        double x = earth.getPositionX() + ((earth.getRadius() + shipRadius + LAUNCH_DISTANCE) * Math.cos(angle));
        double y = earth.getPositionY() + ((earth.getRadius() + shipRadius + LAUNCH_DISTANCE) * Math.sin(angle));

        double speed = launchSpeed;
        double speedX = this.earth.getVelocityX() + (speed + STATION_SPEED) * Math.cos((Math.PI / 2) + angle);
        double speedY = this.earth.getVelocityY() + (speed + STATION_SPEED) * Math.sin((Math.PI / 2) + angle);

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
        if(this.shipLaunched) {
            this.shipParticles.add(mars);
            this.shipParticles.add(sun);
            this.shipParticles.add(earth);

            this.sunParticles.add(ship);
            this.marsParticles.add(ship);
            this.earthParticles.add(ship);
        }
    }

    private boolean arrived(){
        return Gravity.getDistance(this.mars, this.ship) - this.mars.getRadius() - ship.getRadius() < 0;
    }
}
