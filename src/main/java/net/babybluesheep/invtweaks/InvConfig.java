package net.babybluesheep.invtweaks;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import org.checkerframework.checker.units.qual.min;

@Config(name = "invtweaks")
public class InvConfig implements ConfigData {
    @ConfigEntry.Gui.Excluded
    private transient static boolean registered = false;

    @ConfigEntry.Gui.Tooltip
    public int stackLimit = 99;

    @ConfigEntry.Gui.Tooltip
    public String countColor = "ffffff";

    public static synchronized InvConfig getConfig() {
        if (!registered) {
            AutoConfig.register(InvConfig.class, GsonConfigSerializer::new);
            registered = true;
        }

        return AutoConfig.getConfigHolder(InvConfig.class).getConfig();
    }
}
