package org.mineacademy.fo.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 * An advanced menu listing items with automatic page support
 *
 * @param <T> the item that each page consists of
 */
public abstract class MenuPagged<T> extends Menu {

	/**
	 * The pages by the page number, containing a list of items
	 */
	@Getter
	private final Map<Integer, List<T>> pages;

	/**
	 * The current page
	 */
	@Getter
	private int currentPage = 1;

	/**
	 * The next button automatically generated
	 */
	private Button nextButton;
	@Setter
	private static ItemStack nextPageItemModel = ItemCreator
			.of(CompMaterial.LIME_DYE)
			.name("Page {page} &8>>")
			.build().make();

	@Setter
	private static ItemStack lastPageItemModel = ItemCreator
			.of(CompMaterial.GRAY_DYE)
			.name("&7Last Page")
			.build().make();
	;

	/**
	 * The "go to previous page" button automatically generated
	 */
	private Button prevButton;
	@Setter
	private static ItemStack prevPageItemModel = ItemCreator
			.of(CompMaterial.LIME_DYE)
			.name("&8<< &fPage {page}")
			.build().make();

	@Setter
	private static ItemStack firstPageItemModel = ItemCreator
			.of(CompMaterial.GRAY_DYE)
			.name("&7First Page")
			.build().make();


