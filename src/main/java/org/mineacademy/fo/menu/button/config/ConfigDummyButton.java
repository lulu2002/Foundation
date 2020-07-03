package org.mineacademy.fo.menu.button.config;

import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.menu.model.ItemCreator;

public class ConfigDummyButton extends ConfigMenuButton {

    public ConfigDummyButton(ItemPath path) {
        super(path);
    }

    @Override
    protected Button getButtonToRegister() {
        return Button.makeDummy(ItemCreator.of(getItem()));
    }
}
