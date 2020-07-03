package org.mineacademy.fo.menu.button.config;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.config.ItemPath;

public abstract class ConfigLeftOrRightButton extends ConfigClickableButton {

    protected ConfigLeftOrRightButton(ItemPath path) {
        super(path);
    }

    @Override
    public final void onClicked(Player player, Menu menu, ClickType click) {
        switch (click) {
            case LEFT:
                onLeftClick(player, menu);
                break;
            case RIGHT:
                onRightClick(player, menu);
                break;
        }
    }

    protected abstract void onLeftClick(Player player, Menu menu);

    protected abstract void onRightClick(Player player, Menu menu);
}
