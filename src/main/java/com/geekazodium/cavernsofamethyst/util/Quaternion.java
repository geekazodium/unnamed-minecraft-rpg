package com.geekazodium.cavernsofamethyst.util;

public class Quaternion {
    private double x;
    private double y;
    private double z;
    private double w;
    public static Quaternion IDENTITY = new Quaternion(0,0,0,1);

    public Quaternion(Quaternion rotation) {
        this(rotation.getX(),rotation.getY(),rotation.getZ(),rotation.getW());
    }

    public Quaternion(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Quaternion fromEulerYxz(double y, double x, double z) {
        Quaternion quaternion = IDENTITY.copy();
        quaternion.hamiltonProduct(new Quaternion(0.0F, (float)Math.sin((y / 2.0F)), 0.0F, (float)Math.cos((y / 2.0F))));
        quaternion.hamiltonProduct(new Quaternion((float)Math.sin((x / 2.0F)), 0.0F, 0.0F, (float)Math.cos((x / 2.0F))));
        quaternion.hamiltonProduct(new Quaternion(0.0F, 0.0F, (float)Math.sin((z / 2.0F)), (float)Math.cos((z / 2.0F))));
        return quaternion;
    }

    public double getW() {
        return w;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public void hamiltonProduct(Quaternion other) {
        double x = this.x;
        double y = this.y;
        double z = this.z;
        double w = this.w;
        double x1= other.getX();
        double y1= other.getY();
        double z1= other.getZ();
        double w1= other.getW();
        this.x = w * x1 + x * w1 + y * z1 - z * y1;
        this.y = w * y1 - x * z1 + y * w1 + z * x1;
        this.z = w * z1 + x * y1 - y * x1 + z * w1;
        this.w = w * w1 - x * x1 - y * y1 - z * z1;
    }

    public Quaternion copy(){
        return new Quaternion(x,y,z,w);
    }
}
