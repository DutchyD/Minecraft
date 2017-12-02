package io.veron;

import io.veron.module.Module;
import io.veron.module.ModuleInformation;
import org.bukkit.plugin.java.JavaPlugin;

@ModuleInformation(id = "Essentials")
public class EssentialsModule extends Module {

    private JavaPlugin jPlugin;

    public void onLoad(JavaPlugin jPlugin) {
        this.jPlugin = jPlugin;
        jPlugin.getServer().broadcastMessage("Essentials Module Loaded");
    }

    public void onEnable() {
        jPlugin.getServer().broadcastMessage("Essentials Module Enabled");
    }

    public void onDisable() {
        jPlugin.getServer().broadcastMessage("Essentials Module Disabled");
    }

    public void onReload() {
        jPlugin.getServer().broadcastMessage("Essentials Module Reloaded");
    }
}
