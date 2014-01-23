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

/**
 * @author Paul Thompson
 *
 */
public class AfkCommand extends NyvariaCoreCommand implements CommandExecutor {
	public static String CMD = "afk";
	
	public AfkCommand(NyvariaCore plugin) {
		super(plugin);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Make sure we have a Player
		if ( !NyvariaCoreCommand.isPlayer(sender, InvSeeCommand.CMD) ) {
			return true;
		}
		
		// Get the core player that has called this command
		CorePlayer corePlayer = this.plugin.corePlayerList.get(sender);
		
		// Check the command permission
		if (!NyvariaCoreCommand.hasCommandPermission(corePlayer, NyvariaCore.PERM_AFK)) {
	    	return true;
	    }
		
		// Check if the player is vanished
		if (corePlayer.isVanished()) {
			corePlayer.sendMessage(ChatColor.YELLOW + "You cannot use /" + AfkCommand.CMD + " while " + ChatColor.AQUA + "vanished");
			return true;
		}
		
		// Initialise the message
		StringBuilder message = new StringBuilder();
		
		// Toggle afk status
		if (corePlayer.isAfk()) {
			corePlayer.setAfk(false);
			message.append(corePlayer.getPrettyName() + ChatColor.YELLOW + " is back");
		} else {
			corePlayer.setAfk(true);
			message.append(corePlayer.getPrettyName() + ChatColor.YELLOW + " is afk");
		}
		
		// Optionally append the message
		if (args.length > 0) {
			message.append(":" + ChatColor.WHITE);
			for (String arg : args) {
				message.append(" " + arg);
			}
		}
		
		// And finally broadcast it
		this.plugin.getServer().broadcastMessage(message.toString());
		
	    // Return a happy result
		return true;
	}

}
