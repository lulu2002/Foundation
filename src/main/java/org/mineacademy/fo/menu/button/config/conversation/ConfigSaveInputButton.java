package org.mineacademy.fo.menu.button.config.conversation;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.menu.config.ItemPath;

public abstract class ConfigSaveInputButton extends ConfigEditorButton {

    protected ConfigSaveInputButton(ItemPath path) {
        super(path);
    }

    @Override
    protected final void sendPrompt() {
        Common.tellConversing(getPlayer(), getMessage());
    }

    @Override
    protected final void onEdit(String input) {
        onSaveInput(input);
        tellSaved(input);
    }

    protected void tellSaved(String input) {
        Common.tellConversing(getPlayer(), getSavedMessage(input));
    }

    protected abstract void onSaveInput(String input);

    protected abstract String getMessage();

    protected abstract String getSavedMessage(String input);
}
