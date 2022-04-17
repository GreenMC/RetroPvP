package io.github.greenmc.retropvp;

import io.github.greenmc.retropvp.features.language.LanguageManager;
import io.github.greenmc.retropvp.features.leaderboards.LeaderboardManager;
import io.github.greenmc.retropvp.features.placeholders.CustomPlaceholderManager;
import io.github.greenmc.retropvp.users.User;
import io.github.greenmc.retropvp.users.UserManager;
import org.bukkit.plugin.java.JavaPlugin;

public class RetroPvP extends JavaPlugin {

    private CustomPlaceholderManager customPlaceholderManager;
    private LeaderboardManager leaderboardManager;
    private LanguageManager languageManager;
    private UserManager userManager;

    @Override
    public void onEnable() {
        registerManagers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerManagers() {
        customPlaceholderManager = new CustomPlaceholderManager(this);
        languageManager = new LanguageManager(this);
        userManager = new UserManager(this);
        leaderboardManager = new LeaderboardManager(this);
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

    public void saveAllUserStatistics(boolean sync) {
        for (User user : userManager.getUserList()) {
            userManager.saveStatistics(user, sync);
        }
    }

}