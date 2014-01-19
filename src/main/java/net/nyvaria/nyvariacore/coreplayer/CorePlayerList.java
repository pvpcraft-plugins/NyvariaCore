/**
 * 
 */
package net.nyvaria.nyvariacore.coreplayer;

import java.util.HashMap;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Paul Thompson
 *
 */
public class CorePlayerList {
	private HashMap<Player, CorePlayer> list;
	
	public CorePlayerList() {
		this.list = new HashMap<Player, CorePlayer>();
	}
	
	public void put(Player player) {
		if (!this.list.containsKey(player)) {
			CorePlayer traveler = new CorePlayer(player);
			this.list.put(player, traveler);
		}
	}
	
	public void remove(Player player) {
		if (this.list.containsKey(player)) {
			this.list.remove(player);
		}
	}
	
	public CorePlayer get(Player player) {
		CorePlayer corePlayer = null;
		
		if (this.list.containsKey(player)) {
			corePlayer = this.list.get(player);
		}
		
		return corePlayer;
	}
	
	public CorePlayer get(CommandSender sender) {
		return this.get((Player) sender);
	}
	
	public CorePlayer get(HumanEntity humanEntity) {
		return this.get((Player) humanEntity);
	}
	
	public CorePlayer get(InventoryHolder invHolder) {
		return this.get((Player) invHolder);
	}
	
	public void clear() {
		this.list.clear();
	}
}
