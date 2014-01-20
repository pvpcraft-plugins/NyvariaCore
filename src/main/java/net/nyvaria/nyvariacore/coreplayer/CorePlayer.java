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
package net.nyvaria.nyvariacore.coreplayer;

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coregroup.CoreGroup;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

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
		this.setAfk(false);
	}
	
	public void sendMessage(String message) {
		this.player.sendMessage(message);
	}
	
	public boolean hasPermission(String permission) {
		return this.player.hasPermission(permission);
	}

	public String getPrettyName() {
		String group = this.getPrimaryGroup();
		return CoreGroup.getGroupPrefix(group) + this.player.getName() + CoreGroup.getGroupSuffix(group);
	}
	
	public String getPrimaryGroup() {
		if (NyvariaCore.zperms != null) {
			return NyvariaCore.zperms.getPlayerPrimaryGroup(this.player.getName());
		} else {
			return "Players";
		}
	}
	
	public void setAfk(boolean afk) {
        player.setMetadata("afk", new FixedMetadataValue(NyvariaCore.instance, afk));
	}
	
	public boolean isAfk() {
		if (player.hasMetadata("afk")) {
			return player.getMetadata("afk").get(0).asBoolean();
		}
		
		return false;
	}
	
	public boolean isVanished() {
		if (player.hasMetadata("vanished")) {
			return player.getMetadata("vanished").get(0).asBoolean();
		}
		
		return false;
	}
}
