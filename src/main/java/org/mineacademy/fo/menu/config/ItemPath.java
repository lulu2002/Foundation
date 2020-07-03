package org.mineacademy.fo.menu.config;

import lombok.Getter;

/**
 * 2020-07-03 12:12 PM
 */
@Getter
public class ItemPath {
    private final String file;
    private final String path;

    public ItemPath(String file, String path) {
        this.file = file;
        this.path = path;
    }
}