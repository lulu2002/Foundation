package org.mineacademy.fo.menu.config;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.config.ConfigClickableButton;
import org.mineacademy.fo.menu.button.config.ConfigMenuButton;

public abstract class ConfigConfirmMenu extends ConfigMenu {

    private final ConfigMenuButton yesButton;
    private final ConfigMenuButton noButton;

    protected ConfigConfirmMenu(MenuSection section, Menu parent) {
        super(section, parent);

        yesButton = new ConfigClickableButton(getButtonPath("Agree")) {
            @Override
            protected void onClicked(Player player, Menu menu, ClickType click) {
                onAgree(player, menu);
            }
        };

        noButton = new ConfigClickableButton(getButtonPath("Disagree")) {
            @Override
            protected void onClicked(Player player, Menu menu, ClickType click) {
                onDisagree(player, menu);
            }
        };
    }

    protected void onDisagree(Player player, Menu menu) {
        menu.getParent().displayTo(player);
    }

    protected abstract void onAgree(Player player, Menu menu);
}
