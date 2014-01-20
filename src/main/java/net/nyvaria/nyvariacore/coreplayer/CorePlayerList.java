/**
 * Copyright (c) 2013-2014 -- Paul Thompson / Nyvaria
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
