package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.*;


import javafx.geometry.Point2D;
import java.util.*;

public class GasDiffusion {
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Collision> collisions;
    private Set<GasParticle> particles;
    private Point2D[][] verticalWalls;
    private Point2D[][] horizontalWalls;

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
    }

    public void simulate(GasDiffusionFileParser parser){
        double time = 0;
        int iteration = 0;
        calculateCollisions(this.particles);
        if(collisions.isEmpty()) return;

        parser.writeStateToOutput(particles, iteration++);

        // TODO: EndCondition won't be time but fp
        while(time < 1000){
            double fp = calculateParticleFraction();
            // Get first valid collision
            if(collisions.isEmpty()) break;
            Optional<Collision> maybeCollision = getCollisionIfValid(collisions.poll());
            if(maybeCollision.isEmpty()) continue;
            Collision collision = maybeCollision.get();
            System.out.println(collision);


            Set<GasParticle> particlesInCollision = collision.getParticles();
            double deltaT = collision.getTime() - time;

            // For particles involved in current collision:
            collision.updateVelocity(); // Update velocity
            //calculateCollisions(particlesInCollision); // Determine future collisions // TODO: has to consider time until now
            particlesInCollision.forEach(GasParticle::incrementCollisionCounter); // Increase collision counter
            advanceParticles(deltaT);

            //if(collision instanceof ParticleCollision)
            parser.writeStateToOutput(particles, iteration);

            // TODO: time won't be the driver of the main loop
            time += deltaT;
            iteration++;
        }

        parser.finish();

    }

    // Calculates te next collision for all particles
    private void calculateCollisions(Set<GasParticle> particles) {
        for(GasParticle p: particles) {
            this.collisions.addAll(p.calculateParticleNextCollision(this.particles, this.verticalWalls, this.horizontalWalls));
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
        return new Double(particleCounterLeftSide)/particles.size();
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
