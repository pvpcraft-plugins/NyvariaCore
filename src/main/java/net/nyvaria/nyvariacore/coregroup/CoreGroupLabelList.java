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

import net.nyvaria.nyvariacore.NyvariaCore;
import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

/**
 * @author Paul Thompson
 */
public class CoreGroupLabelList {
    private final NyvariaCore plugin;
    private HashMap<String, CoreGroupLabel> labelMap;
    private HashMap<String, String> groupMap;

    public static final String DEFAULT_GROUP_LABEL = "PLAYERS";

    public CoreGroupLabelList(NyvariaCore plugin) {
        this.plugin = plugin;
        this.labelMap = new HashMap<String, CoreGroupLabel>();
        this.groupMap = new HashMap<String, String>();

        ConfigurationSection labelsConfig = this.plugin.getConfig().getConfigurationSection("group-labels");
        Set<String> labels = labelsConfig.getKeys(false);
        labels.add(CoreGroupLabelList.DEFAULT_GROUP_LABEL);

        int order = 0;
        for (String label : labels) {
            List<String> groups = labelsConfig.getStringList(label);
            this.labelMap.put(label, new CoreGroupLabel(label, ++order, groups));

            for (String group : groups) {
                this.groupMap.put(group, label);
            }
        }

        for (CorePlayer corePlayer : this.plugin.getCorePlayerList()) {
            String groupName = corePlayer.getPrimaryGroup();
            String groupLabel;

            if (this.groupMap.containsKey(groupName)) {
                groupLabel = this.groupMap.get(groupName);
            } else {
                groupLabel = CoreGroupLabelList.DEFAULT_GROUP_LABEL;
            }

            this.labelMap.get(groupLabel).add(corePlayer, groupName);
        }
    }

    public List<CoreGroupLabel> getSortedList() {
        List<CoreGroupLabel> list = new ArrayList<CoreGroupLabel>(this.labelMap.values());
        Collections.sort(list);
        return list;
    }
}
