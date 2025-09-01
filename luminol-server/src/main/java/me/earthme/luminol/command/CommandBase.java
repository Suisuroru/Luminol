package me.earthme.luminol.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandBase {
    protected final String name;
    protected final String permissionBase;
    protected final List<CommandBase> subCommands = new ArrayList<>();

    protected boolean isSubCommand;
    protected String permission = null;

    public CommandBase(@NotNull String name, @NotNull String permissionBase) {
        this.name = name;
        this.permissionBase = permissionBase;
    }

    protected void addSubCommand(CommandBase commandBase) {
        this.subCommands.add(commandBase);
    }

    protected void addSubCommands(CommandBase... commandBases) {
        for (CommandBase commandBase : commandBases) {
            this.addSubCommand(commandBase);
        }
    }

    protected boolean canExecute(@NotNull CommandSender sender, @NotNull String[] args) {
        return true;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return this.permission == null ? this.permissionBase : this.permission;
    }

    protected boolean isSubCommand() {
        return this.isSubCommand;
    }

    public List<CommandBase> getSubCommands() {
        return this.subCommands;
    }

    public String getName() {
        return this.name;
    }

    public String getPermissionBase() {
        return this.permissionBase;
    }

    private int execute0(CommandContext context) throws CommandSyntaxException {
        return this.execute(context) ? 1 : 0;
    }

    abstract public boolean execute(CommandContext context) throws CommandSyntaxException;

    LiteralArgumentBuilder<CommandSourceStack> compile() {
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = LiteralArgumentBuilder.literal(this.name);

        literalArgumentBuilder.executes(this::execute0);

        for (CommandBase subCommand : this.subCommands) {
            literalArgumentBuilder.then(subCommand.compile());
        }

        return literalArgumentBuilder;
    }

    public void register() {
        MinecraftServer.getServer().getCommands().getDispatcher().register(compile());
        Bukkit.getOnlinePlayers().forEach(org.bukkit.entity.Player::updateCommands);
    }

    public void unregister() {
        CommandDispatcher<CommandSourceStack> dispatcher = MinecraftServer.getServer().getCommands().getDispatcher();
        dispatcher.getRoot().removeCommand(name);
        Bukkit.getOnlinePlayers().forEach(org.bukkit.entity.Player::updateCommands);
    }
}
