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

import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author Paul Thompson
 */
public class CoreGroupLabel implements Comparable<CoreGroupLabel> {
    public String name;
    public Integer order;
    public List<String> groupList;
    public HashMap<String, CoreGroup> groupMap;

    public CoreGroupLabel(String name, Integer order, List<String> groupList) {
        this.name = name;
        this.order = order;
        this.groupList = groupList;
        this.groupMap = new HashMap<String, CoreGroup>();
    }

    public void add(CorePlayer corePlayer, String groupName) {
        if (this.groupMap.containsKey(groupName)) {
            this.groupMap.get(groupName).players.put(corePlayer);
        } else {
            CoreGroup coreGroup = new CoreGroup(groupName);
            coreGroup.players.put(corePlayer);
            this.groupMap.put(groupName, coreGroup);
        }
    }

    public List<CoreGroup> getGroups() {
        List<CoreGroup> coreGroupList = new ArrayList<CoreGroup>(this.groupMap.values());
        Collections.sort(coreGroupList);
        return coreGroupList;
    }

    public int compareTo(CoreGroupLabel other) {
        Validate.notNull(other);
        return this.order.compareTo(other.order);
    }
}
