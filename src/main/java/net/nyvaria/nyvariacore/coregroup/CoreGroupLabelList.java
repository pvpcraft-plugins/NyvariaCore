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
    private HashMap<String, CoreGroupLabel> labelMap;

    public static final String DEFAULT_GROUP_LABEL = "PLAYERS";

    public CoreGroupLabelList() {
        HashMap<String, String> groupMap = new HashMap<String, String>();
        labelMap = new HashMap<String, CoreGroupLabel>();

        ConfigurationSection labelsConfig = NyvariaCore.getInstance().getConfig().getConfigurationSection("group-labels");
        Set<String> labels = labelsConfig.getKeys(false);
        labels.add(CoreGroupLabelList.DEFAULT_GROUP_LABEL);

        int order = 0;
        for (String label : labels) {
            List<String> groups = labelsConfig.getStringList(label);
            labelMap.put(label, new CoreGroupLabel(label, ++order, groups));

            for (String group : groups) {
                groupMap.put(group, label);
            }
        }

        for (CorePlayer corePlayer : NyvariaCore.getInstance().getCorePlayerList()) {
            CoreGroup group = corePlayer.getPrimaryGroup();
            String groupLabel;

            if (groupMap.containsKey(group.getName())) {
                groupLabel = groupMap.get(group.getName());
            } else {
                groupLabel = CoreGroupLabelList.DEFAULT_GROUP_LABEL;
            }

            labelMap.get(groupLabel).add(corePlayer, group.getName());
        }
    }

    public List<CoreGroupLabel> getSortedList() {
        List<CoreGroupLabel> list = new ArrayList<CoreGroupLabel>(labelMap.values());
        Collections.sort(list);
        return list;
    }
}
