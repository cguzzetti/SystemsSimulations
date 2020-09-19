package ar.edu.itba.ss.g9.commons.simulation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
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

    public static Collection<Particle> generateWeightedParticles(int numberOfParticles, double height, double width){
        Collection<Particle> particles = new ArrayList<>(numberOfParticles);

        Stream.iterate(0, n-> n+1).limit(numberOfParticles).parallel().forEach((i -> {
            ThreadLocalRandom rand = ThreadLocalRandom.current();
            double x = rand.nextDouble(0, width/2); // We want to initialize on the left side of the box
            double y = rand.nextDouble(0, height);
            double direction = rand.nextDouble(-Math.PI, Math.PI);
            double mass = 1;
            particles.add(new Particle(x, y, direction, i, mass));

        }));

        return particles;
    }
}
