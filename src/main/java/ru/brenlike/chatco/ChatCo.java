package ru.brenlike.chatco;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import ru.brenlike.chatco.command.ChatCoCommand;
import ru.brenlike.chatco.listener.ChatListener;

public final class ChatCo extends JavaPlugin {

    private Config config;
    private static ChatCo instance;

    public static ChatCo instance() {
        return instance;
    }

    public Config config() {
        return config;
    }

    public void reload() {
        reloadConfig();
        config = new Config(getConfig());
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        getCommand("chatco").setExecutor(new ChatCoCommand(this));
        getCommand("chatco").setTabCompleter(new ChatCoCommand(this));

        config = new Config(getConfig());
        instance = this;

        getLogger().info(ChatColor.GREEN + "Plugin enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
