/**
 * 
 */
package net.nyvaria.nyvariacore.coreplayer;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Paul Thompson
 *
 */
public class CorePlayerList implements Iterable<CorePlayer> {
	private HashMap<Player, CorePlayer> map;
	
	public CorePlayerList() {
		this.map = new HashMap<Player, CorePlayer>();
	}
	
	public void put(Player player) {
		if (!this.map.containsKey(player)) {
			CorePlayer corePlayer = new CorePlayer(player);
			this.map.put(player, corePlayer);
		}
	}
	
	public void put(CorePlayer corePlayer) {
		if (!this.map.containsKey(corePlayer.player)) {
			this.map.put(corePlayer.player, corePlayer);
		}
	}
	
	public void remove(Player player) {
		if (this.map.containsKey(player)) {
			this.map.remove(player);
		}
	}
	
	public CorePlayer get(Player player) {
		CorePlayer corePlayer = null;
		
		if (this.map.containsKey(player)) {
			corePlayer = this.map.get(player);
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
		this.map.clear();
	}
	
	public Iterator<CorePlayer> iterator() {
		return this.map.values().iterator();
	}
	
	public int length() {
		return this.map.size();
	}
}
