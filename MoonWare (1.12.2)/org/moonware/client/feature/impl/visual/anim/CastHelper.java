package org.moonware.client.feature.impl.visual.anim;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.moonware.client.MoonWare;

import java.util.ArrayList;
import java.util.List;

public class CastHelper {
    private final List<EntityType> casts = new ArrayList<>();

    public static EntityType isInstanceof(Entity entity, EntityType... types) {
        for (EntityType type : types) {
            if (entity == Minecraft.player) {
                if (type == EntityType.SELF)
                    return type;
            }
            if (type == EntityType.VILLAGERS && entity instanceof EntityVillager) {
                return type;
            }
            if (type == EntityType.FRIENDS && entity instanceof EntityPlayer) {
                if (MoonWare.friendManager.isFriend(entity.getName())) {
                    return type;
                }
            }
            if (type == EntityType.PLAYERS && entity instanceof EntityPlayer
                    && entity != Minecraft.player && !MoonWare.friendManager.isFriend(entity.getName())) {
                return type;
            }
            if (type == EntityType.MOBS && entity instanceof EntityMob) {
                return type;
            }
            if (type == EntityType.ANIMALS && (entity instanceof EntityAnimal || entity instanceof EntityGolem)) {
                return type;
            }
        }
        return null;
    }

    public CastHelper apply(EntityType type) {
        casts.add(type);
        return this;
    }

    public EntityType[] build() {
        return casts.toArray(new EntityType[0]);
    }

    public enum EntityType {
        PLAYERS, MOBS, ANIMALS, VILLAGERS, FRIENDS, SELF
    }
}