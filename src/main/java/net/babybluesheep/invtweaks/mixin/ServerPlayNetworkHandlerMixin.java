package net.babybluesheep.invtweaks.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @ModifyConstant(method = "onCreativeInventoryAction", constant = @Constant(intValue = 64))
    private int onCreativeInventoryMixin(int original) {
        return 99;
    }
}
