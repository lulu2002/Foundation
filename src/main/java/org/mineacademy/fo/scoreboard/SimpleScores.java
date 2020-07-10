package org.mineacademy.fo.scoreboard;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class SimpleScores {

    private static final HashMap<UUID, SimpleScores> allScores = new HashMap<>();

    private final Player player;
    private final Scoreboard scoreboard;
    private final SimpleSidebar sideBar;
    private String title;

    protected SimpleScores(Player player, String title) {
        this.player = player;
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.sideBar = SimpleSidebar.createSidebarIn(scoreboard);
        this.title = title;

        sideBar.setTitle(title);
        player.setScoreboard(scoreboard);
        allScores.put(player.getUniqueId(), this);
    }

    public static Collection<SimpleScores> getAllScores() {
        return Sets.newHashSet(allScores.values());
    }

    public static void removeScores(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        allScores.remove(player.getUniqueId());
    }

    public synchronized void updateSidebar() {
        SimpleSidebar sidebar = getSideBar();

        sidebar.setTitle(title);
        sidebar.setSlotsFromList(getLines(player));
    }

    public abstract List<String> getLines(Player player);
}
