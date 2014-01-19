/**
 * 
 */
package net.nyvaria.nyvariacore.commands;

import java.util.ArrayList;
import java.util.List;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * @author Paul Thompson
 *
 */
public class InvSeeCommand implements CommandExecutor {
	public static String CMD = "invsee";
	private final NyvariaCore plugin;
	
	public InvSeeCommand(NyvariaCore plugin) {
		this.plugin = plugin;
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	    String targetedPlayerName = args[0];
	    ArrayList<String> matchedPlayerNames = new ArrayList<String>();
	    
	    if (sender instanceof Player) {
	    	Player player = (Player) sender;
	    	
	    	if (player.hasPermission(NyvariaCore.PERM_INVSEE)) {
			    for (Player nextPlayer : plugin.getServer().matchPlayer(targetedPlayerName)) {
			    	matchedPlayerNames.add(nextPlayer.getPlayerListName());
			    }
	    	}
	    }
	
		return matchedPlayerNames;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Make sure we have a Player
		if ( !(sender instanceof Player) ) {
			sender.sendMessage("You must be a player to use /" + InvSeeCommand.CMD);
			return true;
		}
		
		CorePlayer corePlayer = this.plugin.corePlayerList.get(sender);
		
	    if (!corePlayer.hasPermission(NyvariaCore.PERM_INVSEE)) {
	    	corePlayer.sendMessage("Unknown command. Type \"help\"for help.");
	    	return true;
	    }
		
	    if (args.length != 1) {
	    	return false;
	    }
	    
	    String invPlayerName = args[0];
	    List<Player> matchedPlayers = this.plugin.getServer().matchPlayer(invPlayerName);
	    
	    if (matchedPlayers.size() > 1) {
	    	corePlayer.sendMessage(ChatColor.WHITE + invPlayerName + ChatColor.YELLOW + " matches more then one online player");
	    	return true;
	    	
	    } else if (matchedPlayers.size() == 0) {
	    	corePlayer.sendMessage(ChatColor.WHITE + invPlayerName + ChatColor.YELLOW + " does not appear to be an online player");
	    	return true;
	    }
	    
	    Player invPlayer = matchedPlayers.get(0);
	    
	    Inventory inv = invPlayer.getInventory();
	    corePlayer.player.closeInventory();
	    corePlayer.player.openInventory(inv);
	    corePlayer.invsee = true;
		
		return true;
	}

}
