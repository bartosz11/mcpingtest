package one.bartosz.mcpingtest;

import one.bartosz.mcpingtest.events.PlayerJoinListener;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;

public final class McPingTest extends JavaPlugin {

    //player + when started the test
    private final HashMap<Player, Long> ongoingTests = new HashMap<>();
    //player's ping results later to be combined into an avg
    private final HashMap<Player, List<Integer>> playerPollingResults = new HashMap<>();

    @Override
    @SuppressWarnings("ConstantConditions")
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getCommand("pingtest").setExecutor(new PingTestCommandHandler(this));
        long pollingFrequency = getConfig().getLong("pingPollingFrequency");
        new PingPollingTask(this).runTaskTimer(this, 0L, 20L * pollingFrequency);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public HashMap<Player, Long> getOngoingTests() {
        return ongoingTests;
    }

    public HashMap<Player, List<Integer>> getPlayerPollingResults() {
        return playerPollingResults;
    }
}
