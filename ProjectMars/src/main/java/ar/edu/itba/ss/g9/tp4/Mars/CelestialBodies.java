package ar.edu.itba.ss.g9.tp4.Mars;

public enum CelestialBodies {
    SUN (0),
    EARTH(1),
    MARS(2),
    SPACE_SHIP(3),
    ;
    final int id;
    CelestialBodies(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
