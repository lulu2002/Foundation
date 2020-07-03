package org.mineacademy.fo.menu.button.config.value;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.config.ConfigClickableButton;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleReplacer;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public abstract class ConfigBooleanButton extends ConfigClickableButton {

    public static String ENABLED_TAG = "&aOn";
    public static String DISABLED_TAG = "&cOff";

    protected ConfigBooleanButton(ItemPath path) {
        super(path);
    }

    @Override
    public final void onClicked(Player player, Menu menu, ClickType click) {
        onStatusChange(player, menu, click, !getBoolean());
    }

    protected abstract void onStatusChange(Player player, Menu menu, ClickType click, boolean newStatus);

    @Override
    protected SimpleReplacer replaceLore(SimpleReplacer unReplacedLore) {
        replaceLoreExceptStatus(unReplacedLore);
        return unReplacedLore.replace("{status}", getBoolean() ? ENABLED_TAG : DISABLED_TAG);
    }

    protected void replaceLoreExceptStatus(SimpleReplacer unReplacedLore) {

    }

    protected abstract boolean getBoolean();

    @Override
    protected SimpleSound getClickSound() {
        return new SimpleSound(CompSound.CLICK.getSound(), 0.5F, getBoolean() ? 0.5F : 1F);
    }
}
