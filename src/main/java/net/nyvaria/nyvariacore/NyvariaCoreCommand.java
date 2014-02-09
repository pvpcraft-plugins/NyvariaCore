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

import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul Thompson
 */
public abstract class NyvariaCoreCommand implements CommandExecutor {
    protected final NyvariaCore plugin;

    protected NyvariaCoreCommand(NyvariaCore plugin) {
        this.plugin = plugin;
    }

    protected Player getOnlinePlayer(String partialName) {
        return this.getOnlinePlayer(partialName, (Player) null);
    }

    protected Player getOnlinePlayer(String partialName, CorePlayer corePlayer) {
        return this.getOnlinePlayer(partialName, corePlayer.player);
    }

    protected Player getOnlinePlayer(String partialName, Player player) {
        Player matchedPlayer = null;
        List<Player> matchedPlayers = this.plugin.getServer().matchPlayer(partialName);

        if (matchedPlayers.size() > 1) {
            if (player != null) {
                player.sendMessage(ChatColor.WHITE + partialName + ChatColor.YELLOW + " matches more then one online player");
            }
        } else if (matchedPlayers.size() == 0) {
            if (player != null) {
                player.sendMessage(ChatColor.WHITE + partialName + ChatColor.YELLOW + " does not appear to be an online player");
            }
        } else {
            matchedPlayer = matchedPlayers.get(0);
        }

        return matchedPlayer;
    }

    protected List<OfflinePlayer> getOfflinePlayers(String partialName) {
        List<OfflinePlayer> matchedPlayers = new ArrayList<OfflinePlayer>();

        for (OfflinePlayer offlinePlayer : this.plugin.getServer().getOfflinePlayers()) {
            String offlinePlayerName = offlinePlayer.getName();

            if (partialName.equalsIgnoreCase(offlinePlayerName)) {
                // Exact match
                matchedPlayers.clear();
                matchedPlayers.add(offlinePlayer);
                break;
            }

            if (offlinePlayerName.toLowerCase().contains(partialName.toLowerCase())) {
                // Partial match
                matchedPlayers.add(offlinePlayer);
            }
        }

        return matchedPlayers;
    }

    protected static boolean isPlayer(CommandSender sender, String cmd) {
        return NyvariaCoreCommand.isPlayer(sender, cmd, true);
    }

    protected static boolean isPlayer(CommandSender sender, String cmd, boolean sendAlert) {
        if (!(sender instanceof Player)) {
            if (sendAlert) {
                sender.sendMessage("You must be a player to use /" + cmd);
            }
            return false;
        }

        return true;
    }

    protected static boolean hasCommandPermission(CommandSender sender, String permission) {
        if (sender instanceof Player) {
            return NyvariaCoreCommand.hasCommandPermission((Player) sender, permission);
        } else if (sender instanceof ConsoleCommandSender) {
            return NyvariaCoreCommand.hasCommandPermission((ConsoleCommandSender) sender, permission);
        }

        return false;
    }

    protected static boolean hasCommandPermission(CorePlayer corePlayer, String permission) {
        return NyvariaCoreCommand.hasCommandPermission(corePlayer.player, permission);
    }

    protected static boolean hasCommandPermission(Player player, String permission) {
        if (!player.hasPermission(permission)) {
            player.sendMessage("Unknown command. Type \"help\"for help...");
            return false;
        }

        return true;
    }

    protected static boolean hasCommandPermission(ConsoleCommandSender console, String permission) {
        if (!console.hasPermission(permission)) {
            console.sendMessage("Unknown command. Type \"help\"for help....");
            return false;
        }

        return true;
    }

    protected static String getFinalArg(final String[] args, final int start) {
        final StringBuilder stringBuilder = new StringBuilder();

        for (int i = start; i < args.length; ++i) {
            if (i != start) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(args[i]);
        }

        return stringBuilder.toString();
    }
}
