package org.mineacademy.fo.menu.button.config;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.config.ItemPath;

import java.lang.reflect.Constructor;

public class ConfigOpenMenuButton extends ConfigClickableButton {

    private final Class toOpenMenuClass;
    private final Object[] paramOfNewMenuConstructor;

    public ConfigOpenMenuButton(ItemPath path, Class toOpenMenuClass, Object... param) {
        super(path);

        this.toOpenMenuClass = toOpenMenuClass;
        paramOfNewMenuConstructor = param;
    }

    @Override
    protected void onClicked(Player player, Menu menu, ClickType click) {
        openMenuByReflection(player, menu);
    }

    private void openMenuByReflection(Player player, Menu menu) {
        Menu toOpen = (Menu) getMenuToOpenByReflection(menu);

        toOpen.displayTo(player);
    }

    private Object getMenuToOpenByReflection(Menu parent) {
        Class<?>[] paramClasses = getParamClasses();

        if (paramClasses.length == 0) {
            return ReflectionUtil.instantiate(getConstructor(Menu.class), parent);
        } else {
            return ReflectionUtil.instantiate(getConstructor(paramClasses), paramOfNewMenuConstructor);
        }
    }

    private Constructor<?> getConstructor(Class<?>... classes) {
        return ReflectionUtil.getConstructor(toOpenMenuClass, classes);
    }


    private Class<?>[] getParamClasses() {
        Class<?>[] paramClasses = new Class[paramOfNewMenuConstructor.length];

        for (int i = 0; i < paramOfNewMenuConstructor.length; i++)
            paramClasses[i] = paramOfNewMenuConstructor[i].getClass();

        return paramClasses;
    }
}
