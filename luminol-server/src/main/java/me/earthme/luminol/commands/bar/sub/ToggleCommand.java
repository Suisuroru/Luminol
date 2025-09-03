package me.earthme.luminol.commands.bar.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import me.earthme.luminol.functions.AbstractGlobalServerBar;
import me.earthme.luminol.functions.GlobalServerBarManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.leavesmc.leaves.command.CommandContext;
import org.leavesmc.leaves.command.LiteralNode;

public class ToggleCommand extends LiteralNode {
    private final String bar_name;

    public ToggleCommand(String barName) {
        super("toggle");
        this.bar_name = barName;
    }

    @Override
    protected boolean execute(@NotNull CommandContext context) throws CommandSyntaxException {
        AbstractGlobalServerBar bar;

        try {
            bar = GlobalServerBarManager.get(bar_name);
        } catch (IllegalArgumentException e) {
            context.getSender().sendMessage(Component.text(e.getMessage()).color(TextColor.color(255, 0, 0)));
            return true;
        }

        if (!bar.enabled()) {
            context.getSender().sendMessage(Component.text("Bar type with " + bar_name + " was already disabled!").color(TextColor.color(255, 0, 0)));
        }

        if (!(context.getSender() instanceof Player player)) {
            context.getSender().sendMessage(Component.text("Only player can use this command!").color(TextColor.color(255, 0, 0)));
            return true;
        }

        if (bar.isPlayerVisible(player)) {
            player.sendMessage(Component.text("Disabled Bar type with " + bar_name).color(TextColor.color(0, 255, 0)));
            bar.setVisibilityForPlayer(player, false);
            return true;
        }

        player.sendMessage(Component.text("Enabled Bar type with " + bar_name).color(TextColor.color(0, 255, 0)));
        bar.setVisibilityForPlayer(player, true);
        return true;
    }
}
