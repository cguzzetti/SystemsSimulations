package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static ar.edu.itba.ss.g9.tp5.Configuration.*;
import static ar.edu.itba.ss.g9.tp5.PredictiveCollisionAvoidance.*;

public class CollisionAvoidanceSimulation {
    private Point2D.Double goal;
    private int obstaclesAmount;
    private double deltaT;
    private double deltaT2;
    private Set<ObstacleParticle> obstacles;
    private PedestrianParticle pedestrian;

    public CollisionAvoidanceSimulation(int obstaclesAmount, double deltaT, double deltaT2, Optional<Double> dmin, Optional<Double> dmid, Optional<Double> radius) {
        this.obstaclesAmount = obstaclesAmount;
        this.deltaT = deltaT;
        this.deltaT2 = deltaT2;
        if (radius.isPresent()) {
            PERSONAL_SPACE = radius.get();
        }
        this.goal = new Point2D.Double(WIDTH-XSHIFT, HEIGHT - YSHIFT);
        if (dmin.isPresent()) {
            DMIN = dmin.get();
        }
        if (dmid.isPresent()) {
            DMID = dmid.get();
        }
    }

    public void simulate() {
        this.pedestrian = new PedestrianParticle(0, XSHIFT, YSHIFT,0,0, PED_MASS, PED_RADIUS);
        this.obstacles = createObstacleParticles();
        startSimulation();
    }

    private void startSimulation() {
        double currentTime = 0.0;

        while(!pedestrianReachedGoal()) {

            boolean shouldPrint = Math.abs(currentTime / deltaT2 - Math.round(currentTime / deltaT2)) < EPSILON;

            if(shouldPrint) {
                System.out.println(this.obstaclesAmount + 2 + 4);
                System.out.println(String.format("t %f", currentTime));
                System.out.println(pedestrian.toString());
                this.obstacles.forEach(System.out::println);
                System.out.println(String.format(
                        "%d %.3f %.3f %.5f %.5f 0 0 0 0 1",
                        this.obstaclesAmount+1, OBS_RADIUS, OBS_MASS, goal.getX(), goal.getY())
                );
                int initCount = this.obstaclesAmount + 2;
                System.out.println(String.format("%d 0.0001 1 0 0 0 0 1 1 1", initCount));
                System.out.println(String.format("%d 0.0001 1 0 %f 0 0 1 1 1", initCount+1, HEIGHT));
                System.out.println(String.format("%d 0.0001 1 %f 0 0 0 1 1 1", initCount+2, WIDTH));
                System.out.println(String.format("%d 0.0001 1 %f %f 0 0 1 1 1", initCount+3, WIDTH, HEIGHT));
            }

            collidedIds = new LinkedList<>();
            Point2D goalForce = getAutopropulsiveForce(this.pedestrian, goal);
            Point2D wallForce = getWallForce(this.pedestrian);
            Point2D evasiveForce = getElusiveForce(goalForce, wallForce, this.pedestrian, this.obstacles, deltaT);

            Point2D totalForce = Vector.add(goalForce, Vector.add(wallForce, evasiveForce));

            // move all obstacles and pedestrian
            this.pedestrian.move(totalForce, deltaT);
            for (ObstacleParticle obs : obstacles){
                obs.move(deltaT);
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
        double distanceBeetwenObstacles = (WIDTH-XSHIFT*3)/obstaclesAmount;
        while(createdParticles < obstaclesAmount){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = XSHIFT*2 + distanceBeetwenObstacles * createdParticles;
            double y = rand.nextDouble(YSHIFT, HEIGHT-YSHIFT);
            double speed = rand.nextDouble(2,2.5);
            if (rand.nextInt() % 2 == 0 )
                speed = -speed;
            ObstacleParticle obstacle = new ObstacleParticle(createdParticles+1, x, y, 0.0, speed, OBS_MASS, OBS_RADIUS);
            if(obstacle.isValid(obstacles)) {
                obstacles.add(obstacle);
                createdParticles++;
            }
        }
        return obstacles;
    }
}
