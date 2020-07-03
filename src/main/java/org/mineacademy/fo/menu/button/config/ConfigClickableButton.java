package org.mineacademy.fo.menu.button.config;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public abstract class ConfigClickableButton extends ConfigMenuButton {

    protected ConfigClickableButton(ItemPath path) {
        super(path);
    }

    @Override
    public Button getButtonToRegister() {
        return new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType click) {
                if (!checkPermission(player))
                    return;

                playClickSound(player);

                onClicked(player, menu, click);
            }

            @Override
            public ItemStack getItem() {
                return ConfigClickableButton.this.getItem();
            }
        };
    }

    private boolean checkPermission(Player player) {
        String permission = getPermission();

        if (permission != null && !PlayerUtil.hasPerm(player, permission)) {
            CompSound.NOTE_BASS.play(player);
            return false;
        }

        return true;
    }

    protected abstract void onClicked(Player player, Menu menu, ClickType click);


    public void broadcast(String msg, Player clicker) {
        Common.broadcast(msg.replace("{player}", clicker.getName()));
    }

    protected final void playClickSound(Player player) {
        SimpleSound sound = getClickSound();

        if (sound != null)
            sound.play(player);
    }

    protected SimpleSound getClickSound() {
        return new SimpleSound(CompSound.NOTE_STICKS.getSound(), 0.5F, 1);
    }

    protected String getPermission() {
        return null;
    }
}
