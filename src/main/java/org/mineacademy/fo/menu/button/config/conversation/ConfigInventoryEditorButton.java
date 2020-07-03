package org.mineacademy.fo.menu.button.config.conversation;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.config.ItemPath;


public abstract class ConfigInventoryEditorButton extends ConfigEditorButton {
    public static String FINISH_INPUT = "finish";

    private ItemStack[] inventoryBackup;
    private GameMode gameModeBackup;

    protected ConfigInventoryEditorButton(ItemPath path) {
        super(path);
    }

    @Override
    protected void sendPrompt() {
        Player player = getPlayer();

        backup(player);
        player.getInventory().setContents(loadInventory().getContents());
        player.setGameMode(GameMode.CREATIVE);

        onStart();
    }

    @Override
    protected boolean isInputValid(String input) {
        return input.equalsIgnoreCase(FINISH_INPUT);
    }

    protected abstract void onStart();

    protected abstract Inventory loadInventory();

    @Override
    protected final void onEdit(String input) {
    }

    @Override
    protected void onEnd() {
        Player player = getPlayer();

        onSave(player.getInventory().getContents());
        restore(player);
    }

    public abstract void onSave(ItemStack[] contents);


    private void backup(Player player) {
        gameModeBackup = player.getGameMode();
        inventoryBackup = player.getInventory().getContents();
    }

    private void restore(Player player) {
        player.getInventory().clear();

        player.getInventory().setContents(inventoryBackup);
        player.setGameMode(gameModeBackup);
    }
}
