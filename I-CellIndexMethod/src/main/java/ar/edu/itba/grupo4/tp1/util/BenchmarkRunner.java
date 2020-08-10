package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BenchmarkRunner {
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
/*
    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void bruteForce(){
        Particle p0 = new Particle(0.4, 0.3, 0, 0,"p0");
        Particle p1 = new Particle(0.6, 0.6, 0, 1,"p1");
        Particle p2 = new Particle(0.1, 0.4, 0, 2, "p2");
        Particle p3 = new Particle(0.7, 0.7, 0, 3, "p3");
        Particle p4 = new Particle(0.3, 0.1, 0, 4, "p4");
        Particle p5 = new Particle(0.8, 0.1, 0, 5, "p5");
        Particle p6 = new Particle(0.8, 0.3, 0, 6, "p6");
        Particle p7 = new Particle(0.9, 0.9, 0, 7, "p7");
        BruteForce bf = new BruteForce(Stream.of(p0, p1, p2, p3, p4, p5, p6, p7).collect(Collectors.toList()), 0.4);
    }

 */

    @Benchmark
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void cellIndexMethod(){
        Particle p0 = new Particle(0.4, 0.3, 0, 0,"p0");
        Particle p1 = new Particle(0.6, 0.6, 0, 1,"p1");
        Particle p2 = new Particle(0.1, 0.4, 0, 2, "p2");
        Particle p3 = new Particle(0.7, 0.7, 0, 3, "p3");
        Particle p4 = new Particle(0.3, 0.1, 0, 4, "p4");
        Particle p5 = new Particle(0.8, 0.1, 0, 5, "p5");
        Particle p6 = new Particle(0.8, 0.3, 0, 6, "p6");
        Particle p7 = new Particle(0.9, 0.9, 0, 7, "p7");
        CellIndexMethod cim = new CellIndexMethod(8, (double) 1, 2, Stream.of(p0, p1, p2, p3, p4, p5, p6, p7).collect(Collectors.toList()), false, 0.4);
    }
}