	/**
	 * Create a new paged menu where each page has 3 rows + 1 bottom bar
	 *
	 * @param pages the pages
	 */
	protected MenuPagged(final Iterable<T> pages) {
		this(null, pages);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param parent the parent menu
	 * @param pages the pages the pages
	 */
	protected MenuPagged(final Menu parent, final Iterable<T> pages) {
		this(null, parent, pages, false);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param parent
	 * @param pages
	 * @param returnMakesNewInstance
	 */
	protected MenuPagged(final Menu parent, final Iterable<T> pages, final boolean returnMakesNewInstance) {
		this(null, parent, pages, returnMakesNewInstance);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param pageSize size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
	 * @param pages the pages
	 *
	 * @deprecated we recommend you don't set the page size for the menu to autocalculate
	 */
	@Deprecated
	protected MenuPagged(final int pageSize, final Iterable<T> pages) {
		this(pageSize, null, pages);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param pageSize size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
	 * @param parent the parent menu
	 * @param pages the pages the pages
	 * @deprecated we recommend you don't set the page size for the menu to autocalculate
	 */
	@Deprecated
	protected MenuPagged(final int pageSize, final Menu parent, final Iterable<T> pages) {
		this(pageSize, parent, pages, false);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param pageSize
	 * @param parent
	 * @param pages
	 * @param returnMakesNewInstance *
	 * @deprecated we recommend you don't set the page size for the menu to
	 * autocalculate
	 */
	@Deprecated
	protected MenuPagged(final int pageSize, final Menu parent, final Iterable<T> pages, final boolean returnMakesNewInstance) {
		this((Integer) pageSize, parent, pages, returnMakesNewInstance);
	}

	/**
	 * Create a new paged menu
	 *
	 * @param pageSize size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
	 * @param parent the parent menu
	 * @param pages the pages the pages
	 * @param returnMakesNewInstance should we re-instatiate the parent menu when
	 *                               returning to it?
	 */
	private MenuPagged(final Integer pageSize, final Menu parent, final Iterable<T> pages, final boolean returnMakesNewInstance) {
		super(parent, returnMakesNewInstance);

		final int items = getItemAmount(pages);
		final int autoPageSize = pageSize != null ? pageSize : items <= 9 ? 9 * 1 : items <= 9 * 2 ? 9 * 2 : items <= 9 * 3 ? 9 * 3 : items <= 9 * 4 ? 9 * 4 : 9 * 5;

		this.currentPage = 1;
		this.pages = fillPages(autoPageSize, pages);

		setSize(9 + autoPageSize);
		setButtons();
	}

	/**
	 * Dynamically populates the pages
	 *
	 * @param allItems all items that will be split
	 * @return the map containing pages and their items
	 */
	private Map<Integer, List<T>> fillPages(int cellSize, Iterable<T> items) {
		final List<T> allItems = Common.toList(items);

		final Map<Integer, List<T>> pages = new HashMap<>();
		final int pageCount = allItems.size() == cellSize ? 0 : allItems.size() / cellSize;

		for (int i = 0; i <= pageCount; i++) {
			final List<T> pageItems = new ArrayList<>();

			final int down = cellSize * i;
			final int up = down + cellSize;

			for (int valueIndex = down; valueIndex < up; valueIndex++)
				if (valueIndex < allItems.size()) {
					final T page = allItems.get(valueIndex);

					pageItems.add(page);
				} else
					break;

			pages.put(i, pageItems);
		}

		return pages;
	}

	@SuppressWarnings("unused")
	private int getItemAmount(final Iterable<T> pages) {
		int amount = 0;

		for (final T t : pages)
			amount++;

		return amount;
	}

	// Render the next/prev buttons
	private void setButtons() {
		final boolean hasPages = pages.size() > 1;

		// Set previous button
		this.prevButton = new Button() {
			final boolean canGo = currentPage > 1;

			@Override
			public void onClickedInMenu(final Player pl, final Menu menu, final ClickType click) {
				if (canGo) {
					currentPage = MathUtil.range(currentPage - 1, 1, pages.size());

					updatePage();
				}
			}

			@Override
			public ItemStack getItem() {
				return getPageToggleItem(prevPageItemModel, firstPageItemModel, currentPage - 1);
			}
		};

		// Set next page button
		this.nextButton = new Button() {
			final boolean canGo = currentPage < pages.size();

			@Override
			public void onClickedInMenu(final Player pl, final Menu menu, final ClickType click) {
				if (canGo) {
					currentPage = MathUtil.range(currentPage + 1, 1, pages.size());

					updatePage();
				}
			}

			@Override
			public ItemStack getItem() {
				return getPageToggleItem(nextPageItemModel, lastPageItemModel, currentPage + 1);
			}
		};
	}

	private ItemStack getPageToggleItem(ItemStack buttonItem, ItemStack noMorePageItem, int toTogglePage) {
		ItemStack item = buttonItem;
		String newName = buttonItem.getItemMeta().getDisplayName().replace("{page}", toTogglePage + "");

		boolean moreThanOnePage = pages.size() > 1;
		boolean hasToTogglePage = toTogglePage >= 1 && toTogglePage <= pages.size();

		return moreThanOnePage ?
				hasToTogglePage ? ItemCreator.of(item).name(newName).build().make() : noMorePageItem
				: CompMaterial.AIR.toItem();
	}

	// Reinits the menu and plays the anvil sound
	private void updatePage() {
		setButtons();
		redraw();
		registerButtons();

		Menu.getSound().play(getViewer());
		PlayerUtil.updateInventoryTitle(getViewer(), compileTitle0());
	}

	// Compile title and page numbers
	private String compileTitle0() {
		final boolean canAddNumbers = addPageNumbers() && pages.size() > 1;

		return getTitle() + (canAddNumbers ? " &8" + currentPage + "/" + pages.size() : "");
	}

	/**
	 * Automatically prepend the title with page numbers
	 *
	 * @param
	 */
	@Override
	protected void onDisplay(final InventoryDrawer drawer) {
		drawer.setTitle(compileTitle0());
		onDisplayAndTitleSet(drawer);
	}

	protected void onDisplayAndTitleSet(InventoryDrawer drawer) {

	}

	/**
	 * Return the {@link ItemStack} representation of an item on a certain page
	 * <p>
	 * Use {@link ItemCreator} for easy creation.
	 *
	 * @param item the given object, for example Arena
	 * @return the itemstack, for example diamond sword having arena name
	 */
	protected abstract ItemStack convertToItemStack(T item);

	/**
	 * Called automatically when an item is clicked
	 *
	 * @param player the player who clicked
	 * @param item the clicked item
	 * @param click the click type
	 */
	protected abstract void onPageClick(Player player, T item, ClickType click);

	/**
	 * Utility: Shall we send update packet when the menu is clicked?
	 *
	 * @return true by default
	 */
	protected boolean updateButtonOnClick() {
		return true;
	}

	/**
	 * Return true if you want our system to add page/totalPages suffix
	 * after your title, true by default
	 *
	 * @return
	 */
	protected boolean addPageNumbers() {
		return true;
	}

	/**
	 * Return if there are no items at all
	 *
	 * @return
	 */
	protected boolean isEmpty() {
		return pages.isEmpty() || pages.get(0).isEmpty();
	}

	/**
	 * Automatically get the correct item from the actual page, including prev/next buttons
	 *
	 * @param slot the slot
	 * @return the item, or null
	 */
	@Override
	public ItemStack getItemAt(final int slot) {
		if (slot < getCurrentPageItems().size()) {
			final T object = getCurrentPageItems().get(slot);

			if (object != null)
				return convertToItemStack(object);
		}

		if (slot == getSize() - 6)
			return prevButton.getItem();

		if (slot == getSize() - 4)
			return nextButton.getItem();

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onMenuClick(final Player player, final int slot, final InventoryAction action, final ClickType click, final ItemStack cursor, final ItemStack clicked, final boolean cancelled) {
		if (slot < getCurrentPageItems().size()) {
			final T obj = getCurrentPageItems().get(slot);

			if (obj != null) {
				final val prevType = player.getOpenInventory().getType();
				onPageClick(player, obj, click);

				if (updateButtonOnClick() && prevType == player.getOpenInventory().getType())
					player.getOpenInventory().getTopInventory().setItem(slot, getItemAt(slot));
			}
		}
	}

	// Do not allow override
	@Override
	public final void onButtonClick(final Player player, final int slot, final InventoryAction action, final ClickType click, final Button button) {
		super.onButtonClick(player, slot, action, click, button);
	}

	// Do not allow override
	@Override
	public final void onMenuClick(final Player player, final int slot, final ItemStack clicked) {
		throw new FoException("Simplest click unsupported");
	}

	// Get all items in a page
	private List<T> getCurrentPageItems() {
		Valid.checkBoolean(pages.containsKey(currentPage - 1), "The menu has only " + pages.size() + " pages, not " + currentPage + "!");

		return pages.get(currentPage - 1);
	}
}