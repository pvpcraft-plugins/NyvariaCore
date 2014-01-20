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

import java.util.logging.Level;

import net.nyvaria.nyvariacore.commands.InvSeeCommand;
import net.nyvaria.nyvariacore.commands.SudoCommand;
import net.nyvaria.nyvariacore.commands.WhoCommand;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;
import net.nyvaria.nyvariacore.metrics.MetricsHandler;

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
	public static String PERM_SEE_VANISHED          = "vanish.see";
	
	// Core player list and listener and metrics
	public  CorePlayerList      corePlayerList = null;
	private NyvariaCoreListener listener       = null;
	private MetricsHandler      metrics        = null;
	
	// zPermissions API
	public static ZPermissionsService zperms = null;
	
	// Commands
	private InvSeeCommand       cmdInvsee = null;
	private SudoCommand         cmdSudo   = null;
	private WhoCommand          cmdWho    = null;

	@Override
	public void onEnable() {
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

		// Initialise or update the configuration
		this.saveDefaultConfig();
		this.getConfig().options().copyDefaults(true);
		
		// Initialise metrics
		boolean useMetrics = this.getConfig().getBoolean("use-metrics");
		if (useMetrics) {
            this.metrics = new MetricsHandler(this);
            metrics.run();
		} else {
            this.log("Skipping metrics");
		}
		
		// Create and set the commands
		this.cmdInvsee = new InvSeeCommand(this);
		this.cmdSudo   = new SudoCommand(this);
		this.cmdWho    = new WhoCommand(this);
		
		this.getCommand(InvSeeCommand.CMD).setExecutor(this.cmdInvsee);
		this.getCommand(InvSeeCommand.CMD).setTabCompleter(this.cmdInvsee);
		this.getCommand(SudoCommand.CMD).setExecutor(this.cmdSudo);
		this.getCommand(SudoCommand.CMD).setTabCompleter(this.cmdSudo);
		this.getCommand(WhoCommand.CMD).setExecutor(this.cmdWho);
		this.getCommand(WhoCommand.CMD).setTabCompleter(this.cmdWho);

		// Print a lovely message
		this.log("Enabling " + this.getNameVersion() + " successful");
	}
	
	@Override
	public void onDisable() {
		// Empty and destroy the flier list
		this.corePlayerList.clear();
		this.corePlayerList = null;
		
		// Unload zPermissions
		NyvariaCore.zperms = null;
		
		// Destroy the metrics handler
		this.metrics = null;
		
		// Print a lovely message
		this.log("Disabling " + this.getNameVersion() + " successful");
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
	
	public void log(String msg) {
		this.log(Level.INFO, msg);
	}
	
	public void log(Level level, String msg) {
		this.getLogger().log(level, msg);
	}
	
	private String getNameVersion() {
		return this.getName() + " v" + this.getDescription().getVersion();
	}
}
