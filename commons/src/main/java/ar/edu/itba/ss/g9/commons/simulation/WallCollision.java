package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WallCollision implements Collision {
    private double time;
    private GasParticle particle;
    private int collisionCounter;
    private final boolean isVertical;

    public WallCollision(GasParticle particle, double time, boolean isVertical) {
        this.particle = particle;
        this.time = time;
        this.collisionCounter = particle.getCollisionCounter();
        this.isVertical = isVertical;
    }

    @Override
    public void updateVelocity() {
        Point2D newVelocity;
        if(isVertical)
            newVelocity = new Point2D(particle.getVx(), -particle.getVy());
        else
            newVelocity = new Point2D(-particle.getVx(), particle.getVy());

        this.particle.setVelocity(newVelocity);
    }

    @Override
    public Set<GasParticle> getParticles() {
        return new HashSet<GasParticle>(Collections.singleton(this.particle));
    }

    @Override
    public boolean isValid() {
        return this.collisionCounter == this.particle.getCollisionCounter();
    }

    @Override
    public double getTime() {
        return this.time;
    }
}
