package io.veron.module;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Module {

    private boolean isEnabled = false;

    public abstract void onLoad(JavaPlugin jPlugin);
    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onReload();

    public void enable() {
        this.isEnabled = true;
        this.onEnable();
    }

    public void disable() {
        this.isEnabled = false;
        this.onDisable();
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public void reload() {
        this.disable();
        this.enable();
    }
}
