package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CellIndexMethod {
    private List<Particle> head;
    private List<Particle> list;

    private static final Particle emptyParticle = new Particle(-1,-1,-1,-1, "empty");

    public CellIndexMethod() {
        this.head = new LinkedList<>();
        this.list = new LinkedList<>();
    }

    public void generateLists(Integer N, Double L, Integer M, List<Particle> particles) {
        double[] vecCellIndex = new double[2];
        int scalarCellIndex;

        for(int c = 0 ; c < M*M ; c++) {
            head.add(c, emptyParticle);
        }

        System.out.println(particles.stream().map(Particle::getName).collect(Collectors.toList()));

        for(int i = 0 ; i < N ; i ++) {

            vecCellIndex[0] = particles.get(i).getX()/(L/M);
            vecCellIndex[1] = particles.get(i).getY()/(L/M);
            System.out.println(i +" : "+vecCellIndex[0] + "," + vecCellIndex[1]);

            scalarCellIndex = (int) (vecCellIndex[0]) + (int) (vecCellIndex[1]) * M;
            System.out.println(scalarCellIndex);

            list.add(i, head.get(scalarCellIndex));

            head.set(scalarCellIndex, particles.get(i));

        }

        System.out.println(head.stream().map(Particle::getName).collect(Collectors.toList()));
        System.out.println(list.stream().map(Particle::getName).collect(Collectors.toList()));

        for (Particle p: head) {
            String s = "";
            while( p.getId() != -1){
              s += p.getId();
              p = list.get(p.getId());
            }
            System.out.println(s);
        }
    }
}
