package ar.edu.itba.ss.g9.tp4.Mars;

import ar.edu.itba.ss.g9.tp4.AcceleratedParticle;

import java.util.List;

public class Gravity implements Force {
    static final double G = 6.693E-11;

    public double getForce(AcceleratedParticle particle, AcceleratedParticle other) {
        return G * particle.getMass() * other.getMass() / (Math.pow(getDistance(particle, other),2));
    }

    public double getDistance(AcceleratedParticle particle, AcceleratedParticle other) {
        return particle.getPosition().distance(other.getPosition());
    }

    public double getForceX(List<AcceleratedParticle> otherParticles, AcceleratedParticle particle) {
        double force = 0;
        for(AcceleratedParticle p: otherParticles) {
            force +=  getForce(particle, p) * (p.getPositionX() - particle.getPositionX()) / getDistance(particle, p);
        }
        return force;
    }

    public double getForceY(List<AcceleratedParticle> otherParticles, AcceleratedParticle particle) {
        double force = 0;
        for(AcceleratedParticle p: otherParticles) {
            force +=  getForce(particle, p) * (p.getPositionY() - particle.getPositionY()) / getDistance(particle, p);
        }
        return force;
    }
}
