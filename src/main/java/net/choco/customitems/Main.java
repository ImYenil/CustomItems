package net.choco.customitems;

import lombok.Getter;
import net.choco.customitems.command.CustomItemCommand;
import net.choco.customitems.item.ItemHandler;
import net.choco.customitems.manager.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private FileManager fileManager;

    @Getter
    private ItemHandler itemHandler;

    @Getter
    private static String PREFIX = ChatColor.DARK_GRAY + "[" + ChatColor.RED + "CustomItems" + ChatColor.DARK_GRAY + "]" + ChatColor.RESET;

    @Override
    public void onEnable() {

        instance = this;

        long startTime = System.currentTimeMillis();

        fileManager = new FileManager(this);
        fileManager.getConfig("items.yml").copyDefaults(true).save();

        itemHandler = new ItemHandler();
        getCommand("customitems").setExecutor(new CustomItemCommand());
        getCommand("customitems").setTabCompleter(new CustomItemCommand());

        log(LOG_LEVEL.INFO, "The plugin has been activated (" + (System.currentTimeMillis() - startTime) / 1000.0 + "s)");
    }

    public void onDisable() {
        HandlerList.unregisterAll(this);

        getServer().getScheduler().cancelTasks(this);

        log(LOG_LEVEL.INFO, "The plugin has been disabled");
    }

    public static void log(LOG_LEVEL level, String text) {
        getInstance().getServer().getConsoleSender().sendMessage(getPREFIX() + " " + ChatColor.DARK_GRAY + "[" + level.getName() + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + text);
    }

    public enum LOG_LEVEL
    {
        INFO("INFO", 0, ChatColor.GREEN + "INFO"),
        WARNING("WARNING", 1, ChatColor.YELLOW + "WARNING"),
        ERROR("ERROR", 2, ChatColor.RED + "ERROR"),
        DEBUG("DEBUG", 3, ChatColor.AQUA + "DEBUG");

        @Getter
        private String name;

        private LOG_LEVEL(String s, int n, String name) {
            this.name = name;
        }
    }

    public void reload() {

        this.fileManager.getConfigs().values().forEach(c -> c.reload());

        itemHandler = new ItemHandler();
    }

    public static YamlConfiguration getItemsConfig() {
        return getInstance().fileManager.getConfig("items.yml").get();
    }
}
