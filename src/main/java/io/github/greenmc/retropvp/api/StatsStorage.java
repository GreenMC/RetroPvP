package io.github.greenmc.retropvp.api;

import io.github.greenmc.retropvp.RetroPvP;
import me.despical.commons.configuration.ConfigUtils;
import me.despical.commons.sorter.SortUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Despical
 * <p>
 * Created at 17.04.2022
 */
public class StatsStorage {

	private static final RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);

	public static Map<String, Integer> getStats(StatisticType stat) {
		FileConfiguration config = ConfigUtils.getConfig(plugin, "stats");
		Map<String, Integer> stats = config.getKeys(false).stream().collect(Collectors.toMap(name -> name, string -> config.getInt(string + "." + stat.name), (a, b) -> b));

		return SortUtils.sortByValue(stats);
	}

	public static int getUserStats(Player player, StatisticType statisticType) {
		return plugin.getUserManager().getUser(player).getStat(statisticType);
	}

	public enum StatisticType {

		KILLS("kills"),
		DEATHS("deaths"),
		KILL_STREAK("kill_streak", false),
		MAX_STREAK("max_streak");

		String name;
		boolean persistent;

		StatisticType(String name) {
			this (name, true);
		}

		StatisticType(String name, boolean persistent) {
			this.name = name;
			this.persistent = persistent;
		}

		public String getName() {
			return name;
		}

		public boolean isPersistent() {
			return persistent;
		}
	}
}