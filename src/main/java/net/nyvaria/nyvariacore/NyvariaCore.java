/**
 * Copyright (c) 2013-2014
 * Paul Thompson <captbunzo@gmail.com> / Nyvaria <geeks@nyvaria.net>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 *
 */
package net.nyvaria.nyvariacore;

import net.nyvaria.component.exception.CannotEnablePluginException;
import net.nyvaria.component.hook.MetricsHook;
import net.nyvaria.component.hook.VaultHook;
import net.nyvaria.component.hook.ZPermissionsHook;
import net.nyvaria.component.wrapper.NyvariaPlugin;
import net.nyvaria.nyvariacore.commands.*;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

import java.util.logging.Level;

/**
 * @author Paul Thompson
 */
public class NyvariaCore extends NyvariaPlugin {
    public static String PERM_AFK                   = "nyvcore.afk";
    public static String PERM_FEED                  = "nyvcore.feed";
    public static String PERM_INVSEE                = "nyvcore.invsee";
    public static String PERM_INVSEE_MODIFY         = "nyvcore.invsee.modify";
    public static String PERM_INVSEE_MODIFY_PREVENT = "nyvcore.invsee.modify.prevent";
    public static String PERM_LASTSEEN              = "nyvcore.lastseen";
    public static String PERM_SUDO                  = "nyvcore.sudo";
    public static String PERM_SUDO_PREVENT          = "nyvcore.sudo.prevent";
    public static String PERM_WHO                   = "nyvcore.who";
    public static String PERM_SEE_VANISHED          = "vanish.see";

    // Core player list and listener
    private NyvariaCoreListener listener       = null;
    private CorePlayerList      corePlayerList = null;

    @Override
    public void onEnable() {
        try {
            // Initialise or update the configuration
            saveDefaultConfig();
            getConfig().options().copyDefaults(true);

            // Initialise required hooks
            if (!VaultHook.enable(this)) {
                throw new CannotEnablePluginException("Vault not found");
            }

            if (!ZPermissionsHook.enable(this)) {
                throw new CannotEnablePluginException("ZPermissions not found");
            }

            // Initialise optional hooks
            MetricsHook.enable(this);

            // Create an empty core player list and add all currently logged in players
            corePlayerList = new CorePlayerList();
            for (Player player : getServer().getOnlinePlayers()) {
                corePlayerList.put(player);
            }

            // Create and register the listener
            listener = new NyvariaCoreListener(this);

            // Create the commands
            AfkCommand      cmdAfk      = new AfkCommand(this);
            FeedCommand     cmdFeed     = new FeedCommand(this);
            InvSeeCommand   cmdInvsee   = new InvSeeCommand(this);
            LastSeenCommand cmdLastSeen = new LastSeenCommand(this);
            SudoCommand     cmdSudo     = new SudoCommand(this);
            WhoCommand      cmdWho      = new WhoCommand(this);

            // Set the command executors
            getCommand(AfkCommand.CMD).setExecutor(cmdAfk);
            getCommand(FeedCommand.CMD).setExecutor(cmdFeed);
            getCommand(InvSeeCommand.CMD).setExecutor(cmdInvsee);
            getCommand(LastSeenCommand.CMD).setExecutor(cmdLastSeen);
            getCommand(SudoCommand.CMD).setExecutor(cmdSudo);
            getCommand(WhoCommand.CMD).setExecutor(cmdWho);

            // Set the command tab completers
            getCommand(FeedCommand.CMD).setTabCompleter(cmdFeed);
            getCommand(InvSeeCommand.CMD).setTabCompleter(cmdInvsee);
            getCommand(LastSeenCommand.CMD).setTabCompleter(cmdLastSeen);
            getCommand(SudoCommand.CMD).setTabCompleter(cmdSudo);
            getCommand(WhoCommand.CMD).setTabCompleter(cmdWho);

        } catch (CannotEnablePluginException e) {
            log("Enabling %1$s failed - %2$s", getNameAndVersion(), e.getMessage());
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);

        } finally {
            log("Enabling %1$s successful", getNameAndVersion());
        }
    }

    @Override
    public void onDisable() {
        // Disable the hooks
        MetricsHook.disable();
        ZPermissionsHook.disable();
        VaultHook.disable();

        // Destroy the core player list
        listener       = null;
        corePlayerList = null;

        // Print a lovely message
        log("Disabling %s successful", getNameAndVersion());
    }

    /**
     * Get the instance of the OpenAnalytics plugin from Bukkit.
     *
     * @return OpenAnalytics
     */
    public static NyvariaCore getInstance() {
        return JavaPlugin.getPlugin(NyvariaCore.class);
    }

    /**
     * Getters
     */

    public CorePlayerList getCorePlayerList() {
        return corePlayerList;
    }

}
