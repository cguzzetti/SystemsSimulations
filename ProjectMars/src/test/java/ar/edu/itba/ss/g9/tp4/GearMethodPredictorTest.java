package ar.edu.itba.ss.g9.tp4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.awt.geom.Point2D;

/**
 * Unit tests for GearMethodPredictor
 */
public class GearMethodPredictorTest {
    @Test
    public void factorialWithValidNumberWorksTest() {
        int realValue = GearMethodPredictor.factorial(3);
        int expectedValue = 6;

        assertEquals(expectedValue, realValue);
    }

    @Test
    public void factorialWithZeroWorksTest(){
        int realValue = GearMethodPredictor.factorial(0);
        int expectedValue = 1;

        assertEquals(expectedValue, realValue);

    }

    @Test(expected = IllegalArgumentException.class)
    public void factorialOfNegativeValueThrowsExceptionTest(){
        GearMethodPredictor.factorial(-1);
    }

    @Test
    public void gearMethodTest(){
        Force force = new OscillatorForce(25.0, 9.95);
        GearMethodPredictor pr = new GearMethodPredictor(2, force);
        AcceleratedParticle particle = new AcceleratedParticle(
                1,
                new Point2D.Double(0,0),
                new Point2D.Double(1, 1),
                0.5,
                1
        );


        pr.moveParticle(particle, 1);
    }
}
