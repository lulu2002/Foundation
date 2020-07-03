package org.mineacademy.fo.menu.button.config.conversation;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.TimeUtil;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleReplacer;

public abstract class ConfigTimeEditButton extends ConfigSaveInputButton {

    private int seconds;

    protected ConfigTimeEditButton(ItemPath path) {
        super(path);
    }

    @Override
    protected final void onSaveInput(String input) {
        onAcceptInput(seconds);
    }

    protected abstract void onAcceptInput(int time);

    protected abstract int getCurrentTime();

    @Override
    protected SimpleReplacer replaceLore(SimpleReplacer unReplacedLore) {
        return TimeUtil.getTimeReplacer(unReplacedLore.getMessages(), getCurrentTime());
    }

    @Override
    protected void tellSaved(String input) {
        Common.tellConversing(getPlayer(), TimeUtil.replacePlaceholders(getSavedMessage(input), getCurrentTime()));
    }

    @Override
    protected boolean isInputValid(String input) {
        try {
            seconds = TimeUtil.parseToSeconds(input);
            return true;
        } catch (Exception ex) {
            Common.tellConversing(getPlayer(), getInvalidTimeMessage());
            return false;
        }
    }

    protected String getInvalidTimeMessage() {
        return "&aInvalid Time!";
    }
}
