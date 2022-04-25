package io.github.greenmc.retropvp.user.scoreboard;

import io.github.greenmc.retropvp.api.StatsStorage;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.utils.Utils;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.scoreboard.common.EntryBuilder;
import me.despical.commons.scoreboard.type.Entry;
import me.despical.commons.scoreboard.type.Scoreboard;
import me.despical.commons.scoreboard.type.ScoreboardHandler;
import me.despical.commons.util.Strings;
import org.apache.commons.lang.StringUtils;
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
		if (mode == 4) mode = 1;
		EntryBuilder builder = new EntryBuilder();
		Utils.getStringList("messages.scoreboard." + mode).stream().map(this::formatScoreboardLine).forEach(builder::next);
		mode++;
		return builder.build();
	}

	private String formatScoreboardLine(String line) {
		String formattedLine = line;

		switch (mode) {
			case 1:
				formattedLine = StringUtils.replace(formattedLine, "%kills%", Integer.toString(user.getStat(StatsStorage.StatisticType.KILLS)));
				formattedLine = StringUtils.replace(formattedLine, "%deaths%", Integer.toString(user.getStat(StatsStorage.StatisticType.DEATHS)));
				formattedLine = StringUtils.replace(formattedLine, "%kill_streak%", Integer.toString(user.getStat(StatsStorage.StatisticType.KILL_STREAK)));
				formattedLine = StringUtils.replace(formattedLine, "%max_kill_streak%", Integer.toString(user.getStat(StatsStorage.StatisticType.MAX_STREAK)));
				break;
			case 2:
				int killsLength = 0;

				for (Map.Entry<String, Integer> entry : Leaderboards.getTopKills()) {
					formattedLine = StringUtils.replace(formattedLine, "%top_killer_" + ++killsLength + "%", entry.getKey());
					formattedLine = StringUtils.replace(formattedLine, "%top_killer_" + killsLength + "_kills%", Integer.toString(entry.getValue()));
				}

				break;
			case 3:
				int streaksLength = 0;

				for (Map.Entry<String, Integer> entry : Leaderboards.getTopStreaks()) {
					formattedLine = StringUtils.replace(formattedLine, "%top_streaker_" + ++streaksLength + "%", entry.getKey());
					formattedLine = StringUtils.replace(formattedLine, "%top_streaker_" + streaksLength + "_kills%", Integer.toString(entry.getValue()));
				}

				break;
		}

		return Strings.format(formattedLine);
	}
}