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

import java.util.ArrayList;
import java.util.List;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.NyvariaCoreCommand;
import net.nyvaria.nyvariacore.coregroup.CoreGroup;
import net.nyvaria.nyvariacore.coregroup.CoreGroupList;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * @author Paul Thompson
 *
 */
public class WhoCommand extends NyvariaCoreCommand implements CommandExecutor, TabCompleter {
	public static String CMD = "who";
	
	public WhoCommand(NyvariaCore plugin) {
		super(plugin);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	    ArrayList<String> completions = new ArrayList<String>();
	    
		// If we have one argument, the first is a partial player name
	    /*
	    if (args.length == 1) {
		    if ( (sender instanceof Player) && ((Player) sender).hasPermission(NyvariaCore.PERM_INVSEE) ) {
		    	String partialPlayerName = args[0];
		    	
			    for (Player nextMatchingPlayer : plugin.getServer().matchPlayer(partialPlayerName)) {
			    	completions.add(nextMatchingPlayer.getPlayerListName());
			    }
		    }
	    }
	    */
	
		return completions;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Check the command permission (though bukkit should have already stopped senders without permission)
		if (!NyvariaCoreCommand.hasCommandPermission(sender, NyvariaCore.PERM_WHO)) {
	    	return true;
	    }
		
		// Find out if the sender can see vanished players
		boolean canSeeVanished = sender.hasPermission(NyvariaCore.PERM_SEE_VANISHED);
		
		// Initialise the message we will be displaying
		StringBuilder message = new StringBuilder();
		
		// Find the player numbers
		int playerCount = this.plugin.getServer().getOnlinePlayers().length;
		int playerMax   = this.plugin.getServer().getMaxPlayers();
		
		// Get a sorted list of the player groups (with their players)
		List<CoreGroup> groupList = new CoreGroupList(this.plugin).getSortedList();
		
		// Iterate through them adding each group to the message
		for (CoreGroup group : groupList) {
			ArrayList<String> playerNames = new ArrayList<String>();
			StringBuilder groupMessage = new StringBuilder();
			
			for (CorePlayer corePlayer : group.players) {
				boolean playerIsVanished = corePlayer.isVanished();
				boolean playerIsAfk      = corePlayer.isAfk();
				
				if (playerIsVanished && !canSeeVanished) {
					--playerCount;
				} else {
					String playerName = group.prefix + corePlayer.player.getName() + group.suffix;
					
					if (playerIsVanished && playerIsAfk) {
						playerName += ChatColor.AQUA + " /* afk-vanished */";
					} else if (playerIsVanished) {
						playerName += ChatColor.AQUA + " /* vanished */";
					} else if (playerIsAfk) {
						playerName += ChatColor.AQUA + " /* afk */";
					}
					
					playerNames.add(playerName);
				}
			}
			
			int groupPlayerNameCount = playerNames.size();
			int groupPlayerNameNum   = 0;
			
			if (playerNames.size() > 0) {
				for (String playerName : playerNames) {
					++groupPlayerNameNum;
	
					groupMessage.append(playerName);
					if ( groupPlayerNameNum < groupPlayerNameCount ) {
						groupMessage.append(ChatColor.GRAY + ", ");
					}
				}
				
				message.append(String.format(ChatColor.GRAY + "" + ChatColor.ITALIC + "\n%s: %s" + group.suffix, group.displayName, groupMessage.toString()));
			}
		}
		
		// Put together the player count string
		message.insert(0, String.format(ChatColor.YELLOW + "There are %d/%d players online:" + ChatColor.WHITE, playerCount, playerMax));
		
		// And send it to the sender
		sender.sendMessage(message.toString());
		
	    // Return a happy result
		return true;
	}

}
