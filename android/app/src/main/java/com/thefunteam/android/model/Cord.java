package com.thefunteam.android.model;

public class Cord {
    public static float global_scale = 2.0f;
    public float x;
    public float y;

    public Cord(float x, float y) {
        this.x = x * global_scale;
        this.y = y * global_scale;
    }

    public Cord(float x, float y, boolean f) {
        this.x = x;
        this.y = y;
    }

    public static Cord middle(Cord cord1, Cord cord2) {
        return new Cord((cord1.x + cord2.x) / 2, (cord1.y + cord2.y) / 2, false);
    }

    public static double dist(Cord touch, Cord mid) {
        return Math.sqrt(Math.pow(Math.abs(touch.x - mid.x), 2) + Math.pow(Math.abs(touch.y - mid.y), 2));
    }
}
