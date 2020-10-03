package ar.edu.itba.grupo9.tp4;

public interface Force {

    double getForceX(double time, AcceleratedParticle particle);
    double getForceY(double time, AcceleratedParticle particle);

}
