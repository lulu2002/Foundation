package org.mineacademy.fo.menu.button.config.conversation;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.config.ConfigClickableButton;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public abstract class ConfigConversationButton extends ConfigClickableButton {

    @Getter
    private Player player;

    protected ConfigConversationButton(ItemPath path) {
        super(path);
    }

    @Override
    protected final void onClicked(Player player, Menu menu, ClickType click) {
        this.player = player;

        clearChatField();
        beforeConversation();
        getPrompt().show(player, -1);
    }

    private void clearChatField() {
        for (int i = 0; i < 50; i++)
            Common.tell(player, " ");
    }

    protected void beforeConversation() {
    }

    protected abstract SimplePrompt getPrompt();

    @Override
    protected SimpleSound getClickSound() {
        return new SimpleSound(CompSound.ITEM_PICKUP.getSound(), 0.8F, 1F);
    }
}
