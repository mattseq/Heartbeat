package net.mattseq.heartbeat;

import net.mattseq.heartbeat.breathing.BreathingEffectHandler;

public class HeartbeatEffects {
    public static void updateBreathingEffect(float fatigue) {

        boolean shouldEnable = fatigue > 0.4f;
        BreathingEffectHandler.setBreathingEnabled(shouldEnable);
        BreathingEffectHandler.setFatigue(fatigue);
    }
}