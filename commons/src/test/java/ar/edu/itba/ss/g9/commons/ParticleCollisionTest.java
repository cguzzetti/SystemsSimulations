package ar.edu.itba.ss.g9.commons;

import ar.edu.itba.ss.g9.commons.simulation.Particle;
import ar.edu.itba.ss.g9.commons.simulation.ParticleCollision;
import javafx.geometry.Point2D;
import org.junit.Test;

public class ParticleCollisionTest {

    @Test
    public void someTest(){
        Particle p1 = new Particle(
                0, 0, 0, 1, 1
        );

        Particle p2 = new Particle(
                10, 0, Math.PI, 2, 1
        );

        ParticleCollision collision = new ParticleCollision(p1, p2, 500.15);

        collision.updateVelocity();



    }
}
