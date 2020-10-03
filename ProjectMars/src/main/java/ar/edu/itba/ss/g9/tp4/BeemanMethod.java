package ar.edu.itba.ss.g9.tp4;

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

        AcceleratedParticle prevP = new AcceleratedParticle(particle.getId(), particle.getPrevPosition(),particle.getRadius(), particle.getMass());

        nextParticle.setPositionX(particle.getPosition().getX() + particle.getVelocity().getX() * deltaT
                + (2.0/3) * (force.getForceX(time, particle)/nextParticle.getMass()) * deltaT * deltaT
                - (1.0/6) * (force.getForceX(time - deltaT, prevP)/nextParticle.getMass()) * deltaT * deltaT);
        nextParticle.setPositionY(particle.getPosition().getY() + particle.getVelocity().getY() * deltaT
                + (2.0/3) * (force.getForceY(time, particle)/nextParticle.getMass()) * deltaT * deltaT
                - (1.0/6) * (force.getForceY(time - deltaT, prevP)/nextParticle.getMass()) * deltaT * deltaT);
    }

    public void updateVelocity(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {

        AcceleratedParticle prevP = new AcceleratedParticle(particle.getId(), particle.getPrevPosition(),particle.getRadius(), particle.getMass());

        nextParticle.setVelocityX(particle.getVelocity().getX()
                + (1.0/3) * (force.getForceX(time, nextParticle)/nextParticle.getMass()) * deltaT
                + (5.0/6) * (force.getForceX(time, particle)/nextParticle.getMass()) * deltaT
                - (1.0/6) * (force.getForceX(time - deltaT, prevP)/nextParticle.getMass()) * deltaT);
        nextParticle.setVelocityY(particle.getVelocity().getY()
                + (1.0/3) * (force.getForceY(time, nextParticle)/nextParticle.getMass()) * deltaT
                + (5.0/6) * (force.getForceY(time, particle)/nextParticle.getMass()) * deltaT
                - (1.0/6) * (force.getForceY(time - deltaT, prevP)/nextParticle.getMass()) * deltaT);
    }


}
