package net.mattseq.heartbeat.meters;

import net.mattseq.heartbeat.Heartbeat;
import net.minecraft.client.player.LocalPlayer;

public class FatigueMeter {
    private float currentFatigue = 0.0f;  // range: 0.0 to 1.0

    // Update based on recent activity
    public void update(LocalPlayer player) {
        float sprintFactor = player.isSprinting() ? 1.0f : 0.0f;
        double speed = player.getDeltaMovement().length();
        float movementFactor = (float)Math.min(speed / 0.3, 1.0);
        float healthFactor = 1.0f - (player.getHealth() / player.getMaxHealth());
        float hungerFactor = 1.0f - (player.getFoodData().getFoodLevel() / 20.0f);
        float fallFactor = player.fallDistance > 3 ? (player.fallDistance - 3) / 10.0f : 0.0f;

        // Combine weighted activity
        float targetFatigue = clamp01(
                (sprintFactor * 0.3f) +
                        (movementFactor * 0.3f) +
                        (healthFactor * 0.2f) +
                        (hungerFactor * 0.1f) +
                        (fallFactor * 0.1f)
        );

        // Smooth interpolation toward target fatigue
        float delta = targetFatigue - currentFatigue;
        float lerpSpeed;

        if (delta > 0) {
            lerpSpeed = 0.05f;
        } else {
            lerpSpeed = 0.005f;
        }

        currentFatigue += (delta) * lerpSpeed;
        Heartbeat.LOGGER.debug("Fatigue Meter: " + currentFatigue);
    }

    public float getFatigue() {
        return currentFatigue;
    }

    private float clamp01(float val) {
        return Math.max(0f, Math.min(1f, val));
    }
}
