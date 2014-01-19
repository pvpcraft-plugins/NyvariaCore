/**
 * 
 */
package net.nyvaria.nyvariacore.coreplayer;

import org.bukkit.entity.Player;

/**
 * @author Paul Thompson
 *
 */
public class CorePlayer {
	public final Player player;
	public boolean invsee;
	
	public CorePlayer(Player player) {
		this.player = player;
		this.invsee = false;
	}
	
	public void sendMessage(String message) {
		this.player.sendMessage(message);
	}
	
	public boolean hasPermission(String permission) {
		return this.player.hasPermission(permission);
	}
}
