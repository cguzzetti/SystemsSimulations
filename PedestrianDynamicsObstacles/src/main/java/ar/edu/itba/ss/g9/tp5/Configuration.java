package ar.edu.itba.ss.g9.tp5;

public class Configuration {
    //Box size
    static double HEIGHT = 7, WIDTH = 25, XSHIFT = 2, YSHIFT = 0.2;

    // Obstacles
    static double       OBS_RADIUS          = 0.1;
    final static double OBS_MASS            = 1;

    // Pedestrian
    static double       PED_RADIUS          = 0.2;
    final static double PED_MASS            = 60;

    // Epsilon
    final static double EPSILON             = Math.pow(10, -6);

    // Avoidance
    final static double UMAX                = 3;
    final static double TIME_LIMIT          = 5;
    static final double PERSONAL_SPACE      = 1;
    static final int    NUMBER_OF_CRASHES   = 5;
    static final double DESIRED_VELOCITY    = 3;
    static final double TAU                 = 0.5;
    static final double WALL_SAFE_DISTANCE  = 0.5;
    static double       DMIN                = 2;
    static double       DMID                = 4;
    static final double DMAX                = 5;
    static final double MULTIPLIER          = 5;
}
