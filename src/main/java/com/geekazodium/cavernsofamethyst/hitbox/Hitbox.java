package com.geekazodium.cavernsofamethyst.hitbox;

import com.geekazodium.cavernsofamethyst.util.Quaternion;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

import static com.geekazodium.cavernsofamethyst.hitbox.CollisionUtil.applyRotationMatrix;
import static com.geekazodium.cavernsofamethyst.hitbox.CollisionUtil.getRotationMatrix;
import static java.lang.Math.toRadians;

public class Hitbox {
    public Quaternion finalRotation;
    public Vector pos = new Vector(0, 0, 0);
    public Vector offset;
    public Vector center;
    public Vector size;
    public Quaternion rotation;
    private byte copyRotationType = 3;
    private final CollisionUtil.OOB collider;
    private Vector effectiveOffset;

    public Hitbox(Vector offset,
                  Vector center,
                  Vector size,
                  Quaternion rotation) {
        this.offset = offset;
        this.center = center;
        this.size = size;
        this.rotation = rotation;
        collider = new CollisionUtil.OOB(this, rotation);
    }

    public Hitbox(Vector offset,
                  Vector center,
                  Vector size,
                  Quaternion rotation,
                  byte copyRotationType) {
        this(offset, center, size, rotation);
        this.copyRotationType = copyRotationType;
    }

    public static Hitbox fromBoundingBox(BoundingBox boundingBox) {
        return new Hitbox(
                new Vector(0,0,0),
                new Vector(0.5,0.5,0.5),
                new Vector(boundingBox.getWidthX(),boundingBox.getHeight(), boundingBox.getWidthZ()),
                Quaternion.fromEulerYxz(0,0,0),
                (byte)0
        );
    }

    public boolean isColliding(Hitbox otherHitbox) {
        return CollisionUtil.getCollision(collider, otherHitbox.getCollider());
    }
    public CollisionUtil.OOB getCollider() {
        return this.collider;
    }

    private void updateCollider(Vector pos, Quaternion rotation) {
        updateRotation(rotation);
        updateEffectiveOffset(rotation);
        updatePos(pos);
    }

    public void updatePos(Vector pos) {
        this.pos = pos.clone();
        this.collider.updatePos(this);
    }

    public void updateEffectiveOffset(Quaternion rotation) {
        Vector[] m = getRotationMatrix(this.rotation);
        Vector o = offset.clone().add(applyRotationMatrix(new Vector(0.5, 0.5, 0.5)
                .subtract(center)
                .multiply(size), m));
        m = getRotationMatrix(rotation);
        this.effectiveOffset = applyRotationMatrix(o, m);
    }

    public void updateCollider(Vector pos,float pitch,float yaw){
        this.updateCollider(
                pos,
                Quaternion.fromEulerYxz(
                        this.copyRotationType==1||this.copyRotationType==3?(float) toRadians(-yaw):0,
                        this.copyRotationType==2||this.copyRotationType==3?(float) toRadians(pitch):0,
                        0
                )
        );
    }

    public void updateRotation(Quaternion rotation) {
        this.finalRotation = new Quaternion(rotation);
        this.finalRotation.hamiltonProduct(this.rotation);
        this.collider.setRotationAxis(getRotationMatrix(finalRotation));
    }

    public Vector centerPoint() {
        if (this.effectiveOffset == null) {
            this.effectiveOffset = this.offset.clone();
        }
        return effectiveOffset.clone().add(pos);
    }

    public Vector[] getOutline() {
        Vector pos = centerPoint();
        Vector[] points = new Vector[]{
                new Vector(0, 0, 0), new Vector(1, 0, 0),
                new Vector(0, 1, 0), new Vector(1, 1, 0),
                new Vector(0, 0, 1), new Vector(1, 0, 1),
                new Vector(0, 1, 1), new Vector(1, 1, 1)
        };
        List<Vector> r = new ArrayList<>();
        if (finalRotation == null) {
            finalRotation = new Quaternion(Quaternion.IDENTITY);
        }
        for (Vector point : points) {
            Vector vec = applyRotationMatrix(new Vector(
                    (point.getX() - 0.5d) * size.getX(),
                    (point.getY() - 0.5d) * size.getY(),
                    (point.getZ() - 0.5d) * size.getZ()
            ), getRotationMatrix(this.finalRotation)).clone().add(pos);
            r.add(vec);
        }
        return r.toArray(new Vector[0]);
    }

    public void updateCollider(Location location) {
        updateCollider(
                new Vector(location.getX(),location.getY(),location.getZ()),
                location.getPitch(),
                location.getYaw()
        );
    }
}
