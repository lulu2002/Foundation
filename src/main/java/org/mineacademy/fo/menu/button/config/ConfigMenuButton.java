package org.mineacademy.fo.menu.button.config;

import lombok.Getter;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.ConfigItem;

@Getter
public abstract class ConfigMenuButton extends ConfigItem {

    private final Button button;
    private final Integer slot;

    protected ConfigMenuButton(ItemPath path) {
        super(path);

        this.slot = getInteger("Slot");

        button = getButtonToRegister();
    }

    protected abstract Button getButtonToRegister();
}
