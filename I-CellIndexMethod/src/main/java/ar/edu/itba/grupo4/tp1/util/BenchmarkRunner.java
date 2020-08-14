package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;
import org.openjdk.jmh.annotations.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void bruteForce(){
        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i< 100; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, 10),
                    rnd.nextDouble(0, 10),
                    0.0,
                    i,
                    String.format("p%d", i)
            ));
        }
        BruteForce bf = new BruteForce(particles, 0.9);
    }


/*
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void cellIndexMethod(){
        int N = 100;
        long L = 10;
        boolean periodic = false;
        double rc = 0.9;
        Integer M=11;
        ArrayList<Particle> particles = new ArrayList<>();

        for(int i =0; i< 100; i++){
            ThreadLocalRandom rnd = ThreadLocalRandom.current();
            particles.add(new Particle(
                    rnd.nextDouble(0, L),
                    rnd.nextDouble(0, L),
                    0.0,
                    i,
                    String.format("p%d", i)
            ));
        }
        CellIndexMethod cim = new CellIndexMethod(N, (double) L, M, particles, periodic, rc);
    }
*/

}
