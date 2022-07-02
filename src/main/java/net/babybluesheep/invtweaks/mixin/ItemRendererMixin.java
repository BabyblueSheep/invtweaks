package net.babybluesheep.invtweaks.mixin;

import net.babybluesheep.invtweaks.InvConfig;
import net.babybluesheep.invtweaks.InvTweaksClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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
    boolean displayFull = false;

    @Inject(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZII)I"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void scaleItemTextMixin(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci, MatrixStack matrixStack, String string, VertexConsumerProvider.Immediate immediate) {
        int digits = String.valueOf(stack.getCount()).length();
        if(digits > 3) {
            if(displayFull) {
                float scale = digits < 10 ? 1.264f - 0.116f*digits : 0.2f;
                float offset = digits < 10 ? (float) (1.33+0.47*Math.exp(0.66*digits)) : 225;
                matrixStack.scale(scale, scale, scale);
                renderer.draw(string, (x + 19 - 2 - renderer.getWidth(string))/scale + offset, ( y + 6 + 3)/scale, Integer.parseInt(InvConfig.getConfig().countColor, 16), true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
            }
            else {
                float scale = 1f;
                float offset = 0;
                String countLabelTrim;
                switch (digits) {
                    case 5, 8 -> countLabelTrim = string.substring(0, 2);
                    case 6, 9 -> {
                        countLabelTrim = string.substring(0, 3);
                        scale = 0.8f;
                        offset = 7;
                    }
                    default -> countLabelTrim = string.substring(0, 1);
                }
                switch (digits) {
                    case 4, 5, 6 -> countLabelTrim += 'k';
                    case 7, 8, 9 -> countLabelTrim += 'M';
                    case 10 -> countLabelTrim += 'B';
                }
                matrixStack.scale(scale, scale, scale);
                renderer.draw(countLabelTrim, (float)(x + 19 - 2 - renderer.getWidth(countLabelTrim))/scale + offset, ( y + 6 + 3)/scale, Integer.parseInt(InvConfig.getConfig().countColor, 16), true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
            }
            ClientTickEvents.END_CLIENT_TICK.register(client -> {
                while(InvTweaksClient.showFullKeybind.wasPressed()) {
                    displayFull = !displayFull;
                }
            });
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
