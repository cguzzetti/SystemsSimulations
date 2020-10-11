package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.AcceleratedParticle;
import ar.edu.itba.ss.g9.tp4.IntegralMethods;

import java.util.List;

public interface IntegralMethod {
    AcceleratedParticle moveParticle(AcceleratedParticle particle, List<AcceleratedParticle> otherParticles);
    IntegralMethods getMethod();
}
