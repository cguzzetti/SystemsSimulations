package ar.edu.itba.grupo9.tp4;

/**
 *  Determines which Integral Method we are running
 */
public enum IntegralMethods {
    BEEMAN ("Beeman Method"),
    VERLET ("Verlet Type Method"),
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
