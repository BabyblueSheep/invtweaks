package net.babybluesheep.invtweaks.mixin;

import net.babybluesheep.invtweaks.InvConfig;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin {

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void scaleItemTextMixin(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci, MatrixStack matrixStack, String string, VertexConsumerProvider.Immediate immediate) {
        int digits = String.valueOf(stack.getCount()).length();
        if(digits > 3) {
            float scale = digits < 10 ? 1.264f - 0.116f*digits : 0.2f;
            float offset = digits < 10 ? (float) (1.33+0.47*Math.exp(0.66*digits)) : 225;
            matrixStack.scale(scale, scale, scale);
            renderer.draw(string, (x + 19 - 2 - renderer.getWidth(string))/scale + offset, ( y + 6 + 3)/scale, Integer.parseInt(InvConfig.getConfig().countColor, 16), true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
        }
        else {
            renderer.draw(string, (float)(x + 19 - 2 - renderer.getWidth(string)), (float)(y + 6 + 3), Integer.parseInt(InvConfig.getConfig().countColor, 16), true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
        }
    }


    @Redirect(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"))
    private int removeDraw(TextRenderer instance, String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light) {
        return 0;
    }


}
