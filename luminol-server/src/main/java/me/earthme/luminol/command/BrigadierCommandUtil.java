package me.earthme.luminol.command;

import org.bukkit.Bukkit;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

import java.util.ArrayList;
import java.util.List;

public class BrigadierCommandUtil {

    /**
     * Register permission names and build complete command path hierarchy
     *
     * @param baseName    Base name path
     * @param commandBase Current command node
     * @return List of all permission names
     */
    private List<String> registerPermission0(String baseName, CommandBase commandBase) {
        List<String> ret = new ArrayList<>();
        String currentPath = baseName.isEmpty() ? commandBase.getName() : baseName + "." + commandBase.getPermissionBase();
        ret.add(currentPath);
        commandBase.setPermission(currentPath);
        for (CommandBase subCommand : commandBase.getSubCommands()) {
            ret.addAll(registerPermission0(currentPath, subCommand));
        }

        return ret;
    }

    public void registerPermission(CommandBase commandBase) {
        registerPermission(commandBase, PermissionDefault.OP);
    }

    public void registerPermission(CommandBase commandBase, PermissionDefault defaultPermission) {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        for (String permission : registerPermission0("", commandBase)) {
            if (pluginManager.getPermission(permission) == null) {
                pluginManager.addPermission(new Permission(permission, defaultPermission));
            }
        }
    }
}
