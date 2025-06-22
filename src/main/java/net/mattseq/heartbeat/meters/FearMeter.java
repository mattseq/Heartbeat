package net.mattseq.heartbeat.meters;

import net.mattseq.heartbeat.Heartbeat;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.monster.Monster;

public class FearMeter {
    private static float currentFear = 0.0f; // range: 0.0 to 1.0

    public static void update(LocalPlayer player) {
        // Fear sources
        int nearbyMobs = player.level().getEntitiesOfClass(Monster.class, player.getBoundingBox().inflate(12)).size();
        float mobFactor = clamp01(nearbyMobs / 10.0f); // up to 10 mobs = max effect

        float healthFactor = 1.0f - (player.getHealth() / player.getMaxHealth());

        float voidFactor = player.getY() < 0 ? 1.0f : player.getY() < 20 ? (20 - (float)player.getY()) / 20.0f : 0.0f;

        float combatFactor = player.hurtTime > 0 || player.getLastHurtByMob() != null ? 1.0f : 0.0f;

        // Weighted total
        float targetFear = clamp01(
                (mobFactor * 0.4f) +
                        (healthFactor * 0.3f) +
                        (voidFactor * 0.2f) +
                        (combatFactor * 0.1f)
        );

        // Smooth interpolation toward target fear
        float delta = targetFear - currentFear;
        float lerpSpeed = delta > 0 ? 0.05f : 0.005f;
        currentFear += delta * lerpSpeed;

        Heartbeat.LOGGER.debug("Fear Meter: " + currentFear);
    }

    public static float getFear() {
        return currentFear;
    }

    private static float clamp01(float val) {
        return Math.max(0f, Math.min(1f, val));
    }
}
