package ar.edu.itba.ss.g9.commons.simulation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ParticleGeneration {
    public static StringBuilder generateRandomParticles(long numberOfParticles, double sideLength){
        return Stream.iterate(0, n -> n+1).limit(numberOfParticles).parallel().collect(StringBuilder::new, (sb, i)-> {
            // Every call to ThreadLocalRandom.current() is going to call
            // localInit which will generate a new seed.
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0, sideLength);
            double y = rand.nextDouble(0, sideLength);
            // double radius = rand.nextDouble(0, 1);
            double radius = 0.25;
            sb.append(String.format("%.2f %.2f %.2f\n", x, y, radius));
        }, StringBuilder::append);
    }

    public static Set<GasParticle> generateGasParticles(int numberOfParticles, double height, double width){
        Set<GasParticle> particles = new HashSet<>(numberOfParticles);
        double radius = 0.0015;
        int createdParticles = 0;
        while( createdParticles < numberOfParticles){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0 + radius, width/2 - radius); // We want to initialize on the left side of the box
            double y = rand.nextDouble(0 + radius, height - radius);
            double direction = rand.nextDouble(-Math.PI, Math.PI);
            double mass = 1;
            GasParticle particle = new GasParticle(x, y, direction, createdParticles, mass);
            if(particle.isValid(particles))
                particles.add(particle);
                createdParticles++;
        }

        return particles;
    }
}
