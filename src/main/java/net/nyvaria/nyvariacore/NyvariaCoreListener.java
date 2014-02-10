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
package net.nyvaria.nyvariacore;

import net.nyvaria.nyvariacore.coreplayer.CorePlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Paul Thompson
 */
public class NyvariaCoreListener implements Listener {
    private final NyvariaCore plugin;

    public NyvariaCoreListener(NyvariaCore plugin) {
        Validate.notNull(plugin, "NyvariaCoreListener cannot have a null plugin");
        this.plugin = plugin;
        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getCorePlayerList().put(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getCorePlayerList().remove(event.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        plugin.getCorePlayerList().remove(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        InventoryType invType = event.getView().getTopInventory().getType();

        if (invType == InventoryType.PLAYER) {
            CorePlayer corePlayer = plugin.getCorePlayerList().get(event.getPlayer());
            corePlayer.setIsInvseeing(false);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory inv = event.getView().getTopInventory();
        InventoryType invType = inv.getType();

        if (invType == InventoryType.PLAYER) {
            CorePlayer invPeeker = plugin.getCorePlayerList().get(event.getWhoClicked());

            if (invPeeker.isInvseeing()) {
                InventoryHolder invHolder = inv.getHolder();

                if (invHolder != null && invHolder instanceof HumanEntity) {
                    CorePlayer invOwner = plugin.getCorePlayerList().get(invHolder);

                    if (!invPeeker.hasPermission(NyvariaCore.PERM_INVSEE_MODIFY)
                            || invOwner.hasPermission(NyvariaCore.PERM_INVSEE_MODIFY_PREVENT)
                            || !invOwner.getPlayer().isOnline()) {
                        event.setCancelled(true);
                        //invPeeker.updateInventory();
                    }
                }
            }
        }
    }
}
