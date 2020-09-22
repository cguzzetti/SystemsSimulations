package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.*;


import com.sun.scenario.effect.impl.state.GaussianShadowState;
import javafx.geometry.Point2D;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static ar.edu.itba.ss.g9.tp3.GasDiffusionFileParser.getFilePath;

public class GasDiffusion {
   // private static Logger logger = LoggerFactory.getLogger(GasDiffusionFileParser.class);
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Collision> collisions;
    private Set<GasParticle> particles;
    private Point2D[][] verticalWalls;
    private Point2D[][] horizontalWalls;
    private double currentTime;

    public GasDiffusion(GasDifussionConfig config, Set<GasParticle> particles){
        this.N = config.getN();
        this.height = config.getHeight();
        this.width = config.getWidth();
        this.partitionLen = config.getPartitionLen();
        this.collisions = new PriorityQueue<>(new Comparator<Collision>() {
            @Override
            public int compare(Collision o1, Collision o2) {
                return Double.compare(o1.getTime(),o2.getTime());
            }
        });
        this.particles = particles;
        this.horizontalWalls = new Point2D[][]{
                {Point2D.ZERO, new Point2D(config.getWidth(), 0)},
                {new Point2D(0, config.getHeight()), new Point2D(config.getWidth(), config.getHeight())},
        };
        this.verticalWalls = new Point2D[][]{
                {Point2D.ZERO, new Point2D(0, config.getHeight())},
                {new Point2D(config.getWidth() / 2, 0), new Point2D(config.getWidth() / 2, config.getHeight() / 2 - config.getPartitionLen() / 2)},
                {new Point2D(config.getWidth() / 2, config.getHeight() / 2 + config.getPartitionLen() / 2), new Point2D(config.getWidth() / 2, config.getHeight())},
                {new Point2D(config.getWidth(), 0), new Point2D(config.getWidth(), config.getHeight())}
        };
        this.currentTime = 0.0;
    }

    public void simulate(GasDiffusionFileParser parser){
        // Wall particles
        Set<GasParticle> wallLimitsParticles = new HashSet<>();
        wallLimitsParticles.add(new GasParticle(horizontalWalls[0][0].getX(), horizontalWalls[0][0].getY(),0,0,-1,1));
        wallLimitsParticles.add(new GasParticle(horizontalWalls[0][1].getX(), horizontalWalls[0][1].getY(),0,0,-1,1));
        wallLimitsParticles.add(new GasParticle(horizontalWalls[1][0].getX(), horizontalWalls[1][0].getY(),0,0,-1,1));
        wallLimitsParticles.add(new GasParticle(horizontalWalls[1][1].getX(), horizontalWalls[1][1].getY(),0,0,-1,1));


        GasMetricsEngine metricsEngine = new GasMetricsEngine(
                ExperimentType.FP, getFilePath("fpExperiment.txt")
        );

        metricsEngine.writeIterationHeader();

        long simulationStart = System.currentTimeMillis();
        int iteration = 0;
        calculateCollisions(this.particles, currentTime);
        if(collisions.isEmpty()) return;


        // Wall particles
        Set<GasParticle> allParticles = new HashSet<>();
        allParticles.addAll(wallLimitsParticles);
        allParticles.addAll(particles);

        parser.writeStateToOutput(allParticles, iteration++);
        Double fp = calculateParticleFraction();

        while(fp - 0.5 > 0.0001){
            if(collisions.isEmpty()) {
                //logger.error("No more collisions to show!");
                System.out.printf("No more collisions to show! Exiting at t=%f\n", currentTime);
                break;
            }
            Optional<Collision> maybeCollision = getCollisionIfValid(collisions.poll());
            if(maybeCollision.isEmpty()) continue;

            // We only care about the FP if its a valid collision
            metricsEngine.writeFP(1 - fp, System.currentTimeMillis()-simulationStart);
            Collision collision = maybeCollision.get();

            Set<GasParticle> particlesInCollision = collision.getParticles();

            // Time calculation
            // * We need deltaT in order to advance all particles
            // * We need to update the current time before
            //   calculating future collisions of particles
            //   involved in current collision
            double deltaT = collision.getTime() - currentTime;
            currentTime += deltaT;

            particlesInCollision.forEach(GasParticle::incrementCollisionCounter); // Increase collision counter for particles involved in current collision
            advanceParticles(deltaT); // Advance all particles
            collision.updateVelocity(); // Update velocity of particles involved in current collision
            calculateCollisions(particlesInCollision, currentTime); // Determine future collisions of particles involved in current collision


            //Wall particles
            allParticles.clear();
            allParticles.addAll(wallLimitsParticles);
            allParticles.addAll(particles);
            parser.writeStateToOutput(allParticles, iteration);
            fp = calculateParticleFraction();
            iteration++;
        }

        parser.finish();

    }

    // Calculates the next collision for all particles
     private void calculateCollisions(Set<GasParticle> particles, double timeSoFar) {
        for(GasParticle p: particles) {
            this.collisions.addAll(p.calculateParticleNextCollision(this.particles, this.verticalWalls, this.horizontalWalls, timeSoFar));
        }
    }

    private Optional<Collision> getCollisionIfValid(Collision collision){
        if(collision.isValid())
            return Optional.of(collision);

        return Optional.empty();
    }

    private void advanceParticles(double time) {
        for(GasParticle p: particles) {
            p.updateParticlePosition(time);
        }
    }

    private double calculateParticleFraction() {
        int particleCounterLeftSide = 0;
        for(Particle p: particles) {
            if(p. getX() < width/2)
                particleCounterLeftSide++;
        }
        return (double) particleCounterLeftSide /particles.size();
    }

    public double getHeight() {
        return height;
    }

    public int getN() {
        return N;
    }

    public double getWidth() {
        return width;
    }

    public double getPartitionLen() {
        return partitionLen;
    }


    public Queue<Collision> getCollisions() {
        return collisions;
    }

    public void setCollisions(PriorityQueue<Collision> collisions) {
        this.collisions = collisions;
    }
}
