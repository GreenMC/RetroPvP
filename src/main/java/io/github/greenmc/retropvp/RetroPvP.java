package io.github.greenmc.retropvp;

import io.github.greenmc.retropvp.commands.AdminCommands;
import io.github.greenmc.retropvp.commands.PlayerCommands;
import io.github.greenmc.retropvp.features.kit.KitManager;
import io.github.greenmc.retropvp.features.language.LanguageManager;
import io.github.greenmc.retropvp.features.leaderboards.Leaderboards;
import io.github.greenmc.retropvp.features.placeholders.CustomPlaceholderManager;
import io.github.greenmc.retropvp.features.spawn.SpawnManager;
import io.github.greenmc.retropvp.listeners.ChatListener;
import io.github.greenmc.retropvp.listeners.PlayerListener;
import io.github.greenmc.retropvp.listeners.ServerListener;
import io.github.greenmc.retropvp.user.User;
import io.github.greenmc.retropvp.user.UserManager;
import me.despical.commandframework.CommandFramework;
import me.despical.commons.exception.ExceptionLogHandler;
import me.despical.commons.scoreboard.ScoreboardLib;
import me.despical.commons.util.LogUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RetroPvP extends JavaPlugin {

	private ExceptionLogHandler exceptionLogHandler;
	private CommandFramework commandFramework;

	private CustomPlaceholderManager customPlaceholderManager;
	private LanguageManager languageManager;
	private SpawnManager spawnManager;
    private UserManager userManager;
	private KitManager kitManager;
	private Permission permission;
	private Chat chat;

    @Override
    public void onEnable() {
        if (getDescription().getVersion().contains("debug") || getConfig().getBoolean("Debug-Messages")) {
            LogUtils.enableLogging();
            LogUtils.setLoggerName("RetroPvP");
        }

        exceptionLogHandler = new ExceptionLogHandler(this);
        exceptionLogHandler.setMainPackage("io.github.greenmc.retropvp");
        exceptionLogHandler.setRecordMessage("[RetroPvP] We have found a bug in the code!");

        LogUtils.log("Initialization started!");
        long start = System.currentTimeMillis();

        saveDefaultConfig();
        initializeClasses();

		for (User user : userManager.getUsers()) {
			user.createScoreboard();
		}

        LogUtils.log("Initialization finished took {0} ms.", System.currentTimeMillis() - start);
    }

    @Override
    public void onDisable() {
        LogUtils.log("System disable initialized.");
        long start = System.currentTimeMillis();

        getServer().getLogger().removeHandler(exceptionLogHandler);
		getServer().getScheduler().cancelTasks(this);
        saveAllUserStatistics(true);
		spawnManager.save();

        LogUtils.log("System disable finished took {0} ms.", System.currentTimeMillis() - start);
        LogUtils.disableLogging();
    }

    private void initializeClasses() {
		setupChat();
		setupPermissions();

		spawnManager = new SpawnManager(this);
		kitManager = new KitManager(this);
        userManager = new UserManager(this);
        languageManager = new LanguageManager(this);
        customPlaceholderManager = new CustomPlaceholderManager(this);
		commandFramework = new CommandFramework(this);
		ScoreboardLib.setPluginInstance(this);

		Leaderboards.saveAndRefresh();
		Leaderboards.startTask();

		new ChatListener(this);
		new PlayerListener(this);
		new ServerListener(this);

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

	public SpawnManager getSpawnManager() {
		return spawnManager;
	}

	public UserManager getUserManager() {
        return userManager;
    }

	public KitManager getKitManager() {
		return kitManager;
	}

	private void setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
	}

	public Chat getChat() {
		return chat;
	}

	private void setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		permission = rsp.getProvider();
	}

	public Permission getPermission() {
		return permission;
	}

	public void saveAllUserStatistics(boolean disabling) {
		for (User user : userManager.getUsers()) {
			if (disabling) user.removeScoreboard();
			userManager.getDatabase().saveAllStatistic(user);
		}
	}

}