package one.bartosz.mcpingtest.events;

import one.bartosz.mcpingtest.McPingTest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final McPingTest plugin;

    public PlayerQuitListener(McPingTest plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        //well technically we could check if player is in the maps but what's the point as long as it doesn't throw an error
        plugin.getOngoingTests().remove(player);
        plugin.getPlayerPollingResults().remove(player);
    }
}
