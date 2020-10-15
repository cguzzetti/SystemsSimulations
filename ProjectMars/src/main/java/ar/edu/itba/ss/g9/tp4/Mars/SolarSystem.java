package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.*;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import static ar.edu.itba.ss.g9.tp4.Mars.CelestialBodies.*;
import static ar.edu.itba.ss.g9.tp4.RunMode.*;
import static java.lang.Math.*;

public class SolarSystem {

    Force force;
    IntegralMethod method;
    double deltaT;
    final static double SHIP_RADIUS = 50;
    final static double SHIP_MASS = 5E5;
    final static double STATION_SPEED = 7.12 * 1000;
    final static double LAUNCH_DISTANCE = 1500 * 1000;
    final static double ARRIVE_DISTANCE_MARS = 300 * 1000;
    final static double ARRIVE_DISTANCE_JUPITER = 1000 * 1000;
    final static double HOUR = 60 * 60;
    final static double DAY = HOUR * 24;
    final static double MONTH = 30 * DAY;
    final static double YEAR = DAY * 365;
    final static double MARS_MAX_DISTANCE_FROM_SUN = 249000000E3;
    final static double JUPITER_MAX_DISTANCE_FROM_SUN = 817000000E3;
    final static double MARS_RADIUS = 3389E3;
    final static double JUPITER_RADIUS = 69911E3;
    AcceleratedParticle sun;
    AcceleratedParticle earth;
    AcceleratedParticle mars;
    AcceleratedParticle ship;
    AcceleratedParticle jupiter;

    List<AcceleratedParticle> earthParticles;
    List<AcceleratedParticle> marsParticles;
    List<AcceleratedParticle> sunParticles;
    List<AcceleratedParticle> shipParticles;
    List<AcceleratedParticle> jupiterParticles;

    private double launchTime;
    private double launchSpeed;
    private double launchAngle;
    private boolean shipLaunched;

    private final boolean jupiterAsDestiny;

    final static double EPSILON = Math.pow(10, -6);

    List<AcceleratedParticle> mainBodies;
    final static double ENERGY_ERROR = Math.pow(10,-3);

    public SolarSystem(Force force, double deltaT, boolean jupiterAsDestiny) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = new BeemanMethod(force, deltaT);
        this.jupiterAsDestiny = jupiterAsDestiny;

        initializeBodies();

