package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

public class ParticleCollision extends Collision {


    private final Particle particle1;
    private final Particle particle2;
    private final double time;

    public ParticleCollision(Particle p1, Particle p2, double time){
        this.particle1 = p1;
        this.particle2 = p2;
        this.time = time;
    }
    @Override
    public void updateVelocity() {
        final Point2D updatedPositionP1 = new Point2D(
                this.particle1.getX() + this.particle1.getVx() * time,
                this.particle1.getY() + this.particle1.getVy() * time
        );

        final Point2D updatedPositionP2 = new Point2D(
                this.particle2.getX() + this.particle2.getVx() * time,
                this.particle2.getY() + this.particle2.getVy() * time
        );

        double sigma = this.particle1.getRadius() + this.particle2.getRadius();
        double m1 = this.particle1.getMass();
        double m2 = this.particle2.getMass();
        final Point2D deltaV = new Point2D(
                this.particle2.getVx() - this.particle1.getVx(),
                this.particle2.getVy() - this.particle2.getVy()
        );
        final Point2D deltaR = updatedPositionP2.subtract(updatedPositionP1);
        // double J = 2*m1*m2*(deltaV.dotProduct()


    }

    public Particle getParticle1() {
        return particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }
}
