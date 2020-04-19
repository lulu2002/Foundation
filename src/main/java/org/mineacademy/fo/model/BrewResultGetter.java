package org.mineacademy.fo.model;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.ReflectionUtil;

import java.lang.reflect.Method;

/**
 * 2019-05-08 下午 02:14
 */
@UtilityClass
public class BrewResultGetter {
    private final Class<?> nmsItemStackClass = ReflectionUtil.getNMSClass("ItemStack");
    private final Class<?> obcItemStackClass = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
    private final Class<?> potionBrewerClass = ReflectionUtil.getNMSClass("PotionBrewer");

    private final Method getBrewResultMethod = ReflectionUtil.getMethod(potionBrewerClass, "d");
    private final Method asBukkitItemStackMethod = ReflectionUtil.getMethod(obcItemStackClass, "asBukkitCopy");
    private final Method asNmsItemStackMethod = ReflectionUtil.getMethod(obcItemStackClass, "asNMSCopy");

    public ItemStack getBrewResult(ItemStack potion, ItemStack ingredient) {
        Object nmsItemStack = ReflectionUtil.invokeStatic(asNmsItemStackMethod, potion);
        Object nmsIngredient = ReflectionUtil.invokeStatic(asNmsItemStackMethod, ingredient);

        Object nmsBrewResult = getResultItem(nmsItemStack, nmsIngredient);

        return ReflectionUtil.invokeStatic(asBukkitItemStackMethod, nmsBrewResult);
    }

    private static Object getResultItem(Object nmsItemStack, Object nmsIngredient) {
        try {
            return ReflectionUtil.invokeStatic(getBrewResultMethod, nmsIngredient, nmsItemStack);
        } catch (Exception e) {
            return ResultGetter1_8.getResultItemLegacy(nmsItemStack, nmsIngredient);
        }
    }

    @UtilityClass
    private static class ResultGetter1_8 {
        private final Class<?> nmsItemClass = ReflectionUtil.getNMSClass("Item");

        private final Method getDataMethod = ReflectionUtil.getMethod(nmsItemStackClass, "getData");
        private final Method getItemMethod = ReflectionUtil.getMethod(nmsItemStackClass, "getItem");
        private final Method getItemStringMethod = ReflectionUtil.getMethod(nmsItemClass, "j", nmsItemStackClass);
        private final Method getBrewResultItemStackDataMethod = ReflectionUtil.getMethod(potionBrewerClass, "a", int.class, String.class);

        public Object getResultItemLegacy(Object nmsItemStack, Object nmsIngredient) {

            int resultData = getResultItemData(nmsItemStack, nmsIngredient);

            ReflectionUtil.invoke("setData", nmsItemStack, resultData);

            return nmsItemStack;
        }

        private int getResultItemData(Object nmsItemStack, Object nmsIngredient) {
            int itemData = ReflectionUtil.invoke(getDataMethod, nmsItemStack);
            Object nmsItem = ReflectionUtil.invoke(getItemMethod, nmsIngredient);
            String string = ReflectionUtil.invoke(getItemStringMethod, nmsItem, nmsIngredient);

            return ReflectionUtil.invokeStatic(getBrewResultItemStackDataMethod, itemData, string);
        }
    }
}