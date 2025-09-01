package me.earthme.luminol.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class CommandMain extends CommandBase {
    public CommandMain(@NotNull String name, @NotNull String permissionBase) {
        super(name, permissionBase);
        this.isSubCommand = false;
    }

    @Override
    protected boolean canExecute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender.hasPermission(this.getPermission())) {
            return true;
        }

        String currentPermission = this.getPermission();
        for (String arg : args) {
            currentPermission += "." + arg;
            if (sender.hasPermission(currentPermission)) {
                return true;
            }
        }

        return false;
    }
}
