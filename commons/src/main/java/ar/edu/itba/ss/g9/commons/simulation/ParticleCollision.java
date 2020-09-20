package ar.edu.itba.ss.g9.commons.simulation;

import javafx.geometry.Point2D;

import java.util.Set;

public class ParticleCollision implements Collision {

    private final Particle particle1;
    private final Particle particle2;
    private int collisionCounterP1;
    private int collisionCounterP2;
    private final double time;

    public ParticleCollision(Particle p1, Particle p2, double time){
        this.particle1 = p1;
        this.particle2 = p2;
        this.time = time;
        this.collisionCounterP1 = p1.getCollisionCounter();
        this.collisionCounterP2 = p2.getCollisionCounter();
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
        double J = 2*m1*m2*(deltaV.dotProduct(deltaR))/(sigma*(m1 + m2));

        double jx = J  * deltaR.getX() / sigma;
        double jy = J * deltaR.getY() / sigma;

        double newVx1 = this.particle1.getVx() + jx/m1;
        double dir1 = Math.acos(newVx1/particle1.getSpeed());
        //assert dir1 - (-Math.asin((this.particle1.getVy() + jy/m1)/particle1.getSpeed())) < 0.0001;

        System.out.printf("DIR1: %f\n", dir1);
        System.out.printf("OTHER DIR1: %f\n", Math.asin((this.particle1.getVy() + jy/m1)/particle1.getSpeed()));
        double newVx2 = this.particle2.getVx() + jx/m2;
        double dir2 = Math.acos(newVx2/particle2.getSpeed());

        System.out.printf("DIR2: %f\n",dir2);
        System.out.printf("OtherDIR2: %f\n",Math.asin((this.particle2.getVy() + jy/m2)/particle2.getSpeed()));
        // assert dir2 - (Math.asin((this.particle2.getVy() + jy/m2)/particle2.getSpeed())) < 0.0001;

        Particle newP1 = new Particle(
                updatedPositionP1.getX(), updatedPositionP1.getY(),
                dir1, particle1.getId(), particle1.getMass()
        );
        Particle newP2 = new Particle(
                updatedPositionP1.getX(), updatedPositionP1.getY(),
                dir2, particle2.getId(), particle2.getMass()
        );

        System.out.printf("P1: %s\n", newP1.toString());
        System.out.printf("P2: %s\n", newP2.toString());
    }

    public Particle getParticle1() {
        return particle1;
    }

    public Particle getParticle2() {
        return particle2;
    }

    @Override
    public boolean isValid() {
        return this.collisionCounterP1 == this.particle1.getCollisionCounter() && this.collisionCounterP2 == this.particle2.getCollisionCounter();
    }

    @Override
    public Set<Particle> getParticles() {
        return null;
    }

    @Override
    public double getTime() {
        return this.time;
    }
}
