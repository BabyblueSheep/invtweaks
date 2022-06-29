package net.babybluesheep.invtweaks.mixin;

import net.minecraft.item.Item;
import org.checkerframework.checker.units.qual.C;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.item.Item$Settings")
public class ItemSettingsMixin {

    @Shadow
    int maxCount;

    @Inject(method = "<init>()V", at = @At("TAIL"))
    private void initMixin(CallbackInfo ci) {
        this.maxCount = 99;
    }
}
