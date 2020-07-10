package org.mineacademy.fo.menu.button.config.conversation;

import lombok.Getter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.menu.config.ItemPath;


public abstract class ConfigInventoryEditorButton extends ConfigEditorButton {
    public static String FINISH_INPUT = "finish";

    private ItemStack[] inventoryBackup;
    private GameMode gameModeBackup;
    @Getter
    private ItemStack[] storageContent;
    @Getter
    private ItemStack[] armorContent;

    protected ConfigInventoryEditorButton(ItemPath path) {
        super(path);
    }

    @Override
    protected final void sendPrompt() {
        Player player = getPlayer();

        backup(player);
        player.getInventory().clear();
        loadInventory(player.getInventory());
        player.setGameMode(GameMode.CREATIVE);

        onStart();
    }

    @Override
    protected boolean isInputValid(String input) {
        return input.equalsIgnoreCase(FINISH_INPUT);
    }

    protected abstract void loadInventory(PlayerInventory inventory);
    protected abstract void onStart();

    @Override
    protected final void onEdit(String input) {
    }

    @Override
    protected void onEnd() {
        Player player = getPlayer();
        PlayerInventory inventory = player.getInventory();

        onSave(inventory);
        restore(player);
    }

    public abstract void onSave(PlayerInventory inventory);

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
