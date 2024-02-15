package me.cross.entity;

import me.cross.handler.HorseNameHandler;
import net.minecraft.nbt.NbtCompound;

import java.util.Random;
import java.util.UUID;

public class HorseAbility {
    public UUID ownerUuid;
    public UUID horseUuid;
    public double speedMultiplier;
    public double jumpMultiplier;
    // 말이 변덕이 있어서 갑자기 멈춤
    public double crazyFactor;
    public String name;
    public boolean isBred;

    private static final Random random = new Random();

    public HorseAbility(UUID ownerUuid, UUID horseUuid) {
        this.ownerUuid = ownerUuid;
        this.horseUuid = horseUuid;

        setStatRandomly();
        // set random name
        name = HorseNameHandler.getHorseName(horseUuid);
    }

    @Override
    public String toString() {
        return "HorseAbility{" +
                "name='" + name +
                ", speedMultiplier=" + speedMultiplier +
                ", jumpMultiplier=" + jumpMultiplier +
                ", crazyFactor=" + crazyFactor +
                '}';
    }

    public void writeToNbt(NbtCompound horseAbilityNbt) {
        horseAbilityNbt.putDouble("SpeedMultiplier", speedMultiplier);
        horseAbilityNbt.putDouble("JumpMultiplier", jumpMultiplier);
        horseAbilityNbt.putDouble("CrazyFactor", crazyFactor);
        horseAbilityNbt.putString("Name", name);
    }

    public void readFromNbt(NbtCompound horseAbilityNbt) {
        speedMultiplier = horseAbilityNbt.getFloat("SpeedMultiplier");
        jumpMultiplier = horseAbilityNbt.getFloat("JumpMultiplier");
        crazyFactor = horseAbilityNbt.getFloat("CrazyFactor");
        name = setName(horseAbilityNbt.getString("Name"));
    }

    private void setStatRandomly() {
        // 0.7 < speedMultiplier < 1.2
        speedMultiplier = getRandomFloatInRange(0.7f, 1.2f);
        // 0.5 < jumpMultiplier < 1.5
        jumpMultiplier = getRandomFloatInRange(0.5f, 1.5f);
        // 미칠 확률 : 달리다 갑자기 멈출 확률, 0<crazyFactor<0.005
        crazyFactor = getRandomFloatInRange(0.0f, 0.005f);
    }

    private String setName(String nbtName) {
        if(nbtName == null || nbtName.isEmpty()) {
            name = HorseNameHandler.getHorseName(horseUuid);
        } else {
            name = nbtName;
        }
        return name;
    }

    private float getRandomFloatInRange(float min, float max) {
        return min + random.nextFloat() * (max - min);
    }
}
