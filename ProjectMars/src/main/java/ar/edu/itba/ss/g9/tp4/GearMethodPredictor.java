package ar.edu.itba.ss.g9.tp4;

import static ar.edu.itba.ss.g9.tp4.IntegralMethods.GEAR_PREDICTOR_CORRECTOR;
import static java.lang.Math.pow;

/**
 * Run the Order 5 gear method predictor
 */
public class GearMethodPredictor implements IntegralMethod{
    private double deltaT;
    private Force appliedForce;

    // Values of the coefficients for a Gear Predictor-Corrector of order V
    double[] coef = { (3.0 / 20), (251.0 / 260), 1.0, (11.0 / 18), (1.0 / 6), (1.0 / 60)};

    // Values of the coefficients for a Gear Predictor-Corrector of order V
    // when forces depend on position and velocity
    double[] coef_velocity = { (3.0 / 20), (251.0 / 260), 1.0, (11.0 / 18), (1.0 / 6), (1.0 / 60)};

    public GearMethodPredictor(double deltaT, Force appliedForce){
        this.deltaT       = deltaT;
        this.appliedForce = appliedForce;
    }

    public AcceleratedParticle moveParticle(AcceleratedParticle p, double time){
        AcceleratedParticle nextParticle = new AcceleratedParticle(
                p.getId(), p.getPosition(), p.getVelocity(), p.getRadius(), p.getMass()
        );
        predictRx(nextParticle, p);

        nextParticle.setPositionX(nextParticle.equationListX[0]);
        nextParticle.setVelocityX(nextParticle.equationListX[1]);

        double realAcceleration = appliedForce.getForceX(time, nextParticle) / nextParticle.getMass();
        double deltaR2 = (realAcceleration - nextParticle.equationListX[2]) * (pow(deltaT, 2)/factorial(2));

        correctRx(nextParticle, deltaR2);

        nextParticle.setPositionX(nextParticle.equationListX[0]);
        nextParticle.setVelocityX(nextParticle.equationListX[1]);
        return nextParticle;
    }

    @Override
    public IntegralMethods getMethod() {
        return GEAR_PREDICTOR_CORRECTOR;
    }


    private void predictRx(AcceleratedParticle nextParticle, AcceleratedParticle p){
        nextParticle.equationListX[0] = p.equationListX[0] + p.equationListX[1] * deltaT + p.equationListX[2] * (pow(deltaT, 2) / factorial(2))
                + p.equationListX[3] * (pow(deltaT, 3) / factorial(3))
                + p.equationListX[4] * (pow(deltaT, 4) / factorial(4))
                + p.equationListX[5] * (pow(deltaT, 5) / factorial(5));
        nextParticle.equationListX[1] = p.equationListX[1] + p.equationListX[2] * deltaT + p.equationListX[3] * (pow(deltaT, 2) / factorial(2))
                + p.equationListX[4] * (pow(deltaT, 3) / factorial(3))
                + p.equationListX[5] * (pow(deltaT, 4) / factorial(4));
        nextParticle.equationListX[2] = p.equationListX[2] + p.equationListX[3] * deltaT + p.equationListX[4] * (pow(deltaT, 2) / factorial(2))
                + p.equationListX[5] * (pow(deltaT, 3) / factorial(3));
        nextParticle.equationListX[3] = p.equationListX[3] + p.equationListX[4] * deltaT + p.equationListX[5] * (pow(deltaT, 2) / factorial(2));
        nextParticle.equationListX[4] = p.equationListX[4] + p.equationListX[5] * deltaT;
        nextParticle.equationListX[5] = p.equationListX[5];
    }

    private void correctRx(AcceleratedParticle nextParticle, double deltaR2){
        for (int i = 0; i < nextParticle.equationListX.length; i++) {
            nextParticle.equationListX[i] = nextParticle.equationListX[i] + (coef[i] * deltaR2 * (factorial(i) / pow(deltaT, i)));
        }
    }

    /* package */ static int factorial(int number){
        if(number < 0)
            throw new IllegalArgumentException();

        return factorialR(number);
    }

    private static int factorialR(int number){
        if (number == 1 || number == 0){
            return 1;
        }

        return number * factorialR( number -1);
    }
}
