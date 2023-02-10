package ru.brenlike.chatco;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brenlike.chatco.util.ColorTranslator;

import java.util.HashSet;
import java.util.Set;

public class Config {
    private final Set<String> posAliases;
    private final Set<String> itemAliases;
    private final String prefix;
    private final String itemMessage;
    private final String posMessage;
    private final String noItemMessage;

    public Config(@NotNull FileConfiguration config) {
        posAliases = new HashSet<>();
        itemAliases = new HashSet<>();

        prefix = config.getString("prefix", "!");
        itemMessage = config.getString("commands.item.message", "%player_name% > {item}");
        noItemMessage = config.getString("commands.item.no-item", "&CВозьмите какой-нибудь предмет в главную руку!");
        posMessage = config.getString("commands.position.message", "%player_name% > {position}");

        if (config.getStringList("commands.position.aliases").size() >= 1) {
            for (String s: config.getStringList("commands.position.aliases"))
                posAliases.add(prefix + s);

        } else posAliases.add(prefix + "метка");

        if (config.getStringList("commands.item.aliases").size() >= 1) {
            for (String s:
                    config.getStringList("commands.item.aliases"))
                itemAliases.add(prefix + s);
        } else itemAliases.add(prefix + "предмет");
    }

    public boolean isItemAlias(@NotNull Component component) {
        TextComponent textComponent = (TextComponent) component;
        String content = textComponent.content();
        content = content.split(" ")[0];

        return content.startsWith(prefix) && itemAliases.contains(content);
    }

    public boolean isPosAlias(@NotNull Component component) {
        TextComponent textComponent = (TextComponent) component;
        String content = textComponent.content();
        content = content.split(" ")[0];

        return content.startsWith(prefix) && posAliases.contains(content);
    }

    public @NotNull Component getPosMessage(@NotNull Player sender) {
        Location loc = sender.getLocation();
        String location = "[X, Y, Z]";
        location = location.replace("X", String.valueOf(loc.getBlockX()));
        location = location.replace("Y", String.valueOf(loc.getBlockY()));
        location = location.replace("Z", String.valueOf(loc.getBlockZ()));

        String message = posMessage.replace("{position}", location);
        message = PlaceholderAPI.setPlaceholders(sender, message);
        return ColorTranslator.parse(message);
    }

    public @Nullable Component getItemMessage(@NotNull Player sender) {
        ItemStack i = sender.getInventory().getItemInMainHand();
        if (i.getType().isAir()) {
            sender.sendMessage(ColorTranslator.parse(PlaceholderAPI.setPlaceholders(sender, noItemMessage)));
            return null;
        }

        String count = "";

        if (i.getMaxStackSize() > 1) {
            count += ", " + i.getAmount();
        }

        Component c1 = Component.text("[");
        Component c2 = Component.translatable(i.getType().translationKey());
        Component c3 = Component.text(count);
        Component c4 = Component.text("]");

        Component item = Component.join(JoinConfiguration.builder().build(), c1, c2, c3, c4);

        return ColorTranslator.parse(PlaceholderAPI.setPlaceholders(sender, itemMessage), "{item}", item);
    }
}
