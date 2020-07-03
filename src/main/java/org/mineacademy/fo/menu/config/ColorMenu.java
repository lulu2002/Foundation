package org.mineacademy.fo.menu.config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.remain.CompColor;
import org.mineacademy.fo.remain.CompMaterial;

public abstract class ColorMenu extends ConfigMenuPagged<ChatColor> {

    public ColorMenu(Menu parent, MenuSection section) {
        super(parent, section, CompColor.getChatColors());

    }

    @Override
    protected ItemStack convertToItemStack(ChatColor chatColor) {
        return CompMaterial.makeWool(CompColor.fromChatColor(chatColor), 1);
    }

    @Override
    protected final void onPageClick(Player player, ChatColor chatColor, ClickType clickType) {
        onChooseColor(player, chatColor);
        getParent().displayTo(player);
    }

    protected abstract void onChooseColor(Player player, ChatColor chatColor);
}
