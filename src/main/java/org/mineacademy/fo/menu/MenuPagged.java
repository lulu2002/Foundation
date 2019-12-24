package org.mineacademy.fo.menu;

import java.util.List;
import java.util.Map;

import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.MathUtil;
import org.mineacademy.fo.PlayerUtil;
import org.mineacademy.fo.Valid;
import org.mineacademy.fo.exception.FoException;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.menu.model.PageManager;
import org.mineacademy.fo.remain.CompMaterial;

import lombok.Getter;
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
    protected MenuPagged(Iterable<T> pages) {
        this(9 * 3, pages);
    }

    /**
     * Create a new paged menu
     *
     * @param pageSize size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
     * @param pages    the pages
     */
    protected MenuPagged(int pageSize, Iterable<T> pages) {
        this(pageSize, null, pages);
    }

    /**
     * Create a new paged menu
     *
     * @param pageSize size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
     * @param parent   the parent menu
     * @param pages    the pages the pages
     */
    protected MenuPagged(int pageSize, Menu parent, Iterable<T> pages) {
        this(pageSize, parent, pages, false);
    }

    /**
     * Create a new paged menu
     *
     * @param pageSize               size of the menu, a multiple of 9 (keep in mind we already add 1 row there)
     * @param parent                 the parent menu
     * @param pages                  the pages the pages
     * @param returnMakesNewInstance should we re-instatiate the parent menu when
     *                               returning to it?
     */
    protected MenuPagged(int pageSize, Menu parent, Iterable<T> pages, boolean returnMakesNewInstance) {
        super(parent, returnMakesNewInstance);

        this.currentPage = 1;
        this.pages = PageManager.populate(pageSize, pages);

        setSize(9 + pageSize);
        setButtons();
    }

    // Render the next/prev buttons
    private final void setButtons() {
        // Set previous button
        this.prevButton = new Button() {
            final boolean canGo = currentPage > 1;

            @Override
            public void onClickedInMenu(Player pl, Menu menu, ClickType click) {
                if (canGo) {
                    MenuPagged.this.currentPage = MathUtil.range(currentPage - 1, 1, pages.size());

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
            public void onClickedInMenu(Player pl, Menu menu, ClickType click) {
                if (canGo) {
                    MenuPagged.this.currentPage = MathUtil.range(currentPage + 1, 1, pages.size());

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

        boolean hasPage = toTogglePage >= 1 && toTogglePage <= pages.size();

        return hasPage ? ItemCreator.of(item).name(newName).build().make()
                : noMorePageItem;
    }

    // Reinits the menu and plays the anvil sound
    private final void updatePage() {
        setButtons();
        redraw();
        registerButtons();

        Menu.getSound().play(getViewer());
        PlayerUtil.updateInventoryTitle(getViewer(), compileTitle0());
    }

    // Compile title and page numbers
    private final String compileTitle0() {
        final boolean canAddNumbers = addPageNumbers() && pages.size() > 1;

        return getTitle() + (canAddNumbers ? " &8" + currentPage + "/" + pages.size() : "");
    }

    /**
     * Automatically preped the title with page numbers
     *
     * @param
     */
    @Override
    protected final void onDisplay(InventoryDrawer drawer) {
        drawer.setTitle(compileTitle0());
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
     * @param item   the clicked item
     * @param click  the click type
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
     * Automatically get the correct item from the actual page, including prev/next buttons
     *
     * @param slot the slot
     * @return the item, or null
     */
    @Override
    public ItemStack getItemAt(int slot) {
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
    public final void onMenuClick(Player player, int slot, InventoryAction action, ClickType click, ItemStack cursor, ItemStack clicked, boolean cancelled) {
        if (slot < getCurrentPageItems().size()) {
            final T obj = getCurrentPageItems().get(slot);

            if (obj != null) {
                val prevType = player.getOpenInventory().getType();
                onPageClick(player, obj, click);

                if (updateButtonOnClick() && prevType == player.getOpenInventory().getType())
                    player.getOpenInventory().getTopInventory().setItem(slot, getItemAt(slot));
            }
        }
    }

    // Do not allow override
    @Override
    public final void onButtonClick(Player player, int slot, InventoryAction action, ClickType click, Button button) {
        super.onButtonClick(player, slot, action, click, button);
    }

    // Do not allow override
    @Override
    public final void onMenuClick(Player player, int slot, ItemStack clicked) {
        throw new FoException("Simplest click unsupported");
    }

    // Get all items in a page
    private final List<T> getCurrentPageItems() {
        Valid.checkBoolean(pages.containsKey(currentPage - 1), "The menu has only " + pages.size() + " pages, not " + currentPage + "!");

        return pages.get(currentPage - 1);
    }
}
