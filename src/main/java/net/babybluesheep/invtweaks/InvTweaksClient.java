package net.babybluesheep.invtweaks;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InvTweaksClient implements ClientModInitializer {

    public static KeyBinding showFullKeybind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.invtweaks.full_count",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_LEFT_ALT,
            "key.categories.inventory"
    ));

    @Override
    public void onInitializeClient() {

    }
}
