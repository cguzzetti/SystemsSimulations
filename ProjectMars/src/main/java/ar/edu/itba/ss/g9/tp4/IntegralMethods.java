package ar.edu.itba.ss.g9.tp4;

/**
 *  Determines which Integral Method we are running
 */
public enum IntegralMethods {
    BEEMAN ("Beeman Method"),
    VERLET_ORIGINAL ("Verlet Original Type Method"),
    GEAR_PREDICTOR_CORRECTOR ("Gear Predictor Corrector of order V"),
    ;

    final String description;
    IntegralMethods(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
