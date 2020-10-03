package ar.edu.itba.ss.g9.tp4;

public class VerletMethod implements IntegralMethod{
    private Force force;
    private double deltaT;
    private IntegralMethods method;

    public VerletMethod(Force force, double deltaT) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = IntegralMethods.VERLET_ORIGINAL;
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
        nextParticle.setPositionX(2.0 * particle.getPosition().getX()
                - particle.getPrevPosition().getX()
                + deltaT * deltaT * (force.getForceX(time, particle)/nextParticle.getMass()));
        nextParticle.setPositionY(2.0 * particle.getPosition().getY()
                - particle.getPrevPosition().getY()
                + deltaT * deltaT * (force.getForceY(time, particle)/nextParticle.getMass()));
    }

    public void updateVelocity(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {
        nextParticle.setVelocityX((nextParticle.getPosition().getX() - particle.getPrevPosition().getX())/(2 * deltaT));
        nextParticle.setVelocityY((nextParticle.getPosition().getY() - particle.getPrevPosition().getY())/(2 * deltaT));
    }

}
