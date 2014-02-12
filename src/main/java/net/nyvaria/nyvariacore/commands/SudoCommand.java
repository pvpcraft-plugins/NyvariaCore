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
package net.nyvaria.nyvariacore.commands;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.NyvariaCoreCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * @author Paul Thompson
 */
public class SudoCommand extends NyvariaCoreCommand implements CommandExecutor, TabCompleter {
    public static String CMD = "sudo";

    public SudoCommand(NyvariaCore plugin) {
        super(plugin);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<String>();

        // If we have one argument, the first is a partial player name
        if (args.length == 1) {
            if ((sender instanceof Player) && sender.hasPermission(NyvariaCore.PERM_SUDO)) {
                String partialPlayerName = args[0];

                for (Player nextMatchingPlayer : plugin.getServer().matchPlayer(partialPlayerName)) {
                    completions.add(nextMatchingPlayer.getPlayerListName());
                }
            }

            // If we have two arguments, the second is a partial command

        //} else if (args.length == 2) {
        //    It would be nice if we could match on command names
        }

        return completions;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Make sure we have a Player
        if (!NyvariaCoreCommand.isPlayer(sender, SudoCommand.CMD)) {
            return true;
        }

        // Get the core player that has called this command
        final CorePlayer corePlayer = this.plugin.getCorePlayerList().get(sender);

        // Check the command permission (though bukkit should have already stopped players without permission)
        if (!NyvariaCoreCommand.hasCommandPermission(corePlayer, NyvariaCore.PERM_SUDO)) {
            return true;
        }

        // Check if we have enough arguments
        if (args.length < 2) {
            return false;
        }

        // Get the target player whose inventory we are peeking at
        String otherPlayerName = args[0];
        final Player otherPlayer = this.getOnlinePlayer(otherPlayerName, corePlayer);
        if (otherPlayer == null) {
            return true;
        }

        // Get the other core player
        CorePlayer otherCorePlayer = this.plugin.getCorePlayerList().get(otherPlayer);

        // See if the other player has sudo prevent
        if (otherPlayer.hasPermission(NyvariaCore.PERM_SUDO_PREVENT)) {
            corePlayer.sendMessage(ChatColor.YELLOW + "Sorry, but you cannot /" + SudoCommand.CMD + " as " + otherCorePlayer.getWrappedName());
            return true;
        }

        // Collect the command to be run
        final String sudoCmd = (args[1].startsWith("/") ? args[1].substring(1) : args[1]);

        // Collect the arguments to be provided to that command
        String[] sudoCmdArgs = new String[args.length - 2];
        if (sudoCmdArgs.length > 0) {
            System.arraycopy(args, 2, sudoCmdArgs, 0, args.length - 2);
        }

        // Combine the arguments and command
        final String sudoCmdAndArgs = sudoCmd + " " + getFinalArg(sudoCmdArgs, 0);

        // Inform the sender of what we are doing
        sender.sendMessage(ChatColor.YELLOW + "Forcing " + otherCorePlayer.getWrappedName() + ChatColor.YELLOW + " to run: " + ChatColor.WHITE + "/" + sudoCmdAndArgs);

        // Run the command
        this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
            public void run() {
                plugin.log(Level.INFO, String.format("[Sudo] %s issued server command: /%s", otherPlayer.getName(), sudoCmdAndArgs));
                if (!otherPlayer.performCommand(sudoCmdAndArgs) && corePlayer.getPlayer().isOnline()) {
                    corePlayer.sendMessage(ChatColor.YELLOW + "Error calling command: " + ChatColor.WHITE + "/" + sudoCmd);
                }
            }
        });

        return true;
    }

}
