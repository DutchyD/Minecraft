package io.veron.module.loader;

import io.veron.module.DirectoryException;
import io.veron.module.InvalidModuleException;
import io.veron.module.Module;
import io.veron.module.ModuleInformation;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModuleLoader {

    private static final String FILE_TYPE = ".jar";
    private HashMap<String, Module> modules = new HashMap<>();

    public void loadModules(final File folder, final JavaPlugin jPlugin) throws DirectoryException, IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, InvalidModuleException {
        if (!folder.isDirectory())
            throw new DirectoryException("File has to be a directory"); //TODO: Logger

        File[] files = folder.listFiles((dir, name) -> name.endsWith(FILE_TYPE));

        assert files != null;

        for (final File file : files) {
            final JarFile jar = new JarFile(file);
            final Enumeration<JarEntry> entries = jar.entries();

            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();

                if (entry.isDirectory() || !entry.getName().endsWith(".class"))
                    continue;

                final URL[] urls = { new URL("jar:file:" + file.getPath() + "!/") };
                final URLClassLoader classLoader = URLClassLoader.newInstance(urls);

                String className = entry.getName().substring(0, entry.getName().length() - String.valueOf(".class").length());
                className = className.replace("/", ".");
                System.out.println("Attempting to load class " + className);
                final Class<?> cls = classLoader.loadClass(className);

                if (!cls.isAnnotationPresent(ModuleInformation.class)) {
                    System.out.println("ModuleInformation not present");
                    if (!Module.class.isAssignableFrom(cls)) {
                        System.out.println("cannot be assigned from Module");
                        continue;
                    }
                }

                System.out.println("Loading class " + className);

                final Module module = (Module) cls.getConstructor().newInstance();

                final String id = cls.getDeclaredAnnotation(ModuleInformation.class).id();

                if (!id.equals("") && !modules.containsKey(id))
                    throw new InvalidModuleException("Module ID empty or already in use"); //TODO: Logger

                this.modules.put(id, module);

                module.onLoad(jPlugin);
            }
        }

        jPlugin.getLogger().fine("Modules done loading");
    }

    public void enable() {
        for (Module module : this.modules.values()) {
            module.enable();
        }
    }

    public void disable() {
        for (Module module : this.modules.values()) {
            module.disable();
        }
    }

    public void flush() {
        this.disable();
        this.modules.clear();
    }

    public void reload() {
        this.disable();
        this.enable();
    }
}
