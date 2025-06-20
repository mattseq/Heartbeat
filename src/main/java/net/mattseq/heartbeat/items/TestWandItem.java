package net.mattseq.heartbeat.items;

import net.mattseq.heartbeat.HeartbeatEffects;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TestWandItem extends Item {
    public TestWandItem() {
        super(new Item.Properties()
                .stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        CameraRenderEvent.triggerShake(100);
//        HandRenderEvent.triggerShake(100);
        HeartbeatEffects.updateBreathingEffect(10);
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}