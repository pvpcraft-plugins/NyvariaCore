/**
 * 
 */
package net.nyvaria.nyvariacore.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.NyvariaCoreCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

/**
 * @author Paul Thompson
 *
 */
public class SudoCommand extends NyvariaCoreCommand implements CommandExecutor, TabCompleter {
	public static String CMD = "sudo";
	
	public SudoCommand(NyvariaCore plugin) {
		super(plugin);
	}
	
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
	    ArrayList<String> completions = new ArrayList<String>();
	    
		// If we have one argument, the first is a partial player name
	    if (args.length == 1) {
		    if ( (sender instanceof Player) && ((Player) sender).hasPermission(NyvariaCore.PERM_SUDO) ) {
		    	String partialPlayerName = args[0];
		    	
			    for (Player nextMatchingPlayer : plugin.getServer().matchPlayer(partialPlayerName)) {
			    	completions.add(nextMatchingPlayer.getPlayerListName());
			    }
		    }
		
		// If we have two arguments, the second is a partial command
	    } else if (args.length == 2)  {
	    	// It would be nice to add command completion here
	    }
	    
		return completions;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Make sure we have a Player
		if ( !NyvariaCoreCommand.isPlayer(sender, SudoCommand.CMD) ) {
			return true;
		}
		
		// Get the core player that has called this command
		final CorePlayer corePlayer = this.plugin.corePlayerList.get(sender);
		
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
	    final Player otherPlayer = this.getTargetPlayer(corePlayer, otherPlayerName);
	    if (otherPlayer == null) {
	    	return true;
	    }
	    
	    // See if the other player has sudo prevent
	    if (otherPlayer.hasPermission(NyvariaCore.PERM_SUDO_PREVENT)) {
	    	corePlayer.sendMessage(ChatColor.YELLOW + "Sorry, but you cannot /" + SudoCommand.CMD + " as " + ChatColor.WHITE + otherPlayerName);
	    	return true;
	    }
	    
	    // Collect the command to be run
        final String sudoCmd = ( args[1].startsWith("/") ? args[1].substring(1) : args[1] );
        
        // Collect the arguments to be provided to that command
        String[] sudoCmdArgs = new String[args.length - 2];
        if (sudoCmdArgs.length > 0) {
        	System.arraycopy(args, 2, sudoCmdArgs, 0, args.length - 2);
        }
        
        // Combine the arguments and command
        final String sudoCmdAndArgs = sudoCmd + " " + getFinalArg(sudoCmdArgs, 0);
        
        // Inform the sender of what we are doing
        sender.sendMessage(ChatColor.YELLOW + "Forcing " + ChatColor.WHITE + otherPlayer.getName() + ChatColor.YELLOW + " to run:" + ChatColor.WHITE + "/" + sudoCmdAndArgs);
        
        // Run the command
    	this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
    		public void run() {
    			plugin.log(Level.INFO, String.format("[Sudo] %s issued server command: /%s", otherPlayer.getName(), sudoCmdAndArgs));
    			if (!otherPlayer.performCommand(sudoCmdAndArgs) && corePlayer.player.isOnline()) {
    				corePlayer.sendMessage(ChatColor.YELLOW + "Error calling command: " + ChatColor.WHITE + "/" + sudoCmd);
    			}
    		}
    	});
        
        /*
        final PluginCommand sudoPluginCmd = this.plugin.getServer().getPluginCommand(sudoCmd);
        if (sudoPluginCmd != null) {
        	this.plugin.getServer().getScheduler().runTask(this.plugin, new Runnable() {
        		public void run() {
        			plugin.log(Level.INFO, String.format("[Sudo] %s issued server command: /%s %s", otherPlayer.getName(), sudoCmd, getFinalArg(sudoCmdArgs, 0)));
        			sudoPluginCmd.execute(otherPlayer, sudoCmd, sudoCmdArgs);
        		}
        	});
        } else {
        	sender.sendMessage(ChatColor.YELLOW + "Error calling command: " + ChatColor.WHITE + sudoCmd);
        }
        */
		
		return true;
	}

}
