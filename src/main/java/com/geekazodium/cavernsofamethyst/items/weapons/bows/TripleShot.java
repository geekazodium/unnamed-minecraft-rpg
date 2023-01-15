package com.geekazodium.cavernsofamethyst.items.weapons.bows;

import com.geekazodium.cavernsofamethyst.hitbox.CollisionUtil;
import com.mojang.math.Quaternion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.util.Vector;

import static java.lang.Math.toRadians;

public class TripleShot extends BowItemHandler {
    public TripleShot() {
        super(0,"triple_shot");
    }

    @Override
    protected ItemStack item() {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    /*
        @Override
        public int baseMana(Player player) {
            return 10;
        }*/
    @Override
    public int earthBaseDamage() {
        return 5;
    }

    @Override
    protected void spawnArrow(Player player, Location eyeLocation, Vector direction, PersistentDataContainer container) {
        super.spawnArrow(player, eyeLocation, direction, container);
        spawnOffsetArrow(player, eyeLocation, 0.2, container);
        spawnOffsetArrow(player, eyeLocation, -0.2, container);
    }

    private void spawnOffsetArrow(Player player, Location eyeLocation,double offset, PersistentDataContainer container) {
        Quaternion quaternion = Quaternion.fromYXZ(
                ((float) toRadians(-eyeLocation.getYaw())),
                ((float) toRadians(eyeLocation.getPitch())),
                0
        );
        quaternion.mul(Quaternion.fromYXZ((float) offset,0,0));
        Vector vector = CollisionUtil.applyRotationMatrix(new Vector(0,0,1), CollisionUtil.getRotationMatrix(quaternion));
        super.spawnArrow(player, eyeLocation, vector, container);
    }
}
