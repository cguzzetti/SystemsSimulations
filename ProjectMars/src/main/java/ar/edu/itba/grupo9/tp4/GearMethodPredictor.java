package ar.edu.itba.grupo9.tp4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ar.edu.itba.grupo9.tp4.IntegralMethods.GEAR_PREDICTOR_CORRECTOR;
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
        predictRListX(nextParticle, p);

        nextParticle.setPositionX(nextParticle.rListX[0]);
        nextParticle.setVelocityX(nextParticle.rListX[1]);

        double realAcceleration = appliedForce.getForceX(time, nextParticle) / nextParticle.getMass();
        double deltaR2 = (realAcceleration - nextParticle.rListX[2]) * (pow(deltaT, 2)/factorial(2));

        correctRListX(nextParticle, deltaR2);

        nextParticle.setPositionX(nextParticle.rListX[0]);
        nextParticle.setVelocityX(nextParticle.rListX[1]);
        return nextParticle;
    }

    @Override
    public IntegralMethods getMethod() {
        return GEAR_PREDICTOR_CORRECTOR;
    }


    private void predictRListX(AcceleratedParticle nextParticle, AcceleratedParticle p){
        nextParticle.rListX[0] = p.rListX[0] + p.rListX[1] * deltaT + p.rListX[2] * (pow(deltaT, 2) / factorial(2))
                + p.rListX[3] * (pow(deltaT, 3) / factorial(3))
                + p.rListX[4] * (pow(deltaT, 4) / factorial(4))
                + p.rListX[5] * (pow(deltaT, 5) / factorial(5));
        nextParticle.rListX[1] = p.rListX[1] + p.rListX[2] * deltaT + p.rListX[3] * (pow(deltaT, 2) / factorial(2))
                + p.rListX[4] * (pow(deltaT, 3) / factorial(3))
                + p.rListX[5] * (pow(deltaT, 4) / factorial(4));
        nextParticle.rListX[2] = p.rListX[2] + p.rListX[3] * deltaT + p.rListX[4] * (pow(deltaT, 2) / factorial(2))
                + p.rListX[5] * (pow(deltaT, 3) / factorial(3));
        nextParticle.rListX[3] = p.rListX[3] + p.rListX[4] * deltaT + p.rListX[5] * (pow(deltaT, 2) / factorial(2));
        nextParticle.rListX[4] = p.rListX[4] + p.rListX[5] * deltaT;
        nextParticle.rListX[5] = p.rListX[5];
    }

    private void correctRListX(AcceleratedParticle nextParticle, double deltaR2){
        for (int i = 0; i < nextParticle.rListX.length; i++) {
            nextParticle.rListX[i] = nextParticle.rListX[i] + (coef[i] * deltaR2 * (factorial(i) / pow(deltaT, i)));
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
