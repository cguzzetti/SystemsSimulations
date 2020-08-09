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

    public void calculateNeighbors(Integer N, Double L, Integer M, List<Particle> particles, boolean periodicCondition, double r) {
        int c;
        int cNeighbor;
        Particle p;
        Particle pNeighbor;
        double distance;

        // scan all cells
        for(int i = 0 ; i < M ; i++) {
            for(int j = 0 ; j < M ; j++) {
                // calculate scalarCellIndex
                c = (int) (i) + (int) (j) * M;
                // scan the neighbor cells including itself
                // we only check the upper 'L' neighbor cells
                // to avoid checking twice
                for(int x = i ; x < i+1 ; x++) {
                    // check if x is out of the  area boundary and
                    // if the case is we have to consider periodic
                    // condition
                    if( x < M || periodicCondition) {
                        if(x >= M)
                            x = 0;
                        // same for y
                        for(int y = j-1 ; y < j+1 ; y++) {
                            if(y >= 0 || periodicCondition) {
                                if(y < 0)
                                    y = M-1;
                                // calculate scalarCellIndex for neighbor cell
                                cNeighbor = (x+M)%M + ((y+M)%M)*M;
                                // scan particles in current cell
                                p = this.head.get(c);
                                while(p.getId() != -1) {
                                    // scan particles in neighbor cells
                                    pNeighbor = this.head.get(cNeighbor);
                                    while(pNeighbor.getId() != -1) {
                                        // calculate distance
                                        distance = p.getPoint().distance(pNeighbor.getPoint());
                                        if(Double.compare(distance, r) <= 0) {
                                            p.addNeighbor(pNeighbor);
                                        }
                                        pNeighbor = list.get(pNeighbor.getId());
                                    }
                                    p = list.get(p.getId());
                                }
                            }
                        }
                    }
                }
            }
        }
        for(Particle part: particles) {
            System.out.println(part.getName() + ": " + part.getNeighbors());
        }
    }
}
