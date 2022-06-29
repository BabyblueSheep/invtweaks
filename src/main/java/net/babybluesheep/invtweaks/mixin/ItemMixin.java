package net.babybluesheep.invtweaks.mixin;

import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Mutable
    @Shadow
    @Final
    static int DEFAULT_MAX_COUNT;

    @Inject(method = "<init>(Lnet/minecraft/item/Item$Settings;)V", at = @At("TAIL"))
    private void initMixin(Item.Settings settings, CallbackInfo ci) {
        DEFAULT_MAX_COUNT = 99;
    }
}
