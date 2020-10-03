package ar.edu.itba.ss.g9.tp4;

public interface IntegralMethod {
    AcceleratedParticle moveParticle(AcceleratedParticle particle,double deltaT);
    IntegralMethods getMethod();
}
