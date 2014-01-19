/**
 * 
 */
package net.nyvaria.nyvariacore;

import net.nyvaria.nyvariacore.coreplayer.CorePlayer;

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
 *
 */
public class NyvariaCoreListener implements Listener {
	private final NyvariaCore plugin;
	
	public NyvariaCoreListener(NyvariaCore plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		this.plugin.corePlayerList.put(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.corePlayerList.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		this.plugin.corePlayerList.remove(event.getPlayer());
	}

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInventoryCloseEvent(InventoryCloseEvent event) {
        InventoryType invType = event.getView().getTopInventory().getType();
        
        if (invType == InventoryType.PLAYER) {
            CorePlayer corePlayer = this.plugin.corePlayerList.get(event.getPlayer());
            corePlayer.invsee = false;
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        Inventory     inv     = event.getView().getTopInventory();
        InventoryType invType = inv.getType();

        if (invType == InventoryType.PLAYER) {
            CorePlayer invPeeker = this.plugin.corePlayerList.get(event.getWhoClicked());
            
            if (invPeeker.invsee) {
	            InventoryHolder invHolder = inv.getHolder();
	            
	            if (invHolder != null && invHolder instanceof HumanEntity) {
	            	CorePlayer invOwner = this.plugin.corePlayerList.get(invHolder);
	            	
	            	if (!invPeeker.hasPermission(NyvariaCore.PERM_INVSEE_MODIFY)
	            			|| invOwner.hasPermission(NyvariaCore.PERM_INVSEE_MODIFY_PREVENT)
	            			|| !invOwner.player.isOnline()) {
	            		event.setCancelled(true);
                        //invPeeker.updateInventory();
	            	}
	            }
            }
        }
	}
}
