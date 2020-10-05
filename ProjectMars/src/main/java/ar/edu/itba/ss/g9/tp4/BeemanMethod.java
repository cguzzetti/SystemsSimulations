package ar.edu.itba.ss.g9.tp4;

import java.awt.geom.Point2D;

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

        AcceleratedParticle prevP = new AcceleratedParticle(particle.getId(), particle.getPrevPosition(), particle.getPrevVelocity() ,particle.getRadius(), particle.getMass());

        double posX = particle.getPosition().getX() + particle.getVelocity().getX() * deltaT
                + (2.0/3) * (force.getForceX(time, particle)/nextParticle.getMass()) * deltaT * deltaT
                - (1.0/6) * (force.getForceX(time - deltaT, prevP)/nextParticle.getMass()) * deltaT * deltaT;
        double posY = particle.getPosition().getY() + particle.getVelocity().getY() * deltaT
                + (2.0/3) * (force.getForceY(time, particle)/nextParticle.getMass()) * deltaT * deltaT
                - (1.0/6) * (force.getForceY(time - deltaT, prevP)/nextParticle.getMass()) * deltaT * deltaT;

        nextParticle.setPosition(new Point2D.Double(posX, posY));
    }

    public void updateVelocity(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {

        AcceleratedParticle prevP = new AcceleratedParticle(particle.getId(), particle.getPrevPosition(), particle.getPrevVelocity(), particle.getRadius(), particle.getMass());

        AcceleratedParticle auxNextParticle = new AcceleratedParticle(nextParticle);
        auxNextParticle.setVelocity(new Point2D.Double(particle.getVelocityX(),particle.getVelocityY())); // Since we don't know the value yet.

        double vx = particle.getVelocity().getX()
                + (1.0/3) * (force.getForceX(time, auxNextParticle)/auxNextParticle.getMass()) * deltaT
                + (5.0/6) * (force.getForceX(time, particle)/auxNextParticle.getMass()) * deltaT
                - (1.0/6) * (force.getForceX(time - deltaT, prevP)/auxNextParticle.getMass()) * deltaT;
        double vy = particle.getVelocity().getY()
                + (1.0/3) * (force.getForceY(time, auxNextParticle)/auxNextParticle.getMass()) * deltaT
                + (5.0/6) * (force.getForceY(time, particle)/auxNextParticle.getMass()) * deltaT
                - (1.0/6) * (force.getForceY(time - deltaT, prevP)/auxNextParticle.getMass()) * deltaT;

        nextParticle.setVelocity(new Point2D.Double(vx, vy));
    }


}
