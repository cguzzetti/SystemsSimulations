package ar.edu.itba.ss.g9.tp4;

public class OscillatorForce implements Force {
    private double k;
    private double g;

    public OscillatorForce(double k, double g){
        this.k = k;
        this.g = g;
    }

    @Override
    public double getForceX(double time, AcceleratedParticle particle) {
        return -k * particle.getPosition().getX() - g*particle.getVelocity().getX();
    }

    @Override
    public double getForceY(double time, AcceleratedParticle particle) {
        return -k * particle.getPosition().getY() - g*particle.getVelocity().getY();
    }


}
