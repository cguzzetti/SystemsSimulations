package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static java.lang.Math.*;

public class PredictiveCollisionAvoidance {

    private static final double TIME_LIMIT          = 1.5;
    // Personal space valid range: [1.2m, 3.6m]
    private static final double PERSONAL_SPACE      = 0.4;  // TODO: chequear
    // Take the first 5 as seen in class
    private static final int NUMBER_OF_CRASHES      = 3;
    // Velocity desired by pedestrian
    private static final double DESIRED_VELOCITY    = 1.2; // TODO: chequear
    private static final double TAU                 = 0.3;
    private static final double WALL_SAFE_DISTANCE        = 0.05;

    // Algorithm for pedestrian p
    // 1. Compute the set of pedestrians that are on collision course with p with anticipation time t
    // 2. Select first N pedestrians that will collide by sorting in order of increasing collision time
    // 3. Show how the pedestrian p can avoid a potential collision with another pedestrian by selecting the evasive force
    // 4. Compute the total evasive force that is applied to p

    // The pedestrian always moves towards the goal
    public static Point2D applyAutopropulsiveForce(Particle p, Point2D goal){
        double auxX = goal.getX() - p.getPosX();
        double auxY = goal.getY() - p.getPosY();

        double distanceToGoal = sqrt(pow(auxX, 2) + pow(auxY, 2));

        double xValue = (DESIRED_VELOCITY * auxX/distanceToGoal - p.getVx()) / TAU;
        double yValue = (DESIRED_VELOCITY * auxY/distanceToGoal - p.getVy()) / TAU;

        return new Point2D.Double(xValue, yValue);
    }

    private static double getWallForceMagnitude(double radius, double distance, double safeDistance) {
        if(Double.compare(distance - radius, safeDistance) >= 0)
            return 0;
        return (safeDistance + radius - distance) / Math.pow(distance - radius, 2);
    }

    public static Point2D getWallForce(Particle particle){
        Point2D wallForce = new Point2D.Double(0,0);
        Point2D normalUnitVector;
        double wallForceMagnitude;

        /* Horizontal walls */
        normalUnitVector = new Point2D.Double(0, 1);
        wallForceMagnitude = getWallForceMagnitude(particle.getRadius(), particle.getPosY(), WALL_SAFE_DISTANCE);
        wallForce = Vector.add(wallForce, Vector.scalarMultiplication(normalUnitVector, wallForceMagnitude));

        normalUnitVector = new Point2D.Double(0, -1);
        wallForceMagnitude = getWallForceMagnitude(particle.getRadius(), CollisionAvoidanceSimulation.HEIGHT - particle.getPosY(), WALL_SAFE_DISTANCE);
        wallForce = Vector.add(wallForce, Vector.scalarMultiplication(normalUnitVector, wallForceMagnitude));

        /* Vertical walls */
        normalUnitVector = new Point2D.Double(1, 0);
        wallForceMagnitude = getWallForceMagnitude(particle.getRadius(), particle.getPosX(), WALL_SAFE_DISTANCE);
        wallForce = Vector.add(wallForce, Vector.scalarMultiplication(normalUnitVector, wallForceMagnitude));

        normalUnitVector = new Point2D.Double(-1, 0);
        wallForceMagnitude = getWallForceMagnitude(particle.getRadius(), CollisionAvoidanceSimulation.WIDTH - particle.getPosX(), WALL_SAFE_DISTANCE);
        wallForce = Vector.add(wallForce, Vector.scalarMultiplication(normalUnitVector, wallForceMagnitude));

        return wallForce;
    }

