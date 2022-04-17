package io.github.greenmc.retropvp.user.data;

import io.github.greenmc.retropvp.RetroPvP;
import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.user.User;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Despical
 * <p>
 * Created at 17.04.2022
 */
public interface UserDatabase {

	RetroPvP plugin = JavaPlugin.getPlugin(RetroPvP.class);

	void saveStatistic(User user, StatsStorage.StatisticType stat);

	void saveAllStatistic(User user);

	void loadStatistics(User user);

}