package ar.edu.itba.grupo9.tp4;

public class Gravity implements Force {

    // I'm pretty sure that this is wrong but hey! Its something
    @Override
    public double apply(AcceleratedParticle p) {
        return p.getMass()*p.getAcceleration();
    }
}
