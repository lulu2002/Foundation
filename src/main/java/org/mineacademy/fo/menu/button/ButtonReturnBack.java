package org.mineacademy.fo.menu.button;

import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a standardized button that will return back to the parent menu
 */
@RequiredArgsConstructor
@AllArgsConstructor
public final class ButtonReturnBack extends Button {

	/**
	 * The material for this button, door by default
	 */
	@Getter
	@Setter
	private static CompMaterial material = CompMaterial.OAK_DOOR;

	/**
	 * The title of this button
	 */
	@Getter
	@Setter
	private static String title = "&4&lReturn";

	/**
	 * The lore of this button
	 */
	@Getter
	@Setter
	private static List<String> lore = Arrays.asList("", "Return back.");

	/**
	 * The parent menu
	 */
	@NonNull
	private final Menu parentMenu;

	/**
	 * Should we make a new instance of the parent menu?
	 * <p>
	 * False by default.
	 */
	private boolean makeNewInstance = false;

	/**
	 * The icon for this button
	 */
	@Override
	public ItemStack getItem() {
		return ItemCreator.of(material).name(title).lores(lore).build().make();
	}

	/**
	 * Open the parent menu when clicked
	 */
	@Override
	public void onClickedInMenu(Player pl, Menu menu, ClickType click) {
		if (makeNewInstance)
			parentMenu.newInstance().displayTo(pl);

		else
			parentMenu.displayTo(pl);
	}
}