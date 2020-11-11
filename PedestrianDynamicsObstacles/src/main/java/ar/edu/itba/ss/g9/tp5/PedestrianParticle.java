package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

import static ar.edu.itba.ss.g9.tp5.CollisionAvoidanceSimulation.UMAX;

/**
 * Particle that represents a Pedestrian
 */
public class PedestrianParticle extends Particle {

    public PedestrianParticle(int id, double xPosition, double yPosition, double xVelocity, double yVelocity, double mass, double radius) {
        super(id, xPosition, yPosition, xVelocity, yVelocity, mass, radius);
    }

    public void move(Point2D finalForce, double dt) {
        double vx = this.getVx() + finalForce.getX() * dt/this.getMass();
        double vy = this.getVy() + finalForce.getY() * dt/this.getMass();
        double speed = Vector.getNorm(new Point2D.Double(vx,vy));
        if(speed > UMAX) {
            this.setVelocity(vx * UMAX/speed, vy * UMAX/speed);
        } else {
            this.setVelocity(vx, vy);
        }
        double x = this.getPosX() + this.getVx() * dt + finalForce.getX() * dt * dt / (2 * this.getMass());
        double y = this.getPosY() + this.getVy() * dt + finalForce.getY() * dt * dt / (2 * this.getMass());
        this.setPosition(x, y);
    }

    @Override
    public String toString() {
        return super.toString() + " 0 1 0";
    }
}
