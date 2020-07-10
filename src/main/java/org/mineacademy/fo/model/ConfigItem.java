package org.mineacademy.fo.model;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.config.ItemPath;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.List;

@Getter
@Setter
public class ConfigItem extends YamlConfig {

    private final CompMaterial material;
    private final String name;
    private final List<String> lore;
    private final Integer slot;

    public ConfigItem(ItemPath path) {
        loadConfiguration(path.getFile());
        pathPrefix(path.getPath());

        material = getMaterial("Type");
        name = getString("Name");
        lore = getStringList("Lore");
        slot = getInteger("Slot");

        saveIfNecessary();
    }

    public static ConfigItem fromItemsFile(String fileName, String path) {
        return new ConfigItem(new ItemPath(fileName, path));
    }

    public static ItemStack getItem(String fileName, String path) {
        return fromItemsFile(fileName, path).getItem();
    }

    public ItemStack getItem() {
        return ItemCreator.of(getMaterial(), getName(), getLore()).build().make();
    }

    public List<String> getLore() {

        SimpleReplacer replacer = new SimpleReplacer(lore);
        replacer = replaceLore(replacer);

        return replacer.buildList();
    }

    public String getName() {
        return replaceName(name);
    }

    protected String replaceName(String unReplacedName) {
        return unReplacedName;
    }

    protected SimpleReplacer replaceLore(SimpleReplacer unReplacedLore) {
        return unReplacedLore;
    }
}
