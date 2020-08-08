package ar.edu.itba.grupo4.tp1.util;

import ar.edu.itba.grupo4.tp1.Particle;

import java.util.LinkedList;
import java.util.List;

public class CellIndexMethod {
    private List<Particle> head;
    private List<Particle> list;

    private static final Particle emptyParticle = new Particle(-1,-1,-1);

    public CellIndexMethod() {
        this.head = new LinkedList<>();
        this.list = new LinkedList<>();
    }

    public void generateLists(Integer N, Integer L, Integer M, List<Particle> particles) {
        Double[] vecCellIndex = new Double[2];
        int scalarCellIndex;

        for(int c = 0 ; c < M ; c++) {
            head.set(c, null);
        }
        for(int i = 0 ; i < N ; i ++) {
            vecCellIndex[0] = particles.get(i).getX()/(L/M);
            vecCellIndex[1] = particles.get(i).getY()/(L/M);

            scalarCellIndex = (int) (vecCellIndex[0] * (L/M) + vecCellIndex[1]);

            list.set(i, head.get(scalarCellIndex));

            head.set(scalarCellIndex, particles.get(i));

        }
    }
}