    private static Point2D getEvasiveForce(Particle particle, ObstacleParticle otherP, double crashTime, Point2D desiredVelocity){
        Point2D c_i = Vector.add(particle.getPosition(), Vector.scalarMultiplication(desiredVelocity, crashTime));
        Point2D c_j = Vector.add(otherP.getPosition(), Vector.scalarMultiplication(otherP.getVelocity(), crashTime));

        Point2D forceDirection = Vector.normalize(Vector.subtract(c_i, c_j));

        double D = Vector.getNorm(Vector.subtract(c_i, particle.getPosition()))
                + Vector.getNorm(Vector.subtract(c_i, c_j))
                - particle.getRadius() - otherP.getRadius();
        double d_min = particle.getRadius(); // PERSONAL_SPACE - particle.getRadius();
        double d_mid = 1; //d_min * 1.5;
        double d_max = 2;// d_min * 2;
        double forceMagnitude = 0;
        double multiplier = 4;
        if(D < d_min) {
            forceMagnitude = 1/(D*D) * multiplier;
        } else if(D < d_mid) {
            forceMagnitude = 1/(d_min*d_min) * multiplier;
        } else if(D < d_max) {
            forceMagnitude = (D - d_max) / (d_min*d_min * (d_mid - d_max)) * multiplier;
        }

        return Vector.scalarMultiplication(forceDirection, forceMagnitude);

    }
    public static Point2D applyElusiveForce(Point2D force, Particle p, Set<ObstacleParticle> otherParticles, double deltaT){

        List<Crash> crashes = new ArrayList<>();

        // v_i^des = v_i + (sum[F_walls] + F_goal) * deltaT
        Point2D desiredVelocity = Vector.add(p.getVelocity(), Vector.scalarMultiplication(Vector.add(getWallForce(p), force), deltaT));

        // 1. Compute the set of pedestrians that are on
        // collision course with p with anticipation time TIME_LIMIT
        for(ObstacleParticle otherP : otherParticles){
            Crash crash = predictCrash(p, otherP, desiredVelocity);
            if(crash != null)
                crashes.add(crash);
        }

        // 2. Select first N pedestrians that will collide
        // by sorting in order of increasing collision time
        Collections.sort(crashes);
        if(crashes.size() > NUMBER_OF_CRASHES)
            crashes = crashes.subList(0, NUMBER_OF_CRASHES);

        Point2D accumulatedEvasiveForce = new Point2D.Double(0,0);
        int processedCollisions = 0;

        // 3. Show how the pedestrian p can avoid a potential
        // collision with another pedestrian by selecting the
        // evasive force
        for(Crash crash: crashes){
            /* Collisions may now not occur due to evasive action in others */
            Crash reprocessedCrash = predictCrash(p, crash.getParticle(), desiredVelocity);
            if(reprocessedCrash != null) {
                Point2D evasiveForce = getEvasiveForce(p, crash.getParticle(),
                        crash.getTime(), desiredVelocity);

                desiredVelocity = Vector.add(desiredVelocity, Vector.scalarMultiplication(evasiveForce, deltaT));
                accumulatedEvasiveForce = Vector.add(accumulatedEvasiveForce, evasiveForce);
                processedCollisions++;
            }
        }

        if(processedCollisions == 0)
            return accumulatedEvasiveForce;

        // 4. Compute the total evasive force that is applied to p
        return Vector.scalarDivsion(accumulatedEvasiveForce, processedCollisions);
    }


    private static Crash predictCrash(Particle particle, ObstacleParticle other, Point2D desiredVelocity) {
        Point2D vel = Vector.subtract(desiredVelocity, other.getVelocity());

        Point2D diffPos = Vector.subtract(particle.getPosition(), other.getPosition());

        double a = pow(Vector.getNorm(vel), 2);
        double b = 2 * Vector.getDotProduct(vel, diffPos);
        double c = pow(Vector.getNorm(diffPos), 2) - Math.pow(PERSONAL_SPACE + other.getRadius(), 2);

        double det = b*b - 4*a*c;

        /* Collision may take place */
        if(Double.compare(det, 0) > 0) {
            double t1 = (-b + Math.sqrt(det)) / (2*a);
            double t2 = (-b - Math.sqrt(det)) / (2*a);

            if((t1 < 0 && t2 > 0) || (t2 < 0 && t1 > 0)) {
                return new Crash(other, 0);
            } else if(Double.compare(t1, 0) >= 0 && Double.compare(t2, 0) >= 0) {
                double minT = Math.min(t1, t2);
                if(minT < TIME_LIMIT) {
                    return new Crash(other, minT);
                }
            }
        }
        return null;
    }
}
