package io.github.greenmc.retropvp.user.scoreboard;

import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

/**
 * @author Despical
 * <p>
 * Created at 25.04.2022
 */
public class ScoreboardManager {

	private final User user;

	private int mode = 1;
	private Scoreboard scoreboard;

	public ScoreboardManager(User user) {
		this.user = user;
	}

	public void createScoreboard() {
		scoreboard = ScoreboardLib.createScoreboard(user.getPlayer()).setHandler(new ScoreboardHandler() {

			@Override
			public String getTitle(Player player) {
				return Utils.getMessage("scoreboard.title-" + mode);
			}

			@Override
			public List<Entry> getEntries(Player player) {
				return formatScoreboard();
			}
		});

		scoreboard.setUpdateInterval(2400); // 2 min
		scoreboard.activate();
	}

	public void removeScoreboard() {
		scoreboard.deactivate();
	}

	private List<Entry> formatScoreboard() {
		EntryBuilder builder = new EntryBuilder();

		switch (mode) {
			case 1:
				for (StatsStorage.StatisticType type : StatsStorage.StatisticType.values()) {
					builder.put(Utils.getMessage("scoreboard." + type.getName(), null), user.getStat(type));
				}

				break;
			case 2:
				for (Map.Entry<String, Integer> entry : Leaderboards.getTopKills()) {
					builder.put(entry.getKey(), entry.getValue());
				}
				break;
			case 3:
				for (Map.Entry<String, Integer> entry : Leaderboards.getTopStreaks()) {
					builder.put(entry.getKey(), entry.getValue());
				}
				break;
		}
		mode++;
		if (mode == 4) mode = 1;
		return builder.get();
	}

	public void switchMode(int mode) {
		this.mode = mode;
		scoreboard.deactivate();
		scoreboard.activate();
	}

}