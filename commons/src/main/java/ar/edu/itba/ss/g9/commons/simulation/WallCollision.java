package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class WallCollision implements Collision {
    private double time;
    private GasParticle particle;
    private int collisionCounter;

    public WallCollision(GasParticle particle, double time) {
        this.particle = particle;
        this.time = time;
        this.collisionCounter = particle.getCollisionCounter();
    }

    @Override
    public void updateVelocity() {
        //double newDirection = vertical? Math.asin((-velocity)/this.getSpeed()):Math.acos((-velocity)/this.getSpeed());
        // TODO: update direction of this.particle
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
