package net.babybluesheep.invtweaks.mixin;

import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BundleItem.class)
public interface BundleAccessor {
    @Invoker("addToBundle")
    static int invokeToBundle(ItemStack bundle, ItemStack stack) {
        throw new AssertionError();
    }

    @Invoker("getBundleOccupancy")
    static int invokeBundleOccupancy(ItemStack stack) {
        throw new AssertionError();
    }

    @Invoker("getItemOccupancy")
    static int invokeItemOccupancy(ItemStack stack) {
        throw new AssertionError();
    }

}
