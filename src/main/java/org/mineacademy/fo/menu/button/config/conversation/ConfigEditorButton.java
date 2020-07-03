package org.mineacademy.fo.menu.button.config.conversation;

import org.bukkit.conversations.ConversationAbandonedEvent;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.mineacademy.fo.conversation.SimpleConversation;
import org.mineacademy.fo.conversation.SimplePrompt;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleSound;
import org.mineacademy.fo.remain.CompSound;

public abstract class ConfigEditorButton extends ConfigConversationButton {

    protected ConfigEditorButton(ItemPath path) {
        super(path);
    }

    protected abstract void sendPrompt();

    @Override
    protected final void beforeConversation() {
        sendPrompt();
    }

    protected boolean isInputValid(String input) {
        return true;
    }

    protected abstract void onEdit(String input);

    protected void onEnd() {

    }

    @Override
    protected final SimplePrompt getPrompt() {
        return new SimplePrompt() {
            @Override
            protected String getPrompt(ConversationContext ctx) {
                return "";
            }

            @Override
            protected boolean isInputValid(ConversationContext context, String input) {
                return ConfigEditorButton.this.isInputValid(input);
            }

            @Override
            public void onConversationEnd(SimpleConversation conversation, ConversationAbandonedEvent event) {
                onEnd();
            }

            @Override
            protected Prompt acceptValidatedInput(ConversationContext conversationContext, String s) {
                onEdit(s);
                return Prompt.END_OF_CONVERSATION;
            }
        };
    }

    @Override
    protected SimpleSound getClickSound() {
        return new SimpleSound(CompSound.SPLASH2.getSound(), 0.8F, 1F);
    }
}
