/**
 * 
 */
package net.nyvaria.nyvariacore;

import java.util.logging.Level;

import net.nyvaria.nyvariacore.commands.InvSeeCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Paul Thompson
 *
 */
public class NyvariaCore extends JavaPlugin {
	public static String PERM_INVSEE                = "nyvcore.invsee";
	public static String PERM_INVSEE_MODIFY         = "nyvcore.invsee.modify";
	public static String PERM_INVSEE_MODIFY_PREVENT = "nyvcore.invsee.modify.prevent";
	
	// Core player list and listener
	public  CorePlayerList      corePlayerList = null;
	private NyvariaCoreListener listener       = null;
	
	// Commands
	private InvSeeCommand       invseeCommand  = null;

	@Override
	public void onEnable() {
		this.getLogger().setLevel(Level.INFO);
		
		// Create an empty core player list
		this.corePlayerList = new CorePlayerList();
		
		// Create and register the listener
		this.listener = new NyvariaCoreListener(this);
		this.getServer().getPluginManager().registerEvents(listener, this);
		
		// Add all currently logged in players to the core player list
		for (Player player : this.getServer().getOnlinePlayers()) {
			this.corePlayerList.put(player);
		}

		// Create and set the commands
		this.invseeCommand    = new InvSeeCommand(this);
		this.getCommand(InvSeeCommand.CMD).setExecutor(this.invseeCommand);
		
		this.log("Enabling " + this.getNameVersion() + " successful");
	}
	
	@Override
	public void onDisable() {
		// Empty and destroy the flier list
		this.corePlayerList.clear();
		this.corePlayerList = null;
		
		this.log("Disabling " + this.getNameVersion() + " successful");
	}
	
	public void reload() {
		this.log("Reloading " + this.getNameVersion());
		this.onDisable();
		this.onEnable();
		this.log("Reloading " + this.getNameVersion() + " successful");
	}

	private String getNameVersion() {
		return this.getName() + " " + this.getVersion();
	}
	
	private String getVersion() {
		return "v" + this.getDescription().getVersion();
	}
	
	public void log(String msg) {
		this.log(Level.INFO, msg);
	}
	
	public void log(Level level, String msg) {
		this.getLogger().log(level, msg);
	}

}
