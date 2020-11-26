package org.mineacademy.fo.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.collection.SerializedMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InventoryContent implements ConfigSerializable {
	private ItemStack[] storage;
	private ItemStack[] armor;
	private ItemStack[] extra;

	public InventoryContent(Player player) {
		this(player.getInventory());
	}

	public InventoryContent(PlayerInventory inv) {
		this.storage = getStorageContent(inv);
		this.extra = getExtraContent(inv);
		this.armor = inv.getArmorContents();
	}

	public static ItemStack[] contentsOf(Player player) {
		return contentsOf(player.getInventory());
	}

	public static ItemStack[] contentsOf(PlayerInventory inventory) {
		return new InventoryContent(inventory).getAllItems();
	}

	private ItemStack[] getStorageContent(PlayerInventory inv) {
		//getContents() returns all contents in version above 1_9_R2,
		// or it's gonna returns storage only.
		//This class is made to solve this problem.

		try {
			return inv.getStorageContents();
		} catch (NoSuchMethodError e) {
			return inv.getContents();
		}
	}

	private ItemStack[] getExtraContent(PlayerInventory inv) {
		try {
			return inv.getExtraContents();
		} catch (NoSuchMethodError e) {
			return new ItemStack[0];
		}
	}

	public void setContents(Player player) {
		setContents(player.getInventory());
	}

	public void setContents(PlayerInventory inv) {
		setStorageContents(inv);
		setExtraContents(inv);
		inv.setArmorContents(armor);
	}

	public ItemStack[] getAllItems() {
		return mergeArrays(storage, armor, extra);
	}

	private ItemStack[] mergeArrays(ItemStack[]... arrays) {
		List<ItemStack> list = new ArrayList<>();

		for (ItemStack[] array : arrays)
			list.addAll(Arrays.asList(array));

		return list.toArray(new ItemStack[0]);
	}

	private void setExtraContents(PlayerInventory inventory) {
		try {
			inventory.setExtraContents(extra);
		} catch (NoSuchMethodError e) {

		}
	}

	private void setStorageContents(PlayerInventory inventory) {
		try {
			inventory.setStorageContents(storage);
		} catch (NoSuchMethodError e) {
			inventory.setContents(storage);
		}
	}

	@Override
	public SerializedMap serialize() {
		SerializedMap map = new SerializedMap();

		map.put("Storage", storage.clone());
		map.put("Armor", armor.clone());
		map.put("Extra", extra.clone());

		return map;
	}

	public static InventoryContent deserialize(SerializedMap map) {
		InventoryContent inventoryContent = new InventoryContent();

		inventoryContent.storage = getArray(map, "Storage");
		inventoryContent.armor = getArray(map, "Armor");
		inventoryContent.extra = getArray(map, "Extra");

		return inventoryContent;
	}

	private static ItemStack[] getArray(SerializedMap map, String key) {
		return map.getListSafe(key, ItemStack.class).toArray(new ItemStack[0]);
	}
}
