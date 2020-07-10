package org.mineacademy.fo.scoreboard;


import org.bukkit.entity.Player;
import org.mineacademy.fo.model.SimpleReplacer;

import java.util.List;

public abstract class SimpleScoreLines {

    private final List<String> lines;
    private SimpleReplacer currentGlobalReplacedLines;

    public SimpleScoreLines(List<String> lines) {
        this.lines = lines;
    }

    public List<String> getFor(Player player) {
        return replace(player, currentGlobalReplacedLines.clone()).buildList();
    }

    public void updateGlobalVariables() {
        currentGlobalReplacedLines = replaceGlobal(SimpleReplacer.from(lines));
    }

    protected abstract SimpleReplacer replace(Player player, SimpleReplacer replacer);

    protected abstract SimpleReplacer replaceGlobal(SimpleReplacer replacer);
}
