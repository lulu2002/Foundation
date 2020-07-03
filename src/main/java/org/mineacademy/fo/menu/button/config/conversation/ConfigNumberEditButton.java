package org.mineacademy.fo.menu.button.config.conversation;

import org.mineacademy.fo.Common;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.menu.button.config.NumberButton;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.model.SimpleReplacer;

import java.lang.reflect.Method;

public abstract class ConfigNumberEditButton<N extends Number> extends ConfigSaveInputButton implements NumberButton {

    private final Class numberClass;
    private N number;

    public ConfigNumberEditButton(ItemPath path, Class<? extends Number> numberClass) {
        super(path);

        this.numberClass = numberClass;
    }

    @Override
    protected boolean isInputValid(String input) {
        try {
            //整數無法case
            Method method = ReflectionUtil.getMethod(numberClass, "valueOf", String.class);
            number = ReflectionUtil.invokeStatic(method, input);
            return true;
        } catch (Exception e) {
            Common.tellConversing(getPlayer(), getInvalidNumberMsg());
            return false;
        }
    }

    protected abstract String getInvalidNumberMsg();

    @Override
    protected void onSaveInput(String input) {
        handleNumber(number);
    }

    @Override
    protected void tellSaved(String input) {
        Common.tellConversing(getPlayer(), getSavedMessage(input)
                .replace("{number}", number + ""));
    }

    @Override
    protected SimpleReplacer replaceLore(SimpleReplacer unReplacedLore) {
        return unReplacedLore.replace("{number}", getOriginalNumber());
    }

    protected abstract void handleNumber(N newValue);
}
