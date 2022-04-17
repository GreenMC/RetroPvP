package io.github.greenmc.retropvp;

import io.github.greenmc.retropvp.commands.AdminCommands;
import io.github.greenmc.retropvp.commands.PlayerCommands;
import io.github.greenmc.retropvp.features.language.LanguageManager;
import io.github.greenmc.retropvp.features.leaderboards.LeaderboardManager;
import io.github.greenmc.retropvp.features.placeholders.CustomPlaceholderManager;
import io.github.greenmc.retropvp.listeners.PlayerListener;
import io.github.greenmc.retropvp.user.UserManager;
import me.despical.commandframework.CommandFramework;
import me.despical.commons.exception.ExceptionLogHandler;
import me.despical.commons.util.LogUtils;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RetroPvP extends JavaPlugin {

	private CommandFramework commandFramework;
    private CustomPlaceholderManager customPlaceholderManager;
    private ExceptionLogHandler exceptionLogHandler;
    private LeaderboardManager leaderboardManager;
    private LanguageManager languageManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        if (getDescription().getVersion().contains("debug") || getConfig().getBoolean("Debug-Messages")) {
            LogUtils.enableLogging();
            LogUtils.setLoggerName("RetroPvP");
        }

        exceptionLogHandler = new ExceptionLogHandler(this);
        exceptionLogHandler.setMainPackage("io.github.greenmc.retropvp");
        exceptionLogHandler.addBlacklistedClass("me.despical.commons.database.MysqlDatabase");
        exceptionLogHandler.setRecordMessage("[Retro PvP] We have found a bug in the code!");

        LogUtils.log("Initialization started!");
        long start = System.currentTimeMillis();

        saveDefaultConfig();
        initializeClasses();
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);

        LogUtils.log("Initialization finished took {0} ms.", System.currentTimeMillis() - start);
    }

    @Override
    public void onDisable() {
        LogUtils.log("System disable initialized.");
        long start = System.currentTimeMillis();

        getServer().getLogger().removeHandler(exceptionLogHandler);
        saveAllUserStatistics();

        LogUtils.log("System disable finished took {0} ms.", System.currentTimeMillis() - start);
        LogUtils.disableLogging();
    }

    private void initializeClasses() {
        this.userManager = new UserManager(this);
        this.languageManager = new LanguageManager(this);
        this.customPlaceholderManager = new CustomPlaceholderManager(this);
        this.leaderboardManager = new LeaderboardManager(this);
		this.commandFramework = new CommandFramework(this);

		new AdminCommands(this);
		new PlayerCommands(this);
    }

	public CommandFramework getCommandFramework() {
		return commandFramework;
	}

	public CustomPlaceholderManager getCustomPlaceholderManager() {
        return customPlaceholderManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public LeaderboardManager getLeaderboardManager() {
        return leaderboardManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    private void saveAllUserStatistics() {
        for (Player player : getServer().getOnlinePlayers()) {
            userManager.getDatabase().saveAllStatistic(userManager.getUser(player));
        }
    }
}