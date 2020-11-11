package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

/**
 * Particle that represents a Pedestrian
 */
public class PedestrianParticle extends Particle {

    public PedestrianParticle(int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, double radius) {
        super(id, xPosition, yPosition, xVelocity, yVelocity, mass, radius);
    }

    public void move(Point2D finalForce, double dt) {
        double vx = this.getVx() + finalForce.getX() * dt;
        double vy = this.getVy() + finalForce.getY() * dt;
        this.setVelocity(vx, vy);

        double x = this.getPosX() + vx*dt;
        double y = this.getPosY() + vy*dt;
        this.setPosition(x, y);
    }

    @Override
    public String toString() {
        return super.toString() + " 0 1 0";
    }
}
