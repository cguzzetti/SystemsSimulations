package ar.edu.itba.ss.g9.tp5;

import java.awt.geom.Point2D;

import static ar.edu.itba.ss.g9.tp5.Configuration.UMAX;

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
        double speed = Vector.getNorm(new Point2D.Double(vx, vy));
        if(speed > UMAX){
            this.setVelocity(vx * UMAX/speed, vy * UMAX/speed);
        } else {
            this.setVelocity(vx, vy);
        }

        double x = this.getPosX() + this.getVx() * dt;
        double y = this.getPosY() + this.getVy() * dt;
        this.setPosition(x, y);
    }

    @Override
    public String toString() {
        return super.toString() + " 0 1 0";
    }
}
