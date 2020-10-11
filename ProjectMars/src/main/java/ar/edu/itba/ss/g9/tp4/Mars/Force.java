package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.AcceleratedParticle;

import java.util.List;

public interface Force {
    double getForceX(List<AcceleratedParticle> otherParticles, AcceleratedParticle particle);
    double getForceY(List<AcceleratedParticle> otherParticles, AcceleratedParticle particle);
}
