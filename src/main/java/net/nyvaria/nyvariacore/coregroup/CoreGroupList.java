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
package net.nyvaria.nyvariacore.coregroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

/**
 * @author Paul Thompson
 *
 */
public class CoreGroupList {
	private final NyvariaCore plugin;
	private HashMap<String, CoreGroup> map;
	
	public CoreGroupList(NyvariaCore plugin) {
		this.plugin = plugin;
		this.map = new HashMap<String, CoreGroup>();
		
		for (CorePlayer corePlayer : this.plugin.corePlayerList) {
			String groupName = corePlayer.getPrimaryGroup();
			
			if (this.containsKey(groupName)) {
				this.get(groupName).players.put(corePlayer);
			} else {
				CoreGroup coreGroup = new CoreGroup(groupName, this.plugin);
				coreGroup.players.put(corePlayer);
				this.put(coreGroup);
			}
		}
	}
	
	public List<CoreGroup> getSortedList() {
		List<CoreGroup> list = new ArrayList<CoreGroup>(this.map.values());
		Collections.sort(list);
		Collections.reverse(list);
		return list;
	}
	
	public boolean containsKey(CoreGroup group) {
		return this.containsKey(group.name);
	}
	
	public boolean containsKey(String groupName) {
		return this.map.containsKey(groupName);
	}
	
	public void put(CoreGroup group) {
		if (!this.containsKey(group.name)) {
			this.map.put(group.name, group);
		}
	}
	
	public CoreGroup get(String groupName) {
		CoreGroup coreGroup = null;
		
		if (this.containsKey(groupName)) {
			coreGroup = this.map.get(groupName);
		}
		
		return coreGroup;
	}
	
	public void remove(CoreGroup group) {
		this.remove(group.name);
	}
	
	public void remove(String groupName) {
		if (this.map.containsKey(groupName)) {
			this.map.remove(groupName);
		}
	}
	
	public void clear() {
		this.map.clear();
	}
}
