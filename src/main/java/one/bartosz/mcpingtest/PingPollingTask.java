package one.bartosz.mcpingtest;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.time.Instant;

public class PingPollingTask extends BukkitRunnable {

    private final McPingTest plugin;
    private final int testDuration;
    private final boolean sendPolledToActionBar;
    private final boolean kickAfterTest;

    public PingPollingTask(McPingTest plugin) {
        this.plugin = plugin;
        this.testDuration = plugin.getConfig().getInt("testDuration");
        this.sendPolledToActionBar = plugin.getConfig().getBoolean("sendPolledToActionBar");
        this.kickAfterTest = plugin.getConfig().getBoolean("kickAfterTest");
    }

    @Override
    public void run() {
        plugin.getOngoingTests().forEach((player, started) -> {
            //online check?
            if (Bukkit.getServer().getPlayer(player.getUniqueId()) != null) {
                int ping = player.getPing();
                plugin.getPlayerPollingResults().get(player).add(ping);
                //mess
                TextComponent msg = new TextComponent("Your ping (ms): ");
                msg.setColor(ChatColor.GREEN);
                TextComponent pingComponent = new TextComponent(String.valueOf(ping));
                pingComponent.setColor(ChatColor.RED);
                msg.addExtra(pingComponent);
                //end of mess
                if (sendPolledToActionBar) player.spigot().sendMessage(ChatMessageType.ACTION_BAR, msg);
                long duration = Instant.now().getEpochSecond() - started;
                if (duration >= testDuration) {
                    int listSize = plugin.getPlayerPollingResults().get(player).size();
                    int avgSum = plugin.getPlayerPollingResults().get(player).stream().mapToInt(Integer::intValue).sum();
                    int avg = avgSum / listSize;
                    plugin.getPlayerPollingResults().remove(player);
                    plugin.getOngoingTests().remove(player);
                    String endMessage = "Ping test finished! Your average ping: "+avg+" ms. (measured "+listSize+" times in "+duration+" seconds)";
                    if (kickAfterTest) {
                        player.kickPlayer(ChatColor.GREEN+endMessage);
                    } else player.sendMessage(ChatColor.GREEN+endMessage);
                }
            //just in case?
            } else {
                plugin.getOngoingTests().remove(player);
                plugin.getPlayerPollingResults().remove(player);
            }
        });
    }
}
