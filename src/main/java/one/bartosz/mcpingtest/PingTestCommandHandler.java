package one.bartosz.mcpingtest;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.ArrayList;

public class PingTestCommandHandler implements CommandExecutor {

    private final McPingTest plugin;
    private final int duration;
    private final boolean kickAfterTest;

    public PingTestCommandHandler(McPingTest plugin) {
        this.plugin = plugin;
        this.duration = plugin.getConfig().getInt("testDuration");
        this.kickAfterTest = plugin.getConfig().getBoolean("kickAfterTest");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("pingtest")) return false;
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return false;
        }
        if (!(args.length >= 1)) {
            player.sendMessage(ChatColor.RED + "No arguments! Usage: /pingtest <start/stop>");
            return false;
        }
        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (plugin.getOngoingTests().containsKey(player))
                    player.sendMessage(ChatColor.RED + "You have started a test already!");
                else {
                    plugin.getOngoingTests().put(player, Instant.now().getEpochSecond());
                    plugin.getPlayerPollingResults().put(player, new ArrayList<>());
                    player.sendMessage(ChatColor.GREEN + "Ping test started. You may need to wait a few seconds to see the first result (if the option is enabled). You can stop it with command /pingtest stop.");
                }
            }
            case "stop" -> {
                if (!plugin.getOngoingTests().containsKey(player))
                    player.sendMessage(ChatColor.RED + "You haven't started a test! You can do it with command /pingtest start.");
                else {
                    int listSize = plugin.getPlayerPollingResults().get(player).size();
                    int avgSum = plugin.getPlayerPollingResults().get(player).stream().mapToInt(Integer::intValue).sum();
                    int avg = avgSum / listSize;
                    plugin.getPlayerPollingResults().remove(player);
                    plugin.getOngoingTests().remove(player);
                    String endMessage = "Ping test finished! Your average ping: " + avg + " ms. (measured " + listSize + " times in " + duration + " seconds)";
                    if (kickAfterTest) {
                        player.kickPlayer(ChatColor.GREEN + endMessage);
                    } else player.sendMessage(ChatColor.GREEN + endMessage);
                }
            }
            default -> player.sendMessage(ChatColor.RED + "Invalid value for argument no.1! Valid values: start, stop");
        }
        return false;
    }
}
