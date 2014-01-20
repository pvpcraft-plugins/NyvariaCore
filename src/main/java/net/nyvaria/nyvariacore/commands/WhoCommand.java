/**
 * Copyright (c) 2013-2014 -- Paul Thompson / Nyvaria
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
		
		// Initialise the message we will be displaying
		StringBuilder message = new StringBuilder();
		
		// Find the player numbers
		int playerCount = this.plugin.getServer().getOnlinePlayers().length;
		int playerMax   = this.plugin.getServer().getMaxPlayers();
		
		// Put together the player count string
		message.append(String.format(ChatColor.YELLOW + "There are %d/%d players online:" + ChatColor.WHITE, playerCount, playerMax));
		
		// Get a sorted list of the player groups (with their players)
		List<CoreGroup> groupList = new CoreGroupList(this.plugin).getSortedList();
		
		// Iterate through them adding each group to the message
		for (CoreGroup group : groupList) {
			StringBuilder groupMessage = new StringBuilder();
			
			int groupPlayerCount = group.players.length();
			int groupPlayerNum   = 0;
			
			for (CorePlayer corePlayer : group.players) {
				groupMessage.append(group.prefix + corePlayer.player.getName() + group.suffix);
				
				if ( ++groupPlayerNum < groupPlayerCount ) {
					groupMessage.append(ChatColor.GRAY + ", ");
				}
			}
			
			message.append(String.format(ChatColor.GRAY + "" + ChatColor.ITALIC + "\n%s: %s" + group.suffix, group.displayName, groupMessage.toString()));
		}
		
		// And send it to the sender
		sender.sendMessage(message.toString());
		
	    // Return a happy result
		return true;
	}

}
