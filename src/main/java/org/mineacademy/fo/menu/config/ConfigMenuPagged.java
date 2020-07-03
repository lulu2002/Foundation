package org.mineacademy.fo.menu.config;

import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;

public abstract class ConfigMenuPagged<E> extends MenuPagged<E> {

    protected final MenuSection section;

    protected ConfigMenuPagged(Menu parent, MenuSection section, Iterable<E> pages) {
        super(MathUtil.range(section.getSize() - 9, 9 * 1, 9 * 5), parent, pages);

        setTitle(section.getTitle());

        this.section = section;
    }

    @Override
    protected String[] getInfo() {
        return null;
    }
}
