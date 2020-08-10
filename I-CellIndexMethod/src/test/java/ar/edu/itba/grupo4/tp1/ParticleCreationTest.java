package ar.edu.itba.grupo4.tp1;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ParticleCreationTest {
    @Test
    public void randomNumberBetweenTwoDigitsTest(){
        long numberOfParticles = 100;
        double sideLength = 10;
        int min = 0;
        ArrayList<Particle> particles = new ArrayList<>();
        for(int i = 0; i<numberOfParticles; i++){
            // Every call to ThreadLocalRandom.current() is going to call
            // localInit which will generate a new seed.
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(min, sideLength);
            double y = rand.nextDouble(min, sideLength);
            int radius = 0;
            particles.add(new Particle(x, y, radius, i));
        }

        particles.forEach(particle -> {
                    Assert.assertNotNull(particle);
                    System.out.println(String.format("(x: %.3f, y: %.3f)\n", particle.getX(), particle.getY()));
                });

        Assert.assertEquals(numberOfParticles,particles.size());

    }
}
