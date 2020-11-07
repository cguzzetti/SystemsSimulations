package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static ar.edu.itba.ss.g9.tp5.PredictiveCollisionAvoidance.applyAutopropulsiveForce;
import static ar.edu.itba.ss.g9.tp5.PredictiveCollisionAvoidance.applyElusiveForce;

public class CollisionAvoidanceSimulation {
    private Point2D.Double goal;
    private int obstaclesAmount;
    private double deltaT;
    private double deltaT2;
    static double HEIGHT = 7, WIDTH = 15, SHIFT = 2;
    private Set<ObstacleParticle> obstacles;
    private PedestrianParticle pedestrian;
    private static double OBS_RADIUS = 0.2;
    private static double OBS_MASS = 1;
    private static double OBS_SPEED = 1;
    private static double PED_RADIUS = OBS_RADIUS;
    private static double PED_MASS = OBS_MASS;
    final static double EPSILON = Math.pow(10, -6);

    public CollisionAvoidanceSimulation(int obstaclesAmount, double deltaT, double deltaT2) {
        this.obstaclesAmount = obstaclesAmount;
        this.goal = new Point2D.Double(WIDTH-0.5, HEIGHT/2);
        this.deltaT = deltaT;
        this.deltaT2 = deltaT2;
    }

    public void simulate() {
        this.pedestrian = new PedestrianParticle(0, 0, HEIGHT/2,1,1, PED_MASS, PED_RADIUS); // TODO: check appropiate values
        this.obstacles = createObstacleParticles();
        startSimulation();
    }

    private void startSimulation() {
        // Variables for point c of the assignment
        double timeTraveled = 0.0;
        double lengthTraveled = 0.0;
        double meanSpeed;
        Point2D goalForce;
//        Point2D evasiveForce;

        double currentTime = 0.0;

        while(!pedestrianReachedGoal()) {

            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

            if(shouldPrint) {
                System.out.println(this.obstaclesAmount + 2);
                System.out.println(String.format("t %f", currentTime));
                System.out.println(pedestrian.toString());
                this.obstacles.forEach(System.out::println);
                System.out.println(String.format(
                        "%d %.3f %.3f %.5f %.5f 0 0 0 0 1",
                        this.obstaclesAmount+1, OBS_RADIUS, OBS_MASS, goal.getX(), goal.getY())
                );
            }


            goalForce = applyAutopropulsiveForce(this.pedestrian, goal);
            Point2D evasiveForce = applyElusiveForce(goalForce, this.pedestrian, this.obstacles, deltaT);

            // update variables for point c of the assigment

            // move all obstacles and pedestrian
            this.pedestrian.move(Vector.add(evasiveForce, goalForce), deltaT);
            for (ObstacleParticle obs : obstacles){
                obs.move(goalForce, deltaT);
            }
            currentTime += deltaT;
        }
    }

    private boolean pedestrianReachedGoal() {
        return this.pedestrian.getPosition().distance(goal) - this.pedestrian.getRadius()*2 < 0;
    }

    private Set<ObstacleParticle> createObstacleParticles() {
        Set<ObstacleParticle> obstacles = new HashSet<>();
        int createdParticles = 0;
        while( createdParticles < obstaclesAmount){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(SHIFT, WIDTH-SHIFT);
            double y = rand.nextDouble(SHIFT, HEIGHT-SHIFT);
            ObstacleParticle obstacle = new ObstacleParticle(createdParticles+1, x, y, 0.0, OBS_SPEED, OBS_MASS, OBS_RADIUS);
            if(obstacle.isValid(obstacles)) {
                obstacles.add(obstacle);
                createdParticles++;
            }
        }
        return obstacles;
    }
}
