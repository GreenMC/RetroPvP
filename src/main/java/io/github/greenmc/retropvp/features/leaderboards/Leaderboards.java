package io.github.greenmc.retropvp.features.leaderboards;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Leaderboards {

	private static final Set<LeaderboardEntry> topKills = new HashSet<>();
	private static final Set<LeaderboardEntry> topStreaks = new HashSet<>();

	private static BukkitRunnable task;

	public static void startTask() {
		if (task != null) {
			task.cancel();
		}
		task = new BukkitRunnable() {

			@Override
			public void run() {
				refresh();
			}
		};

		task.runTaskTimerAsynchronously(JavaPlugin.getPlugin(RetroPvP.class), 0L, 12000L);
	}

	public static void refresh() {
		int i = 0;
		topKills.clear();
		for (Map.Entry<String, Integer> entry : StatsStorage.getStats(StatsStorage.StatisticType.KILLS).entrySet()) {
			topKills.add(new LeaderboardEntry(entry.getKey(), entry.getValue()));
			i++;
			if (i >= 15) break;
		}
		i = 0;
		topStreaks.clear();
		for (Map.Entry<String, Integer> entry : StatsStorage.getStats(StatsStorage.StatisticType.MAX_STREAK).entrySet()) {
			topStreaks.add(new LeaderboardEntry(entry.getKey(), entry.getValue()));
			i++;
			if (i >= 15) break;
		}
		Bukkit.getConsoleSender().sendMessage("Leaderboards has been refreshed!");
	}

	public static void stopTask() {
		if (task != null) {
			task.cancel();
			task = null;
		}
	}

	public static Set<LeaderboardEntry> getTopKills() {
		if (topKills.isEmpty()) {
			throw new IllegalStateException("Not ready yet!");
		}
		return topKills;
	}

	public static Set<LeaderboardEntry> getTopStreaks() {
		if (topStreaks.isEmpty()) {
			throw new IllegalStateException("Not ready yet!");
		}
		return topStreaks;
	}

}