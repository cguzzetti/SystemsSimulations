package ar.edu.itba.ss.g9.commons;

import ar.edu.itba.ss.g9.commons.simulation.GasParticle;
import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.commons.simulation.ParticleCollision;
import javafx.geometry.Point2D;
import org.junit.Assert;
import org.junit.Test;

public class ParticleCollisionTest {

    @Test
    public void collisionUpdatesVelocityCorrectlyTest(){
        GasParticle p1 = new GasParticle(
                0, 0, 0, 1, 1
        );

        GasParticle p2 = new GasParticle(
                10, 0, Math.PI, 2, 1
        );

        ParticleCollision collision = new ParticleCollision(p1, p2, 500.15);

        collision.updateVelocity();

        Assert.assertEquals(p1.getVx(), -0.01, 0.00001);
        Assert.assertEquals(p2.getVx(), 0.01, 0.00001);
        Assert.assertEquals(p1.getVy(), 0, 0.00001);
        Assert.assertEquals(p2.getVy(), 0, 0.00001);


    }
}
