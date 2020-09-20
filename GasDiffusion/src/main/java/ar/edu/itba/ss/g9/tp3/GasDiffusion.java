package ar.edu.itba.ss.g9.tp3;

import ar.edu.itba.ss.g9.commons.simulation.Collision;
import ar.edu.itba.ss.g9.commons.simulation.Particle;


import javafx.geometry.Point2D;
import java.util.*;

public class GasDiffusion {
    private final int N;
    private final double height;
    private final double width;
    private final double partitionLen;
    private Queue<Collision> collisions;
    private Set<Particle> particles;
    private Point2D[][] verticalWalls;
    private Point2D[][] horizontalWalls;

    public GasDiffusion(GasDifussionConfig config, Set<Particle> particles){
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
                {new Point2D(config.getWidth() / 2, config.getHeight() / 2 + config.getPartitionLen() / 2),
                        new Point2D(config.getWidth() / 2, config.getHeight())},
                {new Point2D(config.getWidth(), 0), new Point2D(config.getWidth(), config.getHeight())}
        };
    }

    public void simulate(GasDiffusionFileParser parser){
        double time = 0;
        calculateCollisions(this.particles);
        if(collisions.isEmpty()) return;

        // TODO: EndCondition won't be time but fp
        while(time < 1000){
            // Get first valid collision
            if(collisions.isEmpty()) return;
            Optional<Collision> maybeCollision = getCollisionIfValid(collisions.poll());
            if(maybeCollision.isEmpty()) break;
            Collision collision = maybeCollision.get();

            // advanceParticles(collision.time())
            // parser.writeStateToOutput(particles, dt);

            Set<Particle> particlesInCollision = collision.getParticles();

            // For particles involved in current collision:
            collision.updateVelocity(); // Update velocity
            calculateCollisions(particlesInCollision); // Determine future collisions
            particlesInCollision.forEach(Particle::increaseCollisionCounter); // Increase collision counter

            // TODO: better handle of time evolution. Not sure how the time in collisions differs from the main timeline.
            time+=collision.getTime();
        }

        parser.finish();

    }

    // Calculates te next collision for all particles
    private void calculateCollisions(Set<Particle> particles) {
        for(Particle p: particles) {
            this.collisions.addAll(p.calculateParticleNextCollision(this.particles, this.verticalWalls, this.horizontalWalls));
        }
    }

    private Optional<Collision> getCollisionIfValid(Collision collision){
        if(collision.isValid())
            return Optional.of(collision);

        return Optional.empty();
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
