package ar.edu.itba.ss.g9.tp4;

public class AnaliticalMethod implements IntegralMethod {
    private double k;
    private double gamma;
    private IntegralMethods method;

    public AnaliticalMethod(double k, double gamma) {
        this.k = k;
        this.gamma = gamma;
        this.method = IntegralMethods.ANALITICAL;
    }

    @Override
    public IntegralMethods getMethod() {
        return method;
    }

    @Override
    public AcceleratedParticle moveParticle(AcceleratedParticle particle, double time) {
        AcceleratedParticle nextParticle = new AcceleratedParticle(particle.getId(), particle.getRadius(), particle.getMass());
        nextParticle.setPrevPosition(particle.getPosition());
        nextParticle.setPrevVelocity(particle.getVelocity());

        updatePosition(nextParticle, particle, time);
        return nextParticle;
    }

    private void updatePosition(AcceleratedParticle nextParticle, AcceleratedParticle particle, double time) {
        nextParticle.setPositionX(Math.exp(-(gamma/(2 * nextParticle.getMass()) * time))
                * Math.cos(Math.pow(k/nextParticle.getMass() - (gamma*gamma)/(4 * nextParticle.getMass()*nextParticle.getMass()), 0.5) * time));
    }

}
