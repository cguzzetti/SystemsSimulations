package ar.edu.itba.ss.g9.commons.simulation;

import java.awt.geom.Point2D;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
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

    public static Set<Particle> generateWeightedParticles(int numberOfParticles, double height, double width){
        Set<Particle> particles = new HashSet<>(numberOfParticles);

        int createdParticles = 0;
        while( createdParticles < numberOfParticles){
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0, width/2); // We want to initialize on the left side of the box
            double y = rand.nextDouble(0, height);
            double direction = rand.nextDouble(-Math.PI, Math.PI);
            double mass = 1;
            Particle particle = new Particle(x, y, direction, createdParticles, mass);
            if(particle.isValid(particles))
                particles.add(particle);
                createdParticles++;
        }

        return particles;
    }
}
