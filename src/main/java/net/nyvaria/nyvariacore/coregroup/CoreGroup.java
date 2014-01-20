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
package net.nyvaria.nyvariacore.coregroup;

import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;
import net.nyvaria.utils.StringUtils;

/**
 * @author Paul Thompson
 *
 */
public class CoreGroup implements Comparable<CoreGroup> {
	private final NyvariaCore plugin;
	
	public String   name;
	public String   displayName;
	public Integer  priority;
	public String   prefix;
	public String   suffix;
	
	public CorePlayerList players;
	
	public CoreGroup(String name, NyvariaCore plugin) {
		this.plugin = plugin;
		
		this.name        = name;
		this.displayName = StringUtils.splitCamelCase(name);
		this.priority    = CoreGroup.getGroupPriority(name);
		this.prefix      = ChatColor.translateAlternateColorCodes('&', CoreGroup.getGroupPrefix(name));
		this.suffix      = ChatColor.translateAlternateColorCodes('&', CoreGroup.getGroupSuffix(name));
		this.players     = new CorePlayerList();
		
		this.plugin.log(Level.FINER, "Group name        = " + this.name);
		this.plugin.log(Level.FINER, "Group displayName = " + this.displayName);
		this.plugin.log(Level.FINER, "Group priority    = " + this.priority);
		this.plugin.log(Level.FINER, "Group prefix      = " + this.prefix);
		this.plugin.log(Level.FINER, "Group suffix      = " + this.suffix);
	}

	public int compareTo(CoreGroup other) {
		return this.priority.compareTo(other.priority);
	}
	
	private static final HashMap<String, Integer> GROUP_PRIORITIES = new HashMap<String, Integer>();
	static {
		GROUP_PRIORITIES.put("ServerOwners",    900);
		GROUP_PRIORITIES.put("SeniorAdmins",    830);
		GROUP_PRIORITIES.put("AssociateAdmins", 820);
		GROUP_PRIORITIES.put("JuniorAdmins",    810);
		GROUP_PRIORITIES.put("SeniorMods",      710);
		GROUP_PRIORITIES.put("JuniorMods",      610);
		GROUP_PRIORITIES.put("Nyvarians",       300);
		GROUP_PRIORITIES.put("Players",         300);
		GROUP_PRIORITIES.put("Newbies",         110);
	}
	
	private static Integer getGroupPriority(String name) {
		Integer priority = null;
		
		//priority = NyvariaCore.zperms.getGroupMetadata(name, "priority", Integer.class);
		
		if (CoreGroup.GROUP_PRIORITIES.containsKey(name)) {
			priority = CoreGroup.GROUP_PRIORITIES.get(name);
		} else {
			priority = 1;
		}
		
		return priority;
	}
	
	private static String getGroupPrefix(String name) {
		String prefix = null;
		
		try {
			prefix = NyvariaCore.zperms.getGroupMetadata(name, "prefix", String.class);
		} catch (Exception e) {
		}
		
		if (prefix == null) {
			prefix = "";
		}
		
		return prefix;
	}

	private static String getGroupSuffix(String name) {
		String suffix = null;
		
		try {
			suffix = NyvariaCore.zperms.getGroupMetadata(name, "suffix", String.class);
		} catch (Exception e) {
		}
		
		if (suffix == null) {
			suffix = "";
		}
		
		return suffix;
	}

}
