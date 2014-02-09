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

import net.nyvaria.component.util.StringUtils;
import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.NyvariaCoreCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Thompson
 */
public class LastSeenCommand extends NyvariaCoreCommand implements CommandExecutor, TabCompleter {
    public static String CMD = "lastseen";

    public LastSeenCommand(NyvariaCore plugin) {
        super(plugin);
    }

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> completions = new ArrayList<String>();

        // If we have one argument, the first is a partial player name
        if (args.length == 1) {
            if (sender.hasPermission(NyvariaCore.PERM_LASTSEEN)) {
                String partialPlayerName = args[0];

                for (OfflinePlayer nextMatchingPlayer : this.getOfflinePlayers(partialPlayerName)) {
                    completions.add(nextMatchingPlayer.getName());
                }
            }
        }

        return completions;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If we are a player, check the command permission
        if ((sender instanceof Player) && !NyvariaCoreCommand.hasCommandPermission(sender, NyvariaCore.PERM_LASTSEEN)) {
            return true;
        }

        // Check if we have enough arguments
        if (args.length != 1) {
            return false;
        }

        // First check if this player is online
        String lastSeenPlayerName = args[0];
        Player lastSeenPlayer = this.getOnlinePlayer(lastSeenPlayerName);

        if (lastSeenPlayer != null) {
            CorePlayer onlineCorePlayer = this.plugin.getCorePlayerList().get(lastSeenPlayer);
            sender.sendMessage(onlineCorePlayer.getPrettyName() + ChatColor.YELLOW + " can be seen online right now!");
            return true;
        }

        List<OfflinePlayer> offlinePlayerList = this.getOfflinePlayers(lastSeenPlayerName);

        if (offlinePlayerList.size() == 0) {
            sender.sendMessage(ChatColor.GRAY + lastSeenPlayerName + ChatColor.YELLOW + " has never been seen on this server");
            return true;
        }

        if (offlinePlayerList.size() == 1) {
            CorePlayer offlineCorePlayer = new CorePlayer(offlinePlayerList.get(0));
            String lastSeenDateString = StringUtils.getFormattedDate(offlineCorePlayer.offlinePlayer.getLastPlayed(), "on");

            sender.sendMessage(offlineCorePlayer.getPrettyName() + ChatColor.YELLOW + " was last seen " + lastSeenDateString);
            return true;
        }

        if (offlinePlayerList.size() < 10) {
            List<String> playerNameList = new ArrayList<String>();

            for (OfflinePlayer offlinePlayer : offlinePlayerList) {
                playerNameList.add(new CorePlayer(offlinePlayer).getPrettyName());
            }

            sender.sendMessage(ChatColor.GRAY + lastSeenPlayerName + ChatColor.YELLOW + " matched " + offlinePlayerList.size() + " players: " + StringUtils.join(playerNameList, ChatColor.WHITE + ", "));
            return true;
        }

        sender.sendMessage(ChatColor.GRAY + lastSeenPlayerName + ChatColor.YELLOW + " matched " + offlinePlayerList.size() + " players -- please be more specific");
        return true;
    }

}
