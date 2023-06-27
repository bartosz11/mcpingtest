package one.bartosz.mcpingtest.events;

import one.bartosz.mcpingtest.McPingTest;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.time.Instant;
import java.util.ArrayList;

public class PlayerJoinListener implements Listener {

    private final McPingTest plugin;
    private final boolean testOnJoin;

    public PlayerJoinListener(McPingTest plugin) {
        this.plugin = plugin;
        testOnJoin = plugin.getConfig().getBoolean("startTestOnJoin");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        if (testOnJoin && player.hasPermission("mcpingtest.onjoin")) {
            plugin.getOngoingTests().put(player, Instant.now().getEpochSecond());
            plugin.getPlayerPollingResults().put(player, new ArrayList<>());
            player.sendMessage(ChatColor.GREEN+"Ping test started. You may need to wait a few seconds to see the first result (if the option is enabled). You can stop it with command /pingtest stop.");
        }
    }
}
