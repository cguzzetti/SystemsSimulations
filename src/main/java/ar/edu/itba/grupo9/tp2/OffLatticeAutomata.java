package ar.edu.itba.grupo9.tp2;

import ar.edu.itba.grupo9.tp1.Particle;
import ar.edu.itba.grupo9.tp1.util.CellIndexMethod;

import java.util.List;

public class OffLatticeAutomata {

    private List<Particle> particles;

    public OffLatticeAutomata(Integer timeLapse, Integer N, Double L, Integer M, List<Particle> particles, double rc, double maxRadius1, double maxRadius2) {
        this.particles = particles;
        
        CellIndexMethod cim = new CellIndexMethod(N, L, M, this.particles, true, rc, maxRadius1, maxRadius2);

        for(int i = 0; i < timeLapse ; i++) {
            updateParticles();
            System.out.println(particles);
            cim.CellIndexMethodRun(this.particles);
        }
    }

    private void updateParticles() {

    }

}
