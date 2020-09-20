package ar.edu.itba.ss.g9.commons.simulation;

import java.util.Set;

public interface Collision {

    boolean isValid();

    void updateVelocity();

    Set<Particle> getParticles();

    double getTime();
}
