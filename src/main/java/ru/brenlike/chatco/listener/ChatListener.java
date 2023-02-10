package ru.brenlike.chatco.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import ru.brenlike.chatco.ChatCo;

public record ChatListener(@NotNull ChatCo plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMessage(AsyncChatEvent e) {
        Player sender = e.getPlayer();
        Component msg = e.message();

        if (plugin.config().isItemAlias(msg)) {
            Component message = plugin.config().getItemMessage(sender);

            e.setCancelled(true);

            if (message == null) return;
            for (Player receiver:
                    Bukkit.getOnlinePlayers()) {
                receiver.sendMessage(message);
            }
        }
        if (plugin.config().isPosAlias(msg)) {
            Component message = plugin.config().getPosMessage(sender);

            e.setCancelled(true);

            for (Player receiver:
                    Bukkit.getOnlinePlayers()) {
                receiver.sendMessage(message);
            }
        }
    }

}
