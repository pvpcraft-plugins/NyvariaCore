/**
 * 
 */
package net.nyvaria.nyvariacore;

import java.util.logging.Level;

import net.nyvaria.nyvariacore.commands.InvSeeCommand;
import net.nyvaria.nyvariacore.commands.SudoCommand;
import net.nyvaria.nyvariacore.commands.WhoCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.tyrannyofheaven.bukkit.zPermissions.ZPermissionsService;

/**
 * @author Paul Thompson
 *
 */
public class NyvariaCore extends JavaPlugin {
	public static String PERM_INVSEE                = "nyvcore.invsee";
	public static String PERM_INVSEE_MODIFY         = "nyvcore.invsee.modify";
	public static String PERM_INVSEE_MODIFY_PREVENT = "nyvcore.invsee.modify.prevent";
	public static String PERM_SUDO                  = "nyvcore.sudo";
	public static String PERM_SUDO_PREVENT          = "nyvcore.sudo.prevent";
	public static String PERM_WHO                   = "nyvcore.who";
	
	// Core player list and listener
	public  CorePlayerList      corePlayerList = null;
	private NyvariaCoreListener listener       = null;
	
	// zPermissions API
	public static ZPermissionsService zperms = null;
	
	// Commands
	private InvSeeCommand       cmdInvsee = null;
	private SudoCommand         cmdSudo   = null;
	private WhoCommand          cmdWho    = null;

	@Override
	public void onEnable() {
		this.getLogger().setLevel(Level.INFO);
		
		// Create an empty core player list
		this.corePlayerList = new CorePlayerList();
		
		// Create and register the listener
		this.listener = new NyvariaCoreListener(this);
		this.getServer().getPluginManager().registerEvents(listener, this);
		
		// Load the zPermissions API
		loadZPermissionsService();
		
		// Add all currently logged in players to the core player list
		for (Player player : this.getServer().getOnlinePlayers()) {
			this.corePlayerList.put(player);
		}

		// Create and set the commands
		this.cmdInvsee = new InvSeeCommand(this);
		this.cmdSudo   = new SudoCommand(this);
		this.cmdWho    = new WhoCommand(this);
		
		this.getCommand(InvSeeCommand.CMD).setExecutor(this.cmdInvsee);
		this.getCommand(SudoCommand.CMD).setExecutor(this.cmdSudo);
		this.getCommand(WhoCommand.CMD).setExecutor(this.cmdWho);

		this.log("Enabling " + this.getNameVersion() + " successful");
	}
	
	@Override
	public void onDisable() {
		// Empty and destroy the flier list
		this.corePlayerList.clear();
		this.corePlayerList = null;
		
		// Unload zperms
		NyvariaCore.zperms = null;
		
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
	
	// Private methods
	private boolean loadZPermissionsService() {
		NyvariaCore.zperms = null;
		
		try {
			NyvariaCore.zperms = Bukkit.getServicesManager().load(ZPermissionsService.class);
		} catch (NoClassDefFoundError e) {
			this.log(Level.WARNING, "ZPermissionsService class not found - zPerms support disabled!");
			NyvariaCore.zperms = null;
		} finally {
			if (NyvariaCore.zperms == null) {
				this.log(Level.WARNING, "ZPermissionsService instance unexepectedly null after loading - zPerms support disabled!");
			}
		}
				
		return (NyvariaCore.zperms != null);
	}
}
