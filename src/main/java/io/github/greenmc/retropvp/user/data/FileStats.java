package io.github.greenmc.retropvp.user.data;

import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import me.despical.commons.configuration.ConfigUtils;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @author Despical
 * <p>
 * Created at 17.04.2022
 */
public class FileStats implements UserDatabase {

	private final FileConfiguration config;

	public FileStats() {
		this.config = ConfigUtils.getConfig(plugin, "stats");
	}

	@Override
	public void saveStatistic(User user, StatsStorage.StatisticType stat) {
		config.set(user.getUniqueId().toString() + "." + stat.getName(), user.getStat(stat));

		ConfigUtils.saveConfig(plugin, config, "stats");
	}

	@Override
	public void saveAllStatistic(User user) {
		String uuid = user.getUniqueId().toString();

		for (StatsStorage.StatisticType stat : StatsStorage.StatisticType.values()) {
			if (!stat.isPersistent()) continue;

			config.set(uuid + "." + stat.getName(), user.getStat(stat));
		}

		ConfigUtils.saveConfig(plugin, config, "stats");
	}

	@Override
	public void loadStatistics(User user) {
		String uuid = user.getUniqueId().toString();

		for (StatsStorage.StatisticType stat : StatsStorage.StatisticType.values()) {
			user.setStat(stat, config.getInt(uuid + "." + stat.getName()));
		}
	}

}