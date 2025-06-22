package net.mattseq.heartbeat.tick_events;

import net.mattseq.heartbeat.Heartbeat;
import net.mattseq.heartbeat.HeartbeatEffects;
import net.mattseq.heartbeat.meters.FatigueMeter;
import net.mattseq.heartbeat.meters.FearMeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Heartbeat.MODID, value = Dist.CLIENT)
public class ClientTickHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getInstance().isPaused()) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        FatigueMeter.update(player);
        FearMeter.update(player);
    }
}
