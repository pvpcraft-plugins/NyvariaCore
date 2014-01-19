/**
 * 
 */
package net.nyvaria.nyvariacore;

import java.util.List;

import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * @author Paul Thompson
 *
 */
public abstract class NyvariaCoreCommand implements CommandExecutor {
	protected final NyvariaCore plugin;
	
	protected NyvariaCoreCommand(NyvariaCore plugin) {
		this.plugin = plugin;
	}

    protected Player getTargetPlayer(CorePlayer corePlayer, String targetPlayerName) {
    	return this.getTargetPlayer(corePlayer.player, targetPlayerName);
    }
    
    protected Player getTargetPlayer(Player player, String targetPlayerName) {
		List<Player> matchedPlayers = this.plugin.getServer().matchPlayer(targetPlayerName);
		
		if (matchedPlayers.size() > 1) {
			player.sendMessage(ChatColor.WHITE + targetPlayerName + ChatColor.YELLOW + " matches more then one online player");
			return null;
			
		} else if (matchedPlayers.size() == 0) {
			player.sendMessage(ChatColor.WHITE + targetPlayerName + ChatColor.YELLOW + " does not appear to be an online player");
			return null;
		}
    
		return matchedPlayers.get(0);
    }

    
    
    
    protected static boolean isPlayer(CommandSender sender, String cmd) {
    	if ( !(sender instanceof Player) ) {
    		sender.sendMessage("You must be a player to use /" + cmd);
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
        	player.sendMessage("Unknown command. Type \"help\"for help..");
        	return false;
        }
        
        return true;
	}
    
    protected static boolean hasCommandPermission(ConsoleCommandSender console, String permission) {
        if (!console.hasPermission(permission)) {
        	console.sendMessage("Unknown command. Type \"help\"for help.");
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
