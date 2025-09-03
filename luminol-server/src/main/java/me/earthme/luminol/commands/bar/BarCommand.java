package me.earthme.luminol.commands.bar;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.command.RootNode;

public class BarCommand extends RootNode {
    private static final String PERM_BASE = "luminol.commands.bar";

    public BarCommand() {
        super("bar", PERM_BASE);
        children(
                new BarSubcommand("memory"),
                new BarSubcommand("tps"),
                new BarSubcommand("region")
        );
    }

    public static boolean hasPermission(@NotNull CommandSender sender, String subcommand) {
        return sender.hasPermission(PERM_BASE) || sender.hasPermission(PERM_BASE + "." + subcommand);
    }
}
