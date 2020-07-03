package org.mineacademy.fo.menu.config;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;

/**
 * 2019-07-17 下午 06:24
 */
public abstract class ClickUpdateMenu extends Menu {

    protected ClickUpdateMenu(Menu parent) {
        super(parent);
    }

    @Override
    protected final void onButtonClick(Player player, int slot, InventoryAction action, ClickType click, Button button) {
        super.onButtonClick(player, slot, action, click, button);

        updateSlot(slot);

        onButtonClickedUpdated(player, slot, action, click, button);
    }

    protected void onButtonClickedUpdated(Player player, int slot, InventoryAction action, ClickType click, Button button) {

    }

    protected final void updateSlot(int slot) {
        if (isPlayerStillInThisMenu(getViewer()))
            getViewer().getOpenInventory().getTopInventory().setItem(slot, getItemAt(slot));
    }

    protected boolean isPlayerStillInThisMenu(Player player) {
        return getMenu(player) == this;
    }

    @Override
    protected String[] getInfo() {
        return null;
    }
}