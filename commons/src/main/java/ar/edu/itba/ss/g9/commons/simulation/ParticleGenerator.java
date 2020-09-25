package ar.edu.itba.ss.g9.commons.simulation;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ParticleGenerator {
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

    public static Set<GasParticle> generateGasParticles(int numberOfParticles, double height, double width, double particleRadius, double particleMass, double particleSpeed){
        Set<GasParticle> particles = new HashSet<>(numberOfParticles);
        int createdParticles = 0;
        while( createdParticles < numberOfParticles){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0 + particleRadius, width/2 - particleRadius); // We want to initialize on the left side of the box
            double y = rand.nextDouble(0 + particleRadius, height - particleRadius);
            double direction = rand.nextDouble(-Math.PI, Math.PI);
            GasParticle particle = new GasParticle(x, y, direction, createdParticles, particleRadius, particleMass, particleSpeed);
            if(particle.isValid(particles)) {
                particles.add(particle);
                createdParticles++;
            }
        }
        return particles;
    }
}
