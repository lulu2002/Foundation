package org.mineacademy.fo.menu.config;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.model.ConfigItem;
import org.mineacademy.fo.settings.YamlSectionConfig;

@Getter
public class MenuSection extends YamlSectionConfig {

    private final String path;
    private final String title;
    private final int rows;
    private final int size;

    public MenuSection(String file, String sectionPrefix) {
        super(sectionPrefix);

        loadConfiguration(file);

        this.path = sectionPrefix;
        this.title = getString("Title");
        this.rows = getInteger("Rows");
        this.size = rows * 9;

        saveIfNecessary();
    }

    public ItemStack getButtonItem(String buttonName) {
        return getConfigItem(buttonName).getItem();
    }

    public String getFile() {
        return getFileName();
    }

    public ConfigItem getConfigItem(String buttonName) {

        ConfigItem configItem = new ConfigItem(new ItemPath(getFileName(), formPathPrefix("Buttons." + buttonName)));

        return configItem;
    }
}
