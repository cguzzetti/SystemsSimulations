package ar.edu.itba.ss.g9.tp4;

public interface Force {
    double getForceX(double time, AcceleratedParticle particle);
    double getForceY(double time, AcceleratedParticle particle);
}
