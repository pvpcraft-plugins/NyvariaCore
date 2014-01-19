/**
 * 
 */
package net.nyvaria.nyvariacore.commands;

import java.util.ArrayList;
import java.util.List;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.NyvariaCoreCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Paul Thompson
 *
 */
public class InvSeeCommand extends NyvariaCoreCommand implements CommandExecutor, TabCompleter {
	public static String CMD = "invsee";
	
	public InvSeeCommand(NyvariaCore plugin) {
		super(plugin);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	    ArrayList<String> completions = new ArrayList<String>();
	    
		// If we have one argument, the first is a partial player name
	    if (args.length == 1) {
		    if ( (sender instanceof Player) && ((Player) sender).hasPermission(NyvariaCore.PERM_INVSEE) ) {
		    	String partialPlayerName = args[0];
		    	
			    for (Player nextMatchingPlayer : plugin.getServer().matchPlayer(partialPlayerName)) {
			    	completions.add(nextMatchingPlayer.getPlayerListName());
			    }
		    }
	    }
	
		return completions;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Make sure we have a Player
		if ( !NyvariaCoreCommand.isPlayer(sender, InvSeeCommand.CMD) ) {
			return true;
		}
		
		// Get the core player that has called this command
		CorePlayer corePlayer = this.plugin.corePlayerList.get(sender);
		
		// Check the command permission (though bukkit should have already stopped players without permission)
		if (!NyvariaCoreCommand.hasCommandPermission(corePlayer, NyvariaCore.PERM_INVSEE)) {
	    	return true;
	    }
		
		// Check if we have enough arguments
	    if (args.length != 1) {
	    	return false;
	    }
	    
		// Get the target player whose inventory we are peeking at
	    String invPlayerName = args[0];
	    Player invPlayer = this.getTargetPlayer(corePlayer, invPlayerName);
	    if (invPlayer == null) {
	    	return true;
	    }
	    
		// Grab the inventory and open it up
	    Inventory inv = invPlayer.getInventory();
	    corePlayer.player.closeInventory();
	    corePlayer.player.openInventory(inv);
	    corePlayer.invsee = true;
		
	    // Return a happy result
		return true;
	}

}
