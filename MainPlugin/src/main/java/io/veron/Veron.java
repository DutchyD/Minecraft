package io.veron;

import io.veron.module.DirectoryException;
import io.veron.module.InvalidModuleException;
import io.veron.module.loader.ModuleLoader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Veron extends JavaPlugin {

    private ModuleLoader moduleLoader;
    private static final String MODULES_FOLDER_NAME = "modules";

    @Override
    public void onEnable() {

        try {
            moduleLoader = new ModuleLoader();

            File modulesFolder = new File(MODULES_FOLDER_NAME);

            if (!modulesFolder.exists())
                if (!modulesFolder.mkdirs())
                    return;

            moduleLoader.loadModules(modulesFolder, this);
        } catch (DirectoryException | IOException | IllegalAccessException | ClassNotFoundException | InstantiationException | NoSuchMethodException | InvocationTargetException | InvalidModuleException e) {
            e.printStackTrace();
        }

        moduleLoader.enable();
        moduleLoader.reload();
        moduleLoader.disable();
    }

    @Override
    public void onDisable() {
        this.moduleLoader.flush();
        this.moduleLoader = null;
    }
}
