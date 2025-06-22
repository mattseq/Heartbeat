package net.mattseq.heartbeat.breathing;

import net.mattseq.heartbeat.Heartbeat;
import net.mattseq.heartbeat.meters.FatigueMeter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Heartbeat.MODID, value = Dist.CLIENT)
public class BreathingEffectHandler {
    private static float breathingTimer = 0.0f;
    private static boolean breathingEnabled = false;
    private static float currentOffset = 0.0f;
    private static float currentFatigue = 0.0f;

    @SubscribeEvent
    public static void onFovModify(ComputeFovModifierEvent event) {
        float baseFov = event.getFovModifier();

        breathingEnabled = FatigueMeter.getFatigue() > 0.4f;
        currentFatigue = FatigueMeter.getFatigue();

        if (!breathingEnabled) {
            // Smoothly return to normal FOV
            currentOffset += (0.0f - currentOffset) * 0.1f;
            event.setNewFovModifier(baseFov + currentOffset);
            return;
        }

        // More aggressive ramping function (makes 0.6 fatigue already strong)
        float breathingStrength = (float) Math.pow((currentFatigue - 0.4f) / 0.6f, 0.75); // nonlinear curve

        // Boost the max FOV sway
        final float maxFovOffset = 0.08f;
        breathingTimer += 0.05f + (breathingStrength * 0.2f);
        float targetOffset = (float) Math.sin(breathingTimer) * maxFovOffset * breathingStrength;

        currentOffset += (targetOffset - currentOffset) * 0.2f;

        event.setNewFovModifier(baseFov + currentOffset);
    }
}
