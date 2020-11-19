package ar.edu.itba.ss.g9.tp5;

import java.util.LinkedList;
import java.util.List;

public class Configuration {

    // Obstacles
    static double       OBS_RADIUS          = 0.3;
    final static double OBS_MASS            = 60;

    //Box size
    static double HEIGHT = 10, WIDTH = 30, XSHIFT = OBS_RADIUS * 3, YSHIFT = OBS_RADIUS * 3;


    // Pedestrian
    static double       PED_RADIUS          = 0.3;
    final static double PED_MASS            = 60;

    // Epsilon
    final static double EPSILON             = Math.pow(10, -6);

    // Avoidance
    final static double UMAX                = 3;
    final static double TIME_LIMIT          = 10;
    static double PERSONAL_SPACE      = 0.5;
    static final int    NUMBER_OF_CRASHES   = 5;
    static final double DESIRED_VELOCITY    = 2.5;
    static final double TAU                 = 0.2;
    static final double WALL_SAFE_DISTANCE  = 1;
    static double       DMIN                = 2; // PED_RADIUS;
    static double       DMID                = 3;
    static final double DMAX                = 7;
    static final double MULTIPLIER          = 4;
    public static List<Integer> collidedIds = new LinkedList<>();
}
