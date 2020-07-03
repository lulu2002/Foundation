package org.mineacademy.fo.menu.config;

import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.config.ConfigMenuButton;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ConfigMenu extends ClickUpdateMenu {

    private final Map<Integer, ConfigMenuButton> buttons = new HashMap<>();
    private final MenuSection section;

    public ConfigMenu(MenuSection section, Menu parent) {
        super(parent);

        this.section = section;

        setTitle(section.getTitle());
        setSize(section.getSize());
    }

    @Override
    public Menu newInstance() {
        return this;
    }

    @Override
    protected final List<Button> getButtonsToAutoRegister() {
        autoRegisterConfigMenuButtons();

        List<Button> toRegister = new ArrayList<>();

        toRegister.addAll(buttons.values().stream().map(ConfigMenuButton::getButton).collect(Collectors.toList()));
        toRegister.addAll(getCustomButtons());

        return toRegister;
    }

    private void autoRegisterConfigMenuButtons() {
        try {
            Field[] fields = ReflectionUtil.getAllFields(ConfigMenu.this.getClass());

            for (Field field : fields) {
                field.setAccessible(true);
                Object object = field.get(ConfigMenu.this);
                if (object instanceof ConfigMenuButton) {
                    ConfigMenuButton button = (ConfigMenuButton) object;
                    buttons.put(button.getSlot(), button);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ItemStack getItemAt(int slot) {
        ConfigMenuButton button = buttons.get(slot);

        if (button != null)
            return button.getItem();

        return super.getItemAt(slot);
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    protected List<Button> getCustomButtons() {
        return new ArrayList<>();
    }

    protected ItemPath getButtonPath(String buttonName) {
        return new ItemPath(section.getFile(), section.getPath() + ".Buttons." + buttonName);
    }
}