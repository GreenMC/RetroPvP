package io.github.greenmc.retropvp.features.leaderboards;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.AbstractMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Leaderboards {

	private static final RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);
	private static final Set<Map.Entry<String, Integer>> topKills = new HashSet<>();
	private static final Set<Map.Entry<String, Integer>> topStreaks = new HashSet<>();

	private static BukkitRunnable task;

	public static void startTask() {
		if (task != null) {
			task.cancel();
		}
		task = new BukkitRunnable() {

			@Override
			public void run() {
				saveAndRefresh();
			}
		};

		task.runTaskTimerAsynchronously(plugin, 2300, 2300);
	}

	public static void saveAndRefresh() {
		plugin.saveAllUserStatistics(false);
		refresh();
	}

	private static void refresh() {
		int i = 0;
		topKills.clear();
		for (Map.Entry<String, Integer> entry : StatsStorage.getStats(StatsStorage.StatisticType.KILLS).entrySet()) {
			topKills.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
			i++;
			if (i >= 15) break;
		}
		i = 0;
		topStreaks.clear();
		for (Map.Entry<String, Integer> entry : StatsStorage.getStats(StatsStorage.StatisticType.MAX_STREAK).entrySet()) {
			topStreaks.add(new AbstractMap.SimpleEntry<>(entry.getKey(), entry.getValue()));
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

	public static Set<Map.Entry<String, Integer>> getTopKills() {
		if (topKills.isEmpty()) {
			throw new IllegalStateException("Not ready yet!");
		}

		return topKills;
	}

	public static Set<Map.Entry<String, Integer>> getTopStreaks() {
		if (topStreaks.isEmpty()) {
			throw new IllegalStateException("Not ready yet!");
		}

		return topStreaks;
	}
}