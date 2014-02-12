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

import net.nyvaria.component.wrapper.NyvariaGroup;
import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import net.nyvaria.nyvariacore.coreplayer.CorePlayerList;
import org.apache.commons.lang.Validate;

import java.util.HashMap;
import java.util.logging.Level;

/**
 * @author Paul Thompson
 */
public class CoreGroup extends NyvariaGroup implements Comparable<CoreGroup> {
    public static final String DEFAULT_GROUP_NAME = "Players";

    public Integer priority;
    public CorePlayerList players;

    public CoreGroup(String name) {
        super(name);
        this.priority = CoreGroup.getGroupPriority(name);
        this.players = new CorePlayerList();

        NyvariaCore.getInstance().log(Level.FINER, "Group name     = " + this.getName());
        NyvariaCore.getInstance().log(Level.FINER, "Group label    = " + this.getLabel());
        NyvariaCore.getInstance().log(Level.FINER, "Group priority = " + this.priority);
        NyvariaCore.getInstance().log(Level.FINER, "Group prefix   = " + this.getPrefix());
        NyvariaCore.getInstance().log(Level.FINER, "Group suffix   = " + this.getSuffix());
    }

    public int compareTo(CoreGroup other) {
        Validate.notNull(other);
        return other.priority.compareTo(this.priority);
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
        Integer priority;

        //priority = NyvariaCore.zperms.getGroupMetadata(name, "priority", Integer.class);

        if (CoreGroup.GROUP_PRIORITIES.containsKey(name)) {
            priority = CoreGroup.GROUP_PRIORITIES.get(name);
        } else {
            priority = 1;
        }

        return priority;
    }

    public static CoreGroup getPrimaryGroup(CorePlayer corePlayer) {
        return CoreGroup.getPrimaryGroup(corePlayer, CoreGroup.DEFAULT_GROUP_NAME);
    }

    public static CoreGroup getPrimaryGroup(CorePlayer corePlayer, String defaultPrimaryGroup) {
        String groupName = NyvariaGroup.getPrimaryGroupName(corePlayer.getOfflinePlayer(), defaultPrimaryGroup);
        return groupName == null ? null : new CoreGroup(groupName);
    }
}
