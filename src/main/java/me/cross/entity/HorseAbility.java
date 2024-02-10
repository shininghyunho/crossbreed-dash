package me.cross.entity;

import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.random.Random;

public class HorseAbility {
    public AbstractHorseEntity horseEntity;
    public PlayerEntity playerEntity;
    public int level;
    public float speedMultiplier;
    public float jumpMultiplier;
    public float healthMultiplier;
    // 말이 변덕이 있어서 갑자기 멈춤
    public float crazyFactor;

    public HorseAbility(AbstractHorseEntity horseEntity, PlayerEntity playerEntity) {
        this.horseEntity = horseEntity;
        this.playerEntity = playerEntity;
        // set default value
        level = 1;
        setLevelOneRandomly(horseEntity.getRandom());
    }

    private void setLevelOneRandomly(Random random) {
        // 0<speedMultiplier<1
        speedMultiplier = random.nextFloat();
        // 0<jumpMultiplier<1
        jumpMultiplier = random.nextFloat();
        // 0<healthMultiplier<1
        healthMultiplier = random.nextFloat();
        // 0<crazyFactor<1
        crazyFactor = random.nextFloat();
    }

    @Override
    public String toString() {
        return "HorseAbility{" +
                "level=" + level +
                ", speedMultiplier=" + speedMultiplier +
                ", jumpMultiplier=" + jumpMultiplier +
                ", healthMultiplier=" + healthMultiplier +
                ", crazyFactor=" + crazyFactor +
                '}';
    }
}
