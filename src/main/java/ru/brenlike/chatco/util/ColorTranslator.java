package ru.brenlike.chatco.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTranslator {
    public static final Pattern HEX_PATTERN = Pattern.compile("&\\(#(\\w{5}[0-9A-Za-z])\\)");

    /**
     * Default parsing
     * @param text Text to parse
     * @return Text minecraft component
     */
    public static @NotNull Component parse(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();

        Queue<TextColor> colors = new LinkedList<>();

        while (matcher.find()) {
            colors.add(TextColor.fromHexString("#" + matcher.group(1)));
            matcher.appendReplacement(builder, "\\\\div");
        }

        String line = matcher.appendTail(builder).toString();
        List<Component> components = new ArrayList<>();

        String[] strings = line.split("\\\\div");
        components.add(Component.text(ChatColor.translateAlternateColorCodes('&', strings[0])));
        strings[0] = null;

        for (String s:
                strings) {
            if (s == null) continue;
            s = ChatColor.translateAlternateColorCodes('&', s);

            components.add(Component.text(s).color(colors.poll()));
        }

        return Component.join(JoinConfiguration.builder().build(), components);
    }

    public static @NotNull Component parse(String text, String placeholder, Component replaceable) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();

        Queue<TextColor> colors = new LinkedList<>();

        while (matcher.find()) {
            colors.add(TextColor.fromHexString("#" + matcher.group(1)));
            matcher.appendReplacement(builder, "\\\\div");
        }

        String line = matcher.appendTail(builder).toString();
        List<Component> components = new ArrayList<>();

        String[] strings = line.split("\\\\div");
        components.add(Component.text(ChatColor.translateAlternateColorCodes('&', strings[0])));
        strings[0] = null;

        for (String s:
                strings) {
            if (s == null) continue;
            s = ChatColor.translateAlternateColorCodes('&', s);

            if (s.equalsIgnoreCase(placeholder)) {
                s = s.replace(placeholder, "");
                components.add(Component.text(s).color(colors.poll()));
                components.add(replaceable.style(components.get(components.size() - 1).style()));
                continue;
            }

            components.add(Component.text(s).color(colors.poll()));
        }

        return Component.join(JoinConfiguration.builder().build(), components);
    }
}
