package ru.brenlike.chatco.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.brenlike.chatco.ChatCo;

import java.util.ArrayList;
import java.util.List;

public record ChatCoCommand(@NotNull ChatCo plugin) implements TabExecutor {

    static List<String> container = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length >= 1)
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.isOp()) {
                sender.sendMessage("\u00a7CВы должны быть оператором этого сервера!");
                return true;
            }

            plugin.reload();
            sender.sendMessage("\u00a7AКонфиг плагина \u00a7EChatCommunications \u00a7Aперезагружен.");

            return true;
        }

        sender.sendMessage("\u00a7Использование: /chatco reload");

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (container.isEmpty()) {
            if (sender.isOp()) container.add("reload");
        }

        List<String> result = new ArrayList<>();
        if (args.length == 1) {
            for (String a:
                    container){
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) result.add(a);
                else return container;
            }
            return result;
        }
        return null;
    }
}
