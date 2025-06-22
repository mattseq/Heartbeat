package net.mattseq.heartbeat.meters;

import net.mattseq.heartbeat.Heartbeat;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.core.BlockPos;

public class FatigueMeter {
    private static float currentFatigue = 0.0f;

    public static void update(LocalPlayer player) {
        float fatigue = 0f;

        // Sprinting factor
        fatigue += player.isSprinting() ? 0.3f : 0.0f;

        // Movement speed factor (clamped)
        double speed = player.getDeltaMovement().length();
        fatigue += (float) (Math.min(speed / 0.3f, 1.0f) * 0.2f);

        // Swinging (placeholder: recently attacked)
        if (player.swinging) fatigue += 0.15f;

        // Jumping (in air)
        if (!player.onGround()) fatigue += 0.1f;

        // Heavy armor
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem armor) {
                switch (armor.getMaterial().getName()) {
                    case "netherite" -> fatigue += 0.08f;
                    case "diamond" -> fatigue += 0.06f;
                    case "iron" -> fatigue += 0.05f;
                    case "gold" -> fatigue += 0.04f;
                    case "chainmail" -> fatigue += 0.04f;
                }
            }
        }

        // Swimming or climbing
        if (player.isInWater() || player.horizontalCollision) fatigue += 0.15f;

        // Fire/Poison
        if (player.hasEffect(MobEffects.POISON) || player.isOnFire()) fatigue += 0.2f;

        // Cold biome exposure (approximate with biome tag)
        BlockPos pos = player.blockPosition();
        if (player.level().getBiome(pos).is(Biomes.SNOWY_TAIGA)) fatigue += 0.2f;

        // No food regen
        if (player.getFoodData().getSaturationLevel() <= 0 && player.getFoodData().getFoodLevel() < 20) {
            fatigue += 0.2f;
        }

        // High altitude
        if (player.getY() > 160) fatigue += 0.15f;

        // Clamp and smooth
        float delta = clamp01(fatigue) - currentFatigue;
        float lerpSpeed = delta > 0 ? 0.05f : 0.005f;
        currentFatigue += delta * lerpSpeed;

        Heartbeat.LOGGER.debug("Fatigue Meter: " + currentFatigue);
    }

    public static float getFatigue() {
        return currentFatigue;
    }

    private static float clamp01(float val) {
        return Math.max(0f, Math.min(1f, val));
    }
}
