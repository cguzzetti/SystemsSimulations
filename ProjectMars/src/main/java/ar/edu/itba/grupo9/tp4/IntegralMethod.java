package ar.edu.itba.grupo9.tp4;

public interface IntegralMethod {
    AcceleratedParticle moveParticle(AcceleratedParticle particle,double deltaT);
    IntegralMethods getMethod();
}
