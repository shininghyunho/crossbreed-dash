package me.cross.entity;

import me.cross.handler.HorseNameHandler;
import net.minecraft.nbt.NbtCompound;

import java.util.Random;
import java.util.UUID;

public class HorseAbility {
    public UUID ownerUuid;
    public UUID horseUuid;
    public int level;
    public float speedMultiplier;
    public float jumpMultiplier;
    public float healthMultiplier;
    // 말이 변덕이 있어서 갑자기 멈춤
    public float crazyFactor;
    public String name = "";

    private static final Random random = new Random();

    public HorseAbility(UUID ownerUuid, UUID horseUuid) {
        this.ownerUuid = ownerUuid;
        this.horseUuid = horseUuid;
        // set default value
        level = 1;
        setLevelOneRandomly();
    }

    private void setLevelOneRandomly() {
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

    public void writeToNbt(NbtCompound horseAbilityNbt) {
        horseAbilityNbt.putInt("Level", level);
        horseAbilityNbt.putFloat("SpeedMultiplier", speedMultiplier);
        horseAbilityNbt.putFloat("JumpMultiplier", jumpMultiplier);
        horseAbilityNbt.putFloat("HealthMultiplier", healthMultiplier);
        horseAbilityNbt.putFloat("CrazyFactor", crazyFactor);
        horseAbilityNbt.putString("Name", name);
    }

    public void readFromNbt(NbtCompound horseAbilityNbt) {
        level = horseAbilityNbt.getInt("Level");
        speedMultiplier = horseAbilityNbt.getFloat("SpeedMultiplier");
        jumpMultiplier = horseAbilityNbt.getFloat("JumpMultiplier");
        healthMultiplier = horseAbilityNbt.getFloat("HealthMultiplier");
        crazyFactor = horseAbilityNbt.getFloat("CrazyFactor");
        name = setName(horseAbilityNbt.getString("Name"));
    }

    private String setName(String nbtName) {
        if(nbtName == null || nbtName.isEmpty()) {
            name = HorseNameHandler.getHorseName(horseUuid);
        } else {
            name = nbtName;
        }
        return name;
    }
}
