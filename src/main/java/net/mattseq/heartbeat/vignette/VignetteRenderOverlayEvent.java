package net.mattseq.heartbeat.vignette;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.mattseq.heartbeat.Heartbeat;
import net.mattseq.heartbeat.meters.FearMeter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Heartbeat.MODID, value = Dist.CLIENT)
public class VignetteRenderOverlayEvent {

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() == VanillaGuiOverlay.HOTBAR.type()) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null) return;

            int width = event.getWindow().getGuiScaledWidth();
            int height = event.getWindow().getGuiScaledHeight();

            // Get fear value
            float fear = FearMeter.getFear(); // Replace with your instance
            float alpha = clamp01(fear); // Fade based on fear

            RenderSystem.disableDepthTest();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, ResourceLocation.fromNamespaceAndPath("minecraft", "textures/misc/vignette.png"));
            RenderSystem.setShaderColor(.0f, 0.0f, 0.0f, alpha);

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();

            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            buffer.vertex(0, height, -90).uv(0, 1).endVertex();
            buffer.vertex(width, height, -90).uv(1, 1).endVertex();
            buffer.vertex(width, 0, -90).uv(1, 0).endVertex();
            buffer.vertex(0, 0, -90).uv(0, 0).endVertex();
            tesselator.end();

            RenderSystem.setShaderColor(1, 1, 1, 1); // reset
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    private static float clamp01(float val) {
        return Math.max(0f, Math.min(1f, val));
    }
}