        this.mainBodies = new LinkedList<>();
        this.mainBodies.add(earth);
        this.mainBodies.add(mars);
        this.mainBodies.add(sun);
    }

    private void initializeBodies() {
        // id, position (m), velocity (m/s), radius (m), mass (kg)
        this.sun = new AcceleratedParticle(SUN.getId(), new Point2D.Double(0,0)
                , new Point2D.Double(0,0), 695700E3, 1988500E24);

        this.earth = new AcceleratedParticle(EARTH.getId(), new Point2D.Double(1.493188929636662E+11, 1.318936357931255E+10)
                , new Point2D.Double(-3.113279917782445E+3, 2.955205189256462E+4), 6371E3, 5.97219E24);

        this.mars = new AcceleratedParticle(MARS.getId(), new Point2D.Double(2.059448551842169E+11, 4.023977946528339E+10)
                , new Point2D.Double(-3.717406842095575E+3, 2.584914078301731E+4), MARS_RADIUS, 6.4171E23);

        if(jupiterAsDestiny) {
            this.jupiter = new AcceleratedParticle(JUPITER.getId(), new Point2D.Double(3.658822741863045E+11, -6.743682483255942E+11)
                    , new Point2D.Double(1.133947439887606E+4, 6.854346745596702E+3), JUPITER_RADIUS, 1898.13E24);

        }
    }

    private boolean shipPassedDestinyOrbit() {
        if(!this.shipLaunched)
            return false;
        if(!jupiterAsDestiny)
            return force.getDistance(ship, sun) > MARS_MAX_DISTANCE_FROM_SUN;
        return force.getDistance(ship, sun) > JUPITER_MAX_DISTANCE_FROM_SUN;
    }

    private boolean arrived() {
        if(!this.shipLaunched)
            return false;
        if(!jupiterAsDestiny)
            return force.getDistance(mars, ship) - mars.getRadius() - ship.getRadius() < ARRIVE_DISTANCE_MARS;
        return force.getDistance(jupiter, ship) - jupiter.getRadius() - ship.getRadius() < ARRIVE_DISTANCE_JUPITER;
    }

    private double calculateTotalEnergy() {
        return calculateKineticEnergy() + caclulatePotentialEnergy();
    }

    private double calculateKineticEnergy() {
        double ke = 0;
        for(AcceleratedParticle p: mainBodies) {
            ke += 1/2.0 * p.getMass() * pow(sqrt(pow(p.getVelocityX(),2)+pow(p.getVelocityY(),2)),2);
        }
        return ke;
    }

    private double caclulatePotentialEnergy() {
        double pe = 0;
        pe += force.getForce(earth,mars);
        pe += force.getForce(earth,sun);
        pe += force.getForce(sun,mars);
        return pe;
    }

    public void simulate(double deltaT2, double tf, double launchTime, double launchSpeed, double launchAngle, RunMode mode) {
        this.launchAngle = launchAngle;
        this.launchSpeed = launchSpeed;
        this.launchTime  = launchTime;
        this.shipLaunched = false;

        updateParticlesLists();

        initializePreviousValues(sun, sunParticles);
        initializePreviousValues(mars, marsParticles);
        initializePreviousValues(earth, earthParticles);
        if(jupiterAsDestiny)
            initializePreviousValues(jupiter, jupiterParticles);

        double currentTime = 0;

        double minDistance = Double.MAX_VALUE;
        double minTime = 0;

        double prevEnergy = 0;

        while(currentTime < tf && !shipPassedDestinyOrbit() && !arrived()) {
            updateParticlesLists();
            if (!this.shipLaunched && Math.abs(currentTime - this.launchTime) < EPSILON) {
                this.shipLaunched = true;
                locateShip();
            }

            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

            //esto vuela perror
            if(shouldPrint && mode == MARS_PLANETS) {
                double currEnergy = calculateTotalEnergy();
                double diff = currEnergy - prevEnergy;
                System.out.println(String.format("%2.35e %2.35e", currEnergy, diff));
                prevEnergy = currEnergy;
            }

            shouldPrint = false;

            if(shouldPrint) {
                if(mode == MARS_PLANETS) {
                    int cantBodies = 3;
                    if (this.shipLaunched)
                        cantBodies++;
                    if(jupiterAsDestiny)
                        cantBodies++;

                    System.out.println(cantBodies);

                    System.out.println("t " + Math.round(currentTime / deltaT2));
                    System.out.println(String.format(
                            "%d %f %f %f %f %f %f %f", sun.getId(), sun.getPositionX(), sun.getPositionY(), sun.getMass(), sun.getRadius() * 10 * 2, 1.0, 1.0, 0.0
                    ));
                    System.out.println(String.format(
                            "%d %f %f %f %f %f %f %f", mars.getId(), mars.getPositionX(), mars.getPositionY(), mars.getMass(), mars.getRadius() * 500 * 2, 1.0, 0.0, 0.0
                    ));
                    System.out.println(String.format(
                            "%d %f %f %f %f %f %f %f", earth.getId(), earth.getPositionX(), earth.getPositionY(), earth.getMass(), earth.getRadius() * 500 * 2, 0.0, 0.0, 1.0
                    ));
                    if(jupiterAsDestiny) {
                        System.out.println(String.format(
                                "%d %f %f %f %f %f %f %f", jupiter.getId(), jupiter.getPositionX(), jupiter.getPositionY(), jupiter.getMass(), jupiter.getRadius() * 100, 1.0, 0.65, 0.0
                        ));
                    }
                    if (this.shipLaunched) {
                        System.out.println(String.format(
                                "%d %f %f %f %f %f %f %f %f", ship.getId(), ship.getPositionX(), ship.getPositionY(), ship.getMass(), ship.getRadius() * 1E7 * 2 * 3, 1.0, 0.0, 1.0, force.getDistance(ship, mars)
                        ));
                    }
                }
                else if(this.shipLaunched && mode == SHIP_VELOCITY) {
                    if(!arrived()) {
                        System.out.println(String.format(
                                "%d %f", (int) ((currentTime - launchTime) / DAY), Math.sqrt(pow(ship.getVelocityX() / 1000, 2) + pow(ship.getVelocityY() / 1000, 2)) // km/s
                        ));
                    }
                }
            }



            if(this.shipLaunched) { // print minDistance
                if(mode == FIND_LAUNCH) {
                    AcceleratedParticle destiny = jupiterAsDestiny? jupiter:mars;
                    double distance = force.getDistance(ship, destiny);
                    if (minDistance > distance) {
                        minDistance = distance;
                        minTime = currentTime;
                    }
                    if (arrived()) {
                        double arrivalSpeed = Math.sqrt(Math.pow(ship.getVelocityX() / 1000, 2) + Math.pow(ship.getVelocityY() / 1000, 2)); // km/s
                        System.out.println(String.format("%f, %d, %d, %f", minDistance, (int) ((minTime - launchTime)/ DAY), (int) (launchTime / DAY), arrivalSpeed));
                     //   System.out.println(String.format("%f, %d, %d, %d, %f", minDistance, (int) ((minTime - launchTime)/ HOUR), (int) (launchTime / HOUR), (int) (launchTime / DAY), arrivalSpeed));
                     //   System.out.println(String.format("%f, %d, %d, %d, %f", minDistance, (int) ((minTime - launchTime)/ 60), (int) (launchTime / 60), (int) (launchTime / HOUR), arrivalSpeed));
                        return;
                    }
                }
            }

            AcceleratedParticle auxSun = method.moveParticle(sun, sunParticles);
            AcceleratedParticle auxEarth = method.moveParticle(earth, earthParticles);
            AcceleratedParticle auxMars = method.moveParticle(mars, marsParticles);
            AcceleratedParticle auxJupiter = null;
            if(jupiterAsDestiny) {
                auxJupiter = method.moveParticle(jupiter, jupiterParticles);
            }
            if(this.shipLaunched) {
                AcceleratedParticle auxShip = method.moveParticle(ship, shipParticles);
                this.ship = auxShip;
            }
            this.sun = auxSun;
            this.earth = auxEarth;
            this.mars = auxMars;
            if(jupiterAsDestiny) {
                this.jupiter = auxJupiter;
            }

            currentTime += deltaT;
        }
        if(mode == FIND_LAUNCH && this.shipLaunched) { //print minDistance
              System.out.println(String.format("%f, %d, %d", minDistance, (int) ((minTime - launchTime) / DAY), (int) (launchTime / DAY)));
           // System.out.println(String.format("%f, %d, %d, %d", minDistance, (int) ((minTime - launchTime)/ HOUR), (int) (launchTime / HOUR), (int) (launchTime / DAY)));
           // System.out.println(String.format("%f, %d, %d, %d", minDistance, (int) ((minTime - launchTime)/ 60), (int) (launchTime / 60), (int) (launchTime / HOUR)));
        }
        if(mode == SHIP_VELOCITY && arrived()) {
            AcceleratedParticle destiny = jupiterAsDestiny? jupiter:mars;
            System.out.println(String.format(
                    "%d %f %f %f", (int) ((currentTime - launchTime) / DAY), ship.getVelocityX() / 1000, ship.getVelocityY() / 1000, force.getDistance(destiny, ship) - destiny.getRadius() - ship.getRadius() // km/s
            ));
        }
    }

    private void locateShip(){
        double diffX = this.earth.getPositionX() - this.sun.getPositionX();
        double diffY = this.earth.getPositionY() - this.sun.getPositionY();

        this.ship = new AcceleratedParticle(SPACE_SHIP.getId(), SHIP_RADIUS, SHIP_MASS);

        double angle = Math.atan2(diffY, diffX) + Math.toRadians(this.launchAngle);

        double x = earth.getPositionX() + ((earth.getRadius() + SHIP_RADIUS + LAUNCH_DISTANCE) * Math.cos(angle));
        double y = earth.getPositionY() + ((earth.getRadius() + SHIP_RADIUS + LAUNCH_DISTANCE) * Math.sin(angle));

        double speedX = this.earth.getVelocityX() + (launchSpeed + STATION_SPEED) * Math.cos((Math.PI / 2) + angle);
        double speedY = this.earth.getVelocityY() + (launchSpeed + STATION_SPEED) * Math.sin((Math.PI / 2) + angle);

        ship.setPosition(new Point2D.Double(x, y));
        ship.setVelocity(new Point2D.Double(speedX, speedY));

        initializePreviousValues(ship, shipParticles);
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

        this.jupiterParticles = new LinkedList<>();
        if(jupiterAsDestiny) {
            this.jupiterParticles.add(mars);
            this.jupiterParticles.add(earth);
            this.jupiterParticles.add(sun);

            this.sunParticles.add(jupiter);
            this.marsParticles.add(jupiter);
            this.earthParticles.add(jupiter);
        }

        this.shipParticles = new LinkedList<>();
        if(this.shipLaunched) {
            this.shipParticles.add(mars);
            this.shipParticles.add(sun);
            this.shipParticles.add(earth);
            if(jupiterAsDestiny)
                this.shipParticles.add(jupiter);

            this.sunParticles.add(ship);
            this.marsParticles.add(ship);
            this.earthParticles.add(ship);
            if(jupiterAsDestiny) {
                this.jupiterParticles.add(ship);
            }
        }
    }
}
