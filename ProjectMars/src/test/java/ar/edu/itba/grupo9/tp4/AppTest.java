package ar.edu.itba.grupo9.tp4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void weirdGravityWorks() {
        AcceleratedParticle p = new AcceleratedParticle(
                1,
                0,
                0,
                1,
                1,
                1,
                0.5
        );

        Gravity g = new Gravity();
        double newAcceleration = g.apply(p);
        assertEquals( 0.5, newAcceleration, 0.001);
    }
}
