package com.grave.Game;

public enum Gun {
    ASSAULT("Assault Riffle", 2.0f, 34.0f, 3.0f, 3.0f, 20, 0.1f, 1, 0.0f, 10),
    ASSAULT2("Assault Riffle 2", 3f, 30f, 4.0f, 4.0f, 25, 0.3f, 3, 0.1f, 7),
    MACHINE("Machinegun", 10.0f, 40.0f, 5.0f, 10.0f, 150, 0.04f, 1, 0.0f, 24f),
    PISTOLE("Pistole",10.0f,23.0f,5.0f,1.0f,10,0.4f,1,0.0f, 0);

    private final String name;
    private final float mass;
    private final float speed;
    private final float time;
    private final float reload;
    private final int magazine;
    private final float gap;
    private final int salvo;
    private final float sgap;
    private final float spread;

    Gun(String name, float mass, float speed, float time, float reload, int magazine, float gap, int salvo, float sgap, float spread) {
        this.name = name;
        this.mass = mass;
        this.speed = speed;
        this.time = time;
        this.reload = reload;
        this.magazine = magazine;
        this.gap = gap;
        this.salvo = salvo;
        this.sgap = sgap;
        this.spread = spread;
    }

    public String getName() {
        return name;
    }

    public float getMass() {
        return mass;
    }

    public float getSpeed() {
        return speed;
    }

    public float getTime() {
        return time;
    }

    public float getReload() {
        return reload;
    }

    public int getMagazine() {
        return magazine;
    }

    public float getGap() {
        return gap;
    }

    public int getSalvo() {
        return salvo;
    }

    public float getSgap() {
        return sgap;
    }

    public float getSpread() {
        return spread;
    }
}
