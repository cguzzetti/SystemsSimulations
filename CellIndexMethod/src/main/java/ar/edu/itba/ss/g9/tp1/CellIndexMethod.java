package ar.edu.itba.ss.g9.tp1;

import ar.edu.itba.ss.g9.commons.simulation.Particle;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CellIndexMethod {
    // head[c] holds the the first particle in the c-th cell, or head[c] = emptyParticle (id = −1) if there is no particles in the cell
    private List<Particle> head;
    // list[i] holds the particle to which the i-th atom (atom with id = i) points
    private List<Particle> list;
    // Number of particles
    private Integer N;
    // Length of area side
    private Double L;
    // Amount of cells in one side
    private Integer M;
    // Interaction radius
    private double rc;

    private List<Particle> particles;

    // If the case is the are border represents a limit or not
    private boolean periodicCondition;

    private static final Particle emptyParticle = new Particle(-1,-1,-1,-1, "empty");

    public CellIndexMethod(Integer N, Double L, Integer M, List<Particle> particles, boolean periodicCondition, double rc, double maxRadius1, double maxRadius2)  throws IllegalArgumentException{
        if(particles.isEmpty())
            return;
        if(particles.size() != N)
            throw new IllegalArgumentException("N is different from the amount of particles");

        // Validating criteria for interaction radius
        if(L/M - maxRadius1 - maxRadius2 <= rc)
            throw new IllegalArgumentException("rc must be smaller than L/M - maxRadius1 - maxRadius2");

        this.head = new LinkedList<>();
        this.list = new LinkedList<>();

        this.N = N;
        this.L = L;
        this.M = M;
        this.rc = rc;
        this.particles = particles;
        this.periodicCondition = periodicCondition;

        this.generateLists();
        this.calculateNeighbors();
    }

    public void CellIndexMethodRun(List<Particle> particles) {
        if(particles.isEmpty())
            return;
        if(particles.size() != this.N)
            throw new IllegalArgumentException("N is different from the amount of particles");

        this.particles = particles;
        this.generateLists();
        this.calculateNeighbors();
    }

    // Defines the cells and both list and head lists sorting the particles in their corresponding cells
    public void generateLists() {
        double[] vecCellIndex = new double[2];
        int scalarCellIndex;

        // Reset head for all cells with empty particles
        for(int c = 0 ; c < M*M ; c++) {
            head.add(c, emptyParticle);
        }

        // Scan particles to build head and list
        for(int i = 0 ; i < N ; i ++) {
            Particle p = particles.get(i);
            // Vector cell index to which this particle belongs
            vecCellIndex[0] = p.getX()/(L/M);
            vecCellIndex[1] = p.getY()/(L/M);

            // Translate the vector cell index to a scalar cell index
            scalarCellIndex = (int) (vecCellIndex[0]) + ((int) (vecCellIndex[1])) * M;

            if(scalarCellIndex < 0 || scalarCellIndex >= M*M)
                System.out.println("Particle with error: "+p);

            // Link to the previous occupant (or EMPTY if you're the 1st)
            list.add(i, head.get(scalarCellIndex));

            // The last one goes to the header
            head.set(scalarCellIndex, p);
        }
    }

    // For every particle it checks its neighbor cells and looks for potential neighbor particles to match
    public void calculateNeighbors() {
        int c;
        int cNeighbor;
        Set<Integer> neighborCells = new HashSet<>();
        Point2D.Double rshift = new Point2D.Double();

        // Scan all cells
        for(int i = 0 ; i < M ; i++) {
            for(int j = 0 ; j < M ; j++) {
                // Calculate scalarCellIndex
                c = (int) (i) + (int) (j) * M;

                if(this.head.get(c).getId() != -1) {
                    // Scan the neighbor cells including itself
                    // we only check the upper 'L' neighbor cells
                    // to avoid checking twice
                    for (int x = i; x < i + 2; x++) {
                        for (int y = j - 1; y < j + 2; y++) {
                            if (!(x == i && y == j - 1) && ((y >= 0 && y < M) || periodicCondition)) {
                                // Periodic boundary condition by shifting coordinates
                                if(periodicCondition)
                                    shiftRegion(x, y, M, rshift);

                                // Calculate scalarCellIndex for neighbor cell
                                cNeighbor = (x + M) % M + ((y + M) % M) * M;

                                // Evaluate particles from neighbor cell
                                checkNeighborCell(neighborCells, c, cNeighbor, rshift);
                            }
                        }
                    }
                }
                neighborCells.clear();
            }
        }
    }

    private void calculateDistanceAndAddNeighbor(Particle p, Particle pNeighbor, Point2D.Double rshift) {
        double distance;
        if (p.getId() != pNeighbor.getId()) {
            distance = p.getPoint().distance(new Point2D.Double(pNeighbor.getPoint().getX()+rshift.getX(),pNeighbor.getPoint().getY()+rshift.getY())) - p.getRadius() - pNeighbor.getRadius();
            if (Double.compare(distance, rc) <= 0) {
                p.addNeighbor(pNeighbor);
            }
        }
    }

    private void checkNeighborCell(Set<Integer> neighborCells, int c, int cNeighbor, Point2D.Double rshift) {
        Particle p;
        Particle pNeighbor;
        if(!neighborCells.contains(cNeighbor)) {
            neighborCells.add(cNeighbor);
            // Scan particles in current cell
            p = this.head.get(c);
            while (p.getId() != -1) {
                // Scan particles in neighbor cells
                pNeighbor = this.head.get(cNeighbor);
                while (pNeighbor.getId() != -1) {
                    // Calculate distance and add neighbor correspondingly
                    calculateDistanceAndAddNeighbor(p, pNeighbor, rshift);
                    pNeighbor = list.get(pNeighbor.getId());
                }
                p = list.get(p.getId());
            }
        }
    }

    private void shiftRegion(int x, int y, Integer M, Point2D.Double rshift) {
        if(x >= M)
            rshift.x = L;
        else
            rshift.x = 0.0;

        if(y >= M)
            rshift.y = L;
        else if(y < 0)
            rshift.y = -L;
        else
            rshift.y = 0.0;
    }
}