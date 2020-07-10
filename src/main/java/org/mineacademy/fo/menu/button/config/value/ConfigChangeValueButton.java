package org.mineacademy.fo.menu.button.config.value;

import org.bukkit.entity.Player;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.config.ConfigLeftOrRightButton;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleReplacer;

public abstract class ConfigChangeValueButton extends ConfigLeftOrRightButton {

    private final int sizeChange;

    public ConfigChangeValueButton(ItemPath path, int sizeChange) {
        super(path);

        this.sizeChange = sizeChange;
    }


    @Override
    protected void onRightClick(Player player, Menu menu) {
        int newSize = getOriginalSize() + sizeChange;
        newSize = (getMaximum() == null ? newSize : Math.min(getMaximum(), newSize));

        onChangingSize(player, menu, newSize);
    }

    @Override
    protected void onLeftClick(Player player, Menu menu) {
        onChangingSize(player, menu, Math.max(getMinimum(), getOriginalSize() - sizeChange));
    }

    protected Integer getMinimum() {
        return 0;
    }

    protected Integer getMaximum() {
        return null;
    }

    @Override
    protected SimpleReplacer replaceLore(SimpleReplacer unReplacedLore) {
        return unReplacedLore.replace("{count}", getOriginalSize());
    }

    protected abstract void onChangingSize(Player player, Menu menu, int newCount);

    protected abstract int getOriginalSize();
}
