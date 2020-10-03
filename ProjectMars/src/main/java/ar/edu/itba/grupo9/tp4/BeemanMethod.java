package ar.edu.itba.grupo9.tp4;

public class BeemanMethod implements IntegralMethod{
    private Force force;
    private double deltaT;
    private IntegralMethods method;

    public BeemanMethod(Force force, double deltaT) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = IntegralMethods.BEEMAN;
    }

    @Override
    public IntegralMethods getMethod() {
        return method;
    }

    @Override
    public AcceleratedParticle moveParticle(AcceleratedParticle particle, double time) {
        AcceleratedParticle nextParticle = new AcceleratedParticle(particle.getId(), particle.getRadius(), particle.getMass());
        nextParticle.setPrevPosition(particle.getPosition());
        nextParticle.setPrevVelocity(particle.getVelocity());

        updatePosition(nextParticle, particle, time);
        updateVelocity(nextParticle, particle, time);
        return nextParticle;
    }

    public void updatePosition(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {

    }

    public void updateVelocity(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {

    }


}
