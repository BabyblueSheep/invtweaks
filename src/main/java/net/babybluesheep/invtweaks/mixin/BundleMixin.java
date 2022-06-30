package net.babybluesheep.invtweaks.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(BundleItem.class)
public class BundleMixin {
    @Shadow
    @Final
    @Mutable
    public static int MAX_STORAGE;

    @Inject(method = "<init>(Lnet/minecraft/item/Item$Settings;)V", at=@At("TAIL"))
    private void initMixin(Item.Settings settings, CallbackInfo ci) {
        MAX_STORAGE = 99;
    }

    @Inject(method = "getAmountFilled(Lnet/minecraft/item/ItemStack;)F", at = @At("HEAD"), cancellable = true)
    private static void getAmountMixin(ItemStack stack, CallbackInfoReturnable<Float> cir) {
        cir.setReturnValue(BundleAccessor.invokeBundleOccupancy(stack) / 99f);
    }

    @ModifyVariable(method = "onStackClicked(Lnet/minecraft/item/ItemStack;Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/util/ClickType;Lnet/minecraft/entity/player/PlayerEntity;)Z",  at = @At("STORE"), ordinal = 1)
    private int onStackMixin(int original, ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        return BundleAccessor.invokeToBundle(stack, slot.takeStackRange(slot.getStack().getCount(), (99 - BundleAccessor.invokeBundleOccupancy(stack)) / BundleAccessor.invokeItemOccupancy(slot.getStack()), player));
    }

    @Inject(method = "getItemBarStep(Lnet/minecraft/item/ItemStack;)I", at = @At("HEAD"), cancellable = true)
    private void mixinItemBarStep(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.min(1 + 12 * BundleAccessor.invokeBundleOccupancy(stack) / 99, 13));
    }

    @ModifyVariable(method = "addToBundle(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)I", at = @At("STORE"), ordinal = 2)
    private static int addToMixin(int original, ItemStack bundle, ItemStack stack) {
        int i = BundleAccessor.invokeBundleOccupancy(bundle);
        int j = BundleAccessor.invokeItemOccupancy(stack);
        return Math.min(stack.getCount(), (99 - i) / j);
    }

    @Inject(method = "getItemOccupancy(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void getItemMixinOne(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(99);
    }

    @Inject(method = "getItemOccupancy(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "RETURN", ordinal = 2), cancellable = true)
    private static void getItemMixinTwo(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(99 / stack.getMaxCount());
    }

    @Inject(method = "appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Ljava/util/List;Lnet/minecraft/client/item/TooltipContext;)V", at = @At("TAIL"))
    private void appendMixin(ItemStack stack, World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci) {
        tooltip.clear();
        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", BundleAccessor.invokeBundleOccupancy(stack), 99).formatted(Formatting.GRAY));
    }
}
