package me.earthme.luminol.command;

import org.jetbrains.annotations.NotNull;

public abstract class CommandSub extends CommandBase {
    public CommandSub(@NotNull String name, @NotNull String permissionBase) {
        super(name, permissionBase);
        this.isSubCommand = true;
    }
}
