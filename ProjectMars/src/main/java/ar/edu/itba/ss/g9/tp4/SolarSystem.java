package ar.edu.itba.ss.g9.tp4;

import java.awt.geom.Point2D;

public class SolarSystem {

    Force force;
    IntegralMethod method;
    double deltaT;

    AcceleratedParticle sun;
    AcceleratedParticle earth;
    AcceleratedParticle mars;

    public SolarSystem(Force force, IntegralMethods method, double deltaT) {
        this.force = force;
        this.deltaT = deltaT;
        this.method = initializeMethod(method);

        initializeBodies();
    }

    private IntegralMethod initializeMethod(IntegralMethods method){
        switch (method){
            case GEAR_PREDICTOR_CORRECTOR:
                return new GearMethodPredictor(this.deltaT, this.force);
            case BEEMAN:
                return new BeemanMethod(this.force, this.deltaT);
            case VERLET_ORIGINAL:
                return new VerletMethod(this.force, this.deltaT);
            default:
                System.out.println("Invalid initialization method");
                return null;
        }
    }

    private void initializeBodies() {
        // id, position (m), velocity (m/s), radius (m), mass (kg)
        this.sun = new AcceleratedParticle(0, new Point2D.Double(0,0)
                , new Point2D.Double(0,0), 695700E3, 1988500E24);

        this.earth = new AcceleratedParticle(1, new Point2D.Double(1.493188929636662E+11, 1.318936357931255E+10)
                , new Point2D.Double(-3.113279917782445E+3, 2.955205189256462E+4), 6371E3, 5.97219E24);

        this.mars = new AcceleratedParticle(2, new Point2D.Double(2.059448551842169E+11, 4.023977946528339E+10)
                , new Point2D.Double(-3.717406842095575E+3, 2.584914078301731E+4), 3389E3, 6.4171E23);
    }
}
